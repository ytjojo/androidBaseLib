package com.kerkr.edu.async;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import android.util.Log;

public class ExecutorManager {
	private static final String TAG = ExecutorManager.class.getSimpleName();
	private final int THREAD_MAX_COUNT = getNumCores() * 2;
	private final int THREAD_SCHEDULE_SIZE = 5;
	private static int coreCount = Runtime.getRuntime().availableProcessors();
	private ExecutorService mFixedPool = Executors
			.newFixedThreadPool(THREAD_MAX_COUNT);
	private ExecutorService mSinglePool = Executors.newSingleThreadExecutor();
	private ExecutorService mCachePool = Executors.newCachedThreadPool();
	private ScheduledExecutorService mSchedulePool = Executors
			.newScheduledThreadPool(THREAD_SCHEDULE_SIZE);

	private static ExecutorManager instance = null;

	public static ExecutorManager getInstace() {
		if (instance == null) {
			synchronized (ExecutorManager.class) {
				instance = new ExecutorManager();
			}
		}
		return instance;
	}

	public void executeFixed(Runnable r) {
		mFixedPool.execute(r);
	}

	public void executeSingle(Runnable r) {
		mSinglePool.execute(r);
	}

	public void executeCache(Runnable r) {
		mCachePool.execute(r);
	}

	// 安排所提交的Callable或Runnable任务在initDelay指定的时间后执行。
	public void executeSchedule(Runnable r, long delay) {
		mSchedulePool.schedule(r, delay, TimeUnit.MILLISECONDS);
	}

	/**
	 * 
	 * 安排所提交的Runnable任务按指定的间隔重复执行
	 * 
	 * @param r
	 * @param initDelay
	 *            ：初始化延时
	 * @param period
	 *            :两次开始执行最小间隔时间
	 */
	public void executeScheduleAtFixedRate(Runnable r, long initDelay,
			long period) {
		mSchedulePool.scheduleAtFixedRate(r, initDelay, period,
				TimeUnit.MILLISECONDS);
	}

	/**
	 * 
	 * 安排所提交的Runnable任务在每次执行完后，等待delay所指定的时间后重复执行
	 * 
	 * @param r
	 * @param initDelay
	 * @param period
	 *            前一次执行结束到下一次执行开始的间隔时间（间隔执行延迟时间）
	 */
	public void executeScheduleAtFixedDelay(Runnable r, long initDelay,
			long period) {
		mSchedulePool.scheduleWithFixedDelay(r, initDelay, period,
				TimeUnit.MILLISECONDS);
	}

	public void shutdownAndAwaitTermination(ExecutorService pool) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					Log.e(TAG,"Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	// 获得cpu核心数
	private static int getNumCores() {
		// Private Class to display only CPU devices in the directory listing
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				// Check if filename is "cpu", followed by a single digit number
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}
		}

		try {
			// Get directory containing CPU info
			File dir = new File("/sys/devices/system/cpu/");
			// Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			Log.d(TAG, "CPU Count: " + files.length);
			// Return the number of cores (virtual CPU devices)
			return files.length;
		} catch (Exception e) {
			// Print exception
			Log.d(TAG, "CPU Count: Failed.");
			e.printStackTrace();
			// Default to return 1 core
			return coreCount;
		}
	}
	public ExecutorService getExecutorFixedPool(){
		return mFixedPool;
	}
	public ExecutorService getExecutorCachePoll(){
		return mCachePool;
	}
}
