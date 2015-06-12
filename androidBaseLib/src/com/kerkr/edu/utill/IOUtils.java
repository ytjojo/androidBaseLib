package com.kerkr.edu.utill;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * IO流 工具类<br>
 * 很简单,仅支持文本操作
 * 
 * @author KEZHUANG
 */
public class IOUtils {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

	/**
	 * 文本的写入操作
	 * 
	 * @param filePath
	 *            文件路径。一定要加上文件名字 <br>
	 *            例如：../a/a.txt
	 * @param content
	 *            写入内容
	 */
	public static void write(String filePath, String content) {
		BufferedWriter bufw = null;
		try {
			bufw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filePath)));
			bufw.write(content);
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (bufw != null) {
				try {
					bufw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 文本的读取操作
	 * 
	 * @param path
	 *            文件路径,一定要加上文件名字<br>
	 *            例如：../a/a.txt
	 * @return
	 */
	public static String read(String path) {
		BufferedReader bufr = null;
		try {
			bufr = new BufferedReader(new InputStreamReader(
					new FileInputStream(path)));
			StringBuffer sb = new StringBuffer();
			String str = null;
			while ((str = bufr.readLine()) != null) {
				sb.append(str);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufr != null) {
				try {
					bufr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 文本的读取操作
	 * 
	 * @param path
	 *            文件路径,一定要加上文件名字<br>
	 *            例如：../a/a.txt
	 * @return
	 */
	public static String read(InputStream is) {
		BufferedReader bufr = null;
		try {
			bufr = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String str = null;
			while ((str = bufr.readLine()) != null) {
				sb.append(str);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufr != null) {
				try {
					bufr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static final int BUFFER_SIZE = 1024;
	
	/**
	 * 
	 * @param path
	 * 
	 * java nio 内存映射读取文件，速度更快
	 * @return byte[] 返回byte数组
	 */
    public byte[] getMemoryMappingFile(String path) {

        File file = new File(path);
        FileInputStream in;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            in = new FileInputStream(file);
            FileChannel channel = in.getChannel();
            MappedByteBuffer buff = channel.map(FileChannel.MapMode.READ_ONLY,
                    0, channel.size());

            byte[] b = new byte[1024];
            int len = (int) file.length();

            long begin = System.currentTimeMillis();

            for (int offset = 0; offset < len; offset += 1024) {

                if (len - offset > BUFFER_SIZE) {
                    buff.get(b);
                } else {
                    buff.get(new byte[len - offset]);
                }
                baos.write(buff.array());
            }

            return baos.toByteArray();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
        }finally{
            
        }

        return null;
    }
    /**
     * 将输入流转换为字符串。默认采用UTF-8编码。
     *
     * @param in
     *            输入流
     * @return 转换之后的字符串。
     * @throws IOException
     */
    public static String inputStreamToString(InputStream in) throws IOException {
        return readFully(new InputStreamReader(in, UTF_8));
    }

    /**
     * 将输入流转换为字符串。
     *
     * @param in
     *            输入流
     * @param charset
     *            字符编码。
     * @return 转换之后的字符串。
     * @throws IOException
     */
    public static String inputStreamToString(InputStream in, Charset charset) throws IOException {
        return readFully(new InputStreamReader(in, charset));
    }

    /**
     * 以字符串类型返回{@code reader}的剩下的内容。
     *
     * @param reader
     * @return 返回Reader对象剩下的内容。
     * @throws IOException
     */
    public static String readFully(Reader reader) throws IOException {
        try {
            StringWriter writer = new StringWriter();
            char[] buffer = new char[1024];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }
            return writer.toString();
        } finally {
            reader.close();
        }
    }

}