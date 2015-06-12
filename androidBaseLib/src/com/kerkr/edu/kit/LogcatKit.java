package com.kerkr.edu.kit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

/**
 * 获得 logcat 的 日志 信息
 * 
 * <uses-permission android:name="android.permission.READ_LOGS" />
 * 
 * @author jayqqaa12
 * @date 2013-5-15
 */
public class LogcatKit  
{

	private static String cmd;

	/**
	 * 在sdcard根目录中输出标签为tag的log
	 * 
	 * @param tag
	 *            log的标签
	 */
	public static void startLog(String tag)
	{
		startLog(null, tag);
	}

	/**
	 * 在sdcard根目录中输出标签为tag的log
	 * 
	 * @param fileName
	 *            sdcard上生成的log文件名
	 * @param tag
	 *            log的标签
	 */
	public static void startLog(String fileName, String tag)
	{
		if (fileName == null)
		{
			fileName = tag;
		}
		startLog(null, fileName, tag);
	}

	/**
	 * 在saveiDir目录中输出标签为tag的log
	 * 
	 * @param saveiDir
	 *            log在sdcard中指定名称的文件夹中输出
	 * @param fileName
	 *            sdcard上生成的log文件名
	 * @param tag
	 *            log的标签
	 */
	public static void startLog(String saveiDir, String fileName, String tag)
	{
		if (saveiDir == null)
		{
			startLog(Environment.getExternalStorageDirectory().getPath() + "/", fileName, tag, "V");
		}
		else startLog(Environment.getExternalStorageDirectory().getPath() + "/" + saveiDir + "/", fileName, tag, "V");
	}

	/**
	 * 在saveiDir目录中输出标签为tag 优先级为proority 的log
	 * 
	 * @param saveiDir
	 *            log在sdcard中指定名称的文件夹中输出
	 * @param fileName
	 *            sdcard上生成的log文件名
	 * @param tag
	 *            log的标签
	 * @param priority
	 *            V — Verbose (lowest priority); D — Debug; I — Info; W —
	 *            Warning; E — Error; F — Fatal; S — Silent (highest priority,
	 *            on which nothing is ever printed)
	 */
	public static void startLog(String saveiDir, String fileName, String tag, String priority)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyMMdd_HHmmss");
		String nowStr = format.format(new Date());
		new File(saveiDir).mkdirs();
		cmd = "logcat -d -v time -f " + saveiDir + fileName + ".log -s " + tag + ":" + priority;
		try
		{
			Runtime.getRuntime().exec("rm " + saveiDir + fileName + ".log");
			Runtime.getRuntime().exec(cmd);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 获得 指定 tag 的  BufferedReader
	 * @param tag
	 * @return
	 */
	public static BufferedReader getLog(String tag, String priority)
	{
		Process process;
		try
		{
			Runtime.getRuntime().exec("logcat -c");
			process = Runtime.getRuntime().exec("logcat -s " + tag+":"+priority);
			
			return new BufferedReader(new InputStreamReader(process.getInputStream()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return null;

	}


}
