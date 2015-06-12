/**
 * רע������,���?���ֵ
 *  maoxiang@gmail.com
 *  2010-3-30����04:40:06
 */
package com.kerkr.edu.async;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicLong;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import android.os.StatFs;
import android.util.Log;

/**
 * һ�����߳�֧�ֶϵ���Ĺ�����<br/>
 * 2010-03 ��Htpp Component��д
 */
public class HttpDownloader {

	private final String TAG = "HTTPDOWLOADER";
	private int threads = 5; // �ܹ����߳���
	private int maxThreads = 10; // �����߳���
	private String destUrl; // Ŀ���URL
	private String savePath; // �����·��
	private File lockFile;// ���������ȵ��ļ�
	private String userAgent = "jHttpDownload";
	private boolean useProxy = false;
	private String proxyServer;
	private int proxyPort;
	private String proxyUser;
	private String proxyPassword;
	private int blockSize = 1024 * 4; // 4K һ����
	// 1��λ���һ����,����������Ƿ��������
	private byte[] blockSet;
	private int blockPage; // ÿ���̸߳���Ĵ�С
	private int blocks;
	private boolean running; // �Ƿ�������,�����̲߳����ͷ�
	private DefaultHttpClient httpClient;//
	// =======���ؽ����Ϣ
	private long beginTime;
	private AtomicLong downloaded = new AtomicLong(0); // �����ص��ֽ���\
	private long fileLength; // �ܵ��ֽ���
	// ����߳�,���������Ⱥͻ㱨���
	private MonitorThread monitorThread = new MonitorThread();

	public HttpDownloader(String destUrl, String savePath, int threads) {
		this.threads = threads;
		this.destUrl = destUrl;
		this.savePath = savePath;
	}

	public HttpDownloader(String destUrl, String savePath) {
		this(destUrl, savePath, 5);
	}

