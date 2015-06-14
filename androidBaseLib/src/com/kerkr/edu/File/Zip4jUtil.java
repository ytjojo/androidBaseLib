package com.kerkr.edu.File;

import java.io.File;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

public class Zip4jUtil {
	public static void unZipFile(String zipFileName, String targetBaseDir) {
		if (!targetBaseDir.endsWith(File.separator)) {
			targetBaseDir += File.separator;
		}
		try {

			ZipFile zipFile = new ZipFile(zipFileName);

			if (zipFile.isEncrypted()) {
				// 在这里输入密码哦，如果写错了，会报异常的说
				zipFile.setPassword("viewalloc@meishidiandian");
			}
			zipFile.extractAll(targetBaseDir);
		} catch (ZipException e) {
		}
	}

	public static void unZipFileWithProgress(final File zipFile,
			final String filePath, String password, final Handler handler,
			final boolean isDeleteZip) throws ZipException {
		ZipFile zFile = new ZipFile(zipFile);
		zFile.setFileNameCharset("GBK");

		if (!zFile.isValidZipFile()) { //
			throw new ZipException("exception!");
		}
		File destDir = new File(filePath); // ��ѹĿ¼
		if (destDir.isDirectory() && !destDir.exists()) {
			destDir.mkdir();
		}
		if (zFile.isEncrypted()) {
			zFile.setPassword(password); // 设置解压密码
		}

		final ProgressMonitor progressMonitor = zFile.getProgressMonitor();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Bundle bundle = null;
				Message msg = null;
				try {
					int precentDone = 0;
					if (handler == null) {
						return;
					}
					handler.sendEmptyMessage(CompressStatus.START);
					while (true) {
						// 每隔50ms,发送一个解压进度出去
						Thread.sleep(50);
						precentDone = progressMonitor.getPercentDone();
						bundle = new Bundle();
						bundle.putInt(CompressStatus.PERCENT, precentDone);
						msg = Message.obtain(handler);
						msg.what = CompressStatus.HANDLING;
						msg.setData(bundle);
						handler.sendMessage(msg); // 通过 Handler将进度扔出去
						if (precentDone >= 100) {
							break;
						}
					}
					handler.sendEmptyMessage(CompressStatus.COMPLETED);
				} catch (InterruptedException e) {
					bundle = new Bundle();
					bundle.putString(CompressStatus.ERROR_COM, e.getMessage());
					msg = new Message();
					msg.what = CompressStatus.ERROR;
					msg.setData(bundle);
					handler.sendMessage(msg);
					e.printStackTrace();
				} finally {
					if (isDeleteZip) {
						zipFile.delete();// 将原压缩文件删除
					}
				}
			}
		});
		thread.start();
		zFile.setRunInThread(true); // true 在子线程中进行解压 , false主线程中解压
		zFile.extractAll(filePath); // 将
	}

	public static class CompressStatus {
		public final static int START = 10000;
		public final static int HANDLING = 10001;
		public final static int COMPLETED = 10002;
		public final static int ERROR = 10003;

		public final static String PERCENT = "PERCENT";
		public final static String ERROR_COM = "ERROR";
	}
}
