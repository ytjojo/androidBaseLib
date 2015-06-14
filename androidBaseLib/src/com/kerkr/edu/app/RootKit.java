package com.kerkr.edu.app;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.RootToolsException;

public class RootKit
{


	// ///////////////////////--------------------ROOT TOOLS .jar 封装
	// //////////////////////////////////////////////

	/**
	 * 直接 发送 指令
	 * 
	 * @param name
	 */
	public static void sendShell(String cmd)
	{
		try
		{
			RootTools.sendShell(cmd, 2);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (RootToolsException e)
		{
			e.printStackTrace();
		}
		catch (TimeoutException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * 杀死 进程
	 * 
	 * @param pid
	 */
	public static void killProcess(final int pid)
	{
		new Thread() {
			public void run()
			{
				try
				{
					RootTools.sendShell("kill -9 " + pid, 2);

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			};
		}.start();
	}
}