	/**
	 * ��ʼ����
	 */
	public boolean download() {
		Log.i(TAG,"�����ļ�" + destUrl + ",����·��=" + savePath);
		beginTime = System.currentTimeMillis();
		boolean ok = false;
		try {
			File saveFile = new File(savePath);
			lockFile = new File(savePath + ".lck");
			if (lockFile.exists() && !lockFile.canWrite()) {
				throw new Exception("无法读取下载进度");
			}
			File parent = saveFile.getParentFile();
			if (!parent.exists()) {
				Log.i(TAG,"文件路径=" + parent.getAbsolutePath());
			}
			if (!parent.canWrite()) {
				throw new Exception("下载文件不可读取");
			}
			if (saveFile.exists()) {
				if (!saveFile.canWrite()) {
					throw new Exception("文件无法保存");
				}
				Log.i(TAG,"文件开始继续下载");
				if (lockFile.exists()) {
					Log.i(TAG,"下载进度可以读取");
					loadPrevious();
				}
			} else {
				lockFile.createNewFile();
			}
			// 1��ʼ��httpClient
			setupHttpClient();
			HttpResponse response = getResponse(0);
			Header length = response.getFirstHeader("Content-Length");
			if (length != null) {
				try {
					fileLength = Long.parseLong(length.getValue());
				} catch (Exception e) {
				}
			}
			Log.i(TAG,"文件大小:" + fileLength);
			if (fileLength <= 0) {
				// ��֧�ֶ��߳�����,���õ��߳�����
				Log.i(TAG,"");
				threads = 1;
			}
			if (response.getFirstHeader("Content-Range") == null) {
				Log.i(TAG,"��������֧�ֶ�����");
				threads = 1;
			} else {
				Log.i(TAG,"������֧�ֶϵ���");
			}
			if (blockSet != null) {
				Log.i(TAG,"����ļ����Ƿ��ܹ���");
				if (blockSet.length * 8l * blockSize < fileLength) {
					Log.i(TAG,"�ļ���С�Ѹı䣬��Ҫ��������");
					blockSet = null;
				}
			}
			//
			if (fileLength > 0 && new StatFs(parent.getAbsolutePath()).getAvailableBlocks() < fileLength) {
				;
				throw new Exception("���̿ռ䲻��");
			}
			if (fileLength > 0) {
				int i = (int) (fileLength / blockSize);
				if (fileLength % blockSize > 0) {
					i++;
				}
				blocks = i;
				Log.i(TAG,"�ļ��Ŀ���:" + blocks);
				blockSet = BitUtil.createBit(blocks);
			} else {
				// һ����
				blocks = 1;
			}
			blockPage = blocks / threads; // ÿ���̸߳���Ŀ���
			Log.i(TAG,"�����̡߳��߳�����=" + threads + ",������=" + blocks + ",���ֽ���="
					+ fileLength + ",ÿ���С=" + blockSize + ",��/�߳�=" + blockPage);
			// ���
			running = true;
			ThreadGroup downloadGroup = new ThreadGroup("download");
			for (int i = 0; i < threads; i++) {
				int begin = i * blockPage;
				int end = (i + 1) * blockPage;
				if (i == threads - 1 && blocks % threads > 0) {
					// ������һ���̣߳���������Ҫ����
					end = blocks;
				}
				// ɨ��ÿ���̵߳Ŀ��Ƿ�����Ҫ���ص�
				boolean needDownload = false;
				for (int j = begin; j < end; j++) {
					if (!BitUtil.getBit(blockSet, j)) {
						needDownload = true;
						break;
					}
				}
				if (!needDownload) {
					Log.i(TAG,"���п��Ѿ��������.Begin=" + begin + ",End=" + end);

				}
				// �������������߳�
				DownloadThread downloadThread = new DownloadThread(
						downloadGroup, i, begin, end);
				downloadThread.start();
			}
			monitorThread.setStop(false);
			monitorThread.start();
			while (downloadGroup.activeCount() > 0) {
				Thread.sleep(2000);
			}
			ok = true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG,e.toString());
		} finally {
			// closeHttpClient();
			if (ok) {
				Log.i(TAG,"ɾ�����ļ�:" + lockFile.getAbsolutePath());
				lockFile.delete();
			}
			httpClient = null;
		}
		monitorThread.setStop(true);
		Log.i(TAG,"������ɣ���ʱ:"
				+ getTime((System.currentTimeMillis() - beginTime) / 1000));
		return ok;
	}

	private void loadPrevious() throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		FileInputStream inStream = new FileInputStream(lockFile);
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = inStream.read(buffer))) {
			outStream.write(buffer, 0, n);
		}
		outStream.close();
		inStream.close();
		blockSet = outStream.toByteArray();
		Log.d(TAG,"֮ǰ���ļ���СӦ����:" + blockSet.length * 8l * blockSize);
	}

	private void setupHttpClient() throws Exception {
		HttpParams params = new BasicHttpParams();
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
				schemeRegistry);
		httpClient = new DefaultHttpClient(cm, params);
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
				userAgent);
		ConnPerRoute defaultConnPerRoute = new ConnPerRoute() {

			public int getMaxForRoute(HttpRoute route) {
				return maxThreads;
			}

		};
		httpClient.getParams().setParameter(
				ConnManagerParams.MAX_CONNECTIONS_PER_ROUTE,
				defaultConnPerRoute);
		// �������Ի���
		httpClient.setHttpRequestRetryHandler(myRetryHandler);
		if (useProxy) {
			Log.i(TAG,"���ô��������=" + proxyServer + ",�˿�=" + proxyPort);
			final HttpHost proxy = new HttpHost(proxyServer, proxyPort, "http");
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
			if (proxyUser != null && proxyPassword != null) {
				httpClient.getCredentialsProvider().setCredentials(
						AuthScope.ANY,
						new UsernamePasswordCredentials(proxyUser,
								proxyPassword));
			}
		}
	}

	private HttpResponse response0; // �������ٷ���,����һ������

	private HttpResponse getResponse(long pos) throws Exception {
		if (pos == 0 && response0 != null) {
			return response0;
		}
		HttpGet httpget = new HttpGet(destUrl);
		HttpContext localContext = new BasicHttpContext();
		// ʵ�ֶ��߳����صĺ��ģ�Ҳ��������ʵ�ֶϵ���
		httpget.addHeader("RANGE", "bytes=" + pos + "-");
		HttpResponse response = httpClient.execute(httpget, localContext);
		if (pos == 0) {
			response0 = response;
		}
		return response;
	}

	// ���Ի���
	private HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
			if (executionCount >= 5) {
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				return false;
			}
			HttpRequest request = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
			if (idempotent) {
				return true;
			}
			return false;
		}
	};

	private String getDesc() {
		long downloadBytes = downloaded.longValue();

		return String.format("������/�ܴ�С=%s/%s(%s),�ٶ�:%s,��ʱ:%s,ʣ���С:%d",
				getFileSize(downloadBytes), getFileSize(fileLength),
				getProgress(fileLength, downloadBytes),
				getFileSize(downloadBytes
						/ ((System.currentTimeMillis() - beginTime) / 1000)),
				getTime((System.currentTimeMillis() - beginTime) / 1000),
				fileLength - downloadBytes);
	}

	private String getFileSize(long totals) {
		// �����ļ���С
		int i = 0;
		String j = "BKMGT";
		float s = totals;
		while (s > 1024) {
			s /= 1024;
			i++;
		}
		return String.format("%.2f", s) + j.charAt(i);
	}

	private String getProgress(long totals, long read) {
		if (totals == 0)
			return "0%";
		return String.format("%d", read * 100 / totals) + "%";
	}

	private String getTime(long seconds) {
		int i = 0;
		String j = "���ʱ��";
		long s = seconds;
		String result = "";
		while (s > 0) {
			if (s % 60 > 0) {
				result = String.valueOf(s % 60) + (char) j.charAt(i) + result;
			}
			s /= 60;
			i++;
		}
		return result;
	}

	/**
	 * һ�������߳�.
	 */
	private class DownloadThread extends Thread {

		private RandomAccessFile destFile; // ����ʵ�ֱ��������ļ�
		private int id = 0;
		private int blockBegin = 0; // ��ʼ��
		private int blockEnd = 0; // �����
		private long pos;// ���ָ��

		private String getThreadName() {
			return "DownloadThread-" + id + "=>";
		}

		public DownloadThread(ThreadGroup group, int id, int blockBegin,
				int blockEnd) throws Exception {
			super(group, "downloadThread-" + id);
			this.id = id;
			this.blockBegin = blockBegin;
			this.blockEnd = blockEnd;
			this.pos = 1l * blockBegin * blockSize; // ת��Ϊ������
			destFile = new RandomAccessFile(savePath, "rw");
		}

		public void run() {
			BufferedInputStream inputStream = null;
			try {
				Log.i(TAG,getThreadName() + "�����߳�." + this.toString());
				Log.i(TAG,getThreadName() + ":��λ�ļ�λ��.Pos=" + 1l * blockBegin
						* blockSize);
				destFile.seek(1l * blockBegin * blockSize);
				Log.i(TAG,getThreadName() + ":��ʼ����.[ " + blockBegin + " - "
						+ blockEnd + "]");

				HttpResponse response = getResponse(pos);
				inputStream = new BufferedInputStream(response.getEntity()
						.getContent());
				byte[] b = new byte[blockSize];
				while (blockBegin < blockEnd) {
					if (!running) {
						Log.i(TAG,getThreadName() + ":ֹͣ����.��ǰ��:" + blockBegin);
						return;
					}
					Log.d(TAG,getThreadName() + "���ؿ�=" + blockBegin);
					int counts = 0; // �������ֽ���
					if (BitUtil.getBit(blockSet, blockBegin)) {
						Log.d(TAG,getThreadName() + ":�������Ѿ����=" + blockBegin);
						destFile.skipBytes(blockSize);
						int skips = 0;
						while (skips < blockSize) {
							skips += inputStream.skip(blockSize - skips);
						}
						downloaded.addAndGet(blockSize);

					} else {
						while (counts < blockSize) {
							int read = inputStream.read(b, 0, blockSize
									- counts);
							if (read < 0)
								break;
							counts += read;
							destFile.write(b, 0, read);
							downloaded.addAndGet(read);
						}
						BitUtil.setBit(blockSet, blockBegin, true); // ����Ѿ��������
					}
					blockBegin++;
				}
				response.getEntity().consumeContent();
				Log.i(TAG,getThreadName() + "�������.");
				return;
			} catch (Exception e) {
				Log.e(TAG,getThreadName() + "���ش���:" + e.getMessage());
				e.printStackTrace();
			} finally {
				try {
					if (inputStream != null)
						inputStream.close();
				} catch (Exception te) {
					Log.e(TAG,te.toString());
				}
				try {
					if (destFile != null)
						destFile.close();
				} catch (Exception te) {
					Log.e(TAG,te.toString());
				}
			}
		}
	}

	// ����߳�,�������ȣ������´ζϵ���
	private class MonitorThread extends Thread {
		boolean stop = false;

		public void setStop(boolean stop) {
			this.stop = stop;
		}

		public void run() {
			FileOutputStream saveStream = null;
			try {
				saveStream = new FileOutputStream(lockFile);
				while (running && !stop) {
					Log.i(TAG,getDesc());
					// ������
					saveStream.write(blockSet);
					sleep(1000);
				}
			} catch (Exception e) {
				Log.e(TAG,e.toString());
			} finally {
				if (saveStream != null) {
					try {
						saveStream.close();
					} catch (Exception e) {
						Log.e(TAG,e.toString());
					}
				}
			}
		}
	}

	// ��������λ�Ĺ���
	private static class BitUtil {
		public static byte[] createBit(int len) {
			int size = len / Byte.SIZE;
			if (len % Byte.SIZE > 0) {
				size++;
			}
			return new byte[size];
		}

		/** ȡ��ĳλ����0 ����1 */
		public static boolean getBit(byte[] bits, int pos) {
			int i = pos / Byte.SIZE;
			int b = bits[i];
			int j = pos % Byte.SIZE;
			byte c = (byte) (0x80 >>> (j - 1));
			return b == c;
		}

		/** ����ĳλ����0 ����1 */
		public static void setBit(byte[] bits, int pos, boolean flag) {
			int i = pos / Byte.SIZE;
			byte b = bits[i];
			int j = pos % Byte.SIZE;
			byte c = (byte) (0x80 >>> (j - 1));
			if (flag) {
				bits[i] = (byte) (b | c);
			} else {
				c = (byte) (0xFF ^ c);
				bits[i] = (byte) (b & c);
			}
		}
	}
}
