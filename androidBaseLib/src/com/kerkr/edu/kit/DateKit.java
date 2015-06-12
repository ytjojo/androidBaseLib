package com.kerkr.edu.kit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/***
 * 日期操作 工具
 * @author 12
 *
 */
public class DateKit
{
	public static final String HH_MM_SS = "HH:mm:ss";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YY_MM_DD_HH_MM_SS = "yy-MM-dd HH:mm:ss";

	
	
	/**
	 * 获取 当天零点时间
	 * @return
	 */
	public static long getZeroTime()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (cal.getTimeInMillis());
	}
	
	
	/**
	 * 获得 几天后的时间
	 */
	public static Date getAfterDate(Date date, int amount)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, amount);
		return cal.getTime();

	}
	
	public static Date getAfterDate(Date date, int hour,int min)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, hour);
		cal.add(Calendar.MINUTE, min);
		return cal.getTime();

	}


	/**
	 * 返回 指定 年 的 月份的天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 */

	public static int getMonthDay(int year, int month)
	{
		return getMonthDayByYear(year)[--month];
	}

	/**
	 * 通过 年份 返回 每个月 有几天
	 * 
	 * @param year
	 * @return
	 */
	public static int[] getMonthDayByYear(int year)
	{

		int[] monthDay = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) monthDay[1]++;
		return monthDay;
	}

	/**
	 * 判断 2个日期 相差的时间 默认格式"yyyy-MM-dd"
	 * 
	 * @param startTime
	 * @param endTime
	 * @return 返回 一个 数组 每个 元素 依次 表示 相差的天数 小时 分钟 秒
	 */
	public static long[] dateDiff(String startTime, String endTime)
	{
		return dateDiff(startTime, endTime, YYYY_MM_DD);
	}

	/**
	 * 判断 2个日期 相差的时间
	 * 
	 * @param startTime
	 * @param endTime
	 * @param format
	 *            日期的格式 比如 yyyy-MM-dd HH:mm:ss date[0] = day; date[1] = hour;
	 *            date[2] = min; date[3] = sec;
	 * 
	 * @return 返回 一个 数组 每个 元素 依次 表示 相差的天数 小时 分钟 秒
	 */
	public static long[] dateDiff(String startTime, String endTime, String format)
	{

		// 按照传入的格式生成一个simpledateformate对象

		SimpleDateFormat sd = new SimpleDateFormat(format);

		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数

		long nh = 1000 * 60 * 60;// 一小时的毫秒数

		long nm = 1000 * 60;// 一分钟的毫秒数

		long ns = 1000;// 一秒钟的毫秒数

		long diff;

		long[] date = new long[4];

		try
		{

			// 获得两个时间的毫秒时间差异

			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();

			long day = diff / nd;// 计算差多少天
			long hour = diff % nd / nh;// 计算差多少小时
			long min = diff % nd % nh / nm;// 计算差多少分钟
			long sec = diff % nd % nh % nm / ns;// 计算差多少秒

			date[0] = day;
			date[1] = hour;
			date[2] = min;
			date[3] = sec;

		}
		catch (ParseException e)
		{

			e.printStackTrace();

		}

		return date;

	}

	/**
	 * 功能描述：格式化日期
	 * 
	 * @param dateStr
	 *            String 字符型日期
	 * @param format
	 *            String 格式
	 * @return Date 日期
	 */
	public static Date parseDate(String dateStr, String format)
	{
		Date date = null;
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);

			date = dateFormat.parse(dateStr);

		}
		catch (Exception e)
		{
		}
		return date;
	}

	/**
	 * 功能描述：格式化日期
	 * 
	 * @param dateStr
	 *            String 字符型日期：YYYY-MM-DD 格式
	 * @return Date
	 */
	public static Date parseDate(String dateStr)
	{
		return parseDate(dateStr, YYYY_MM_DD);
	}

	/**
	 * 功能描述：格式化输出日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return 返回字符型日期
	 */
	public static String format(Date date, String format)
	{
		String result = "";
		try
		{
			if (date != null)
			{
				DateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(date);
			}
		}
		catch (Exception e)
		{
		}
		return result;
	}

	/**
	 * 功能描述：
	 * 
	 * @param date
	 *            Date 日期
	 * @return
	 */
	public static String format(Date date)
	{
		return format(date, YYYY_MM_DD);
	}

	/**
	 * 功能描述：返回年份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回年份
	 */
	public static int getYear(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 功能描述：返回月份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回月份
	 */
	public static int getMonth(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 功能描述：返回日份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回日份
	 */
	public static int getDay(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 功能描述：返回小时
	 * 
	 * @param date
	 *            日期
	 * @return 返回小时
	 */
	public static int getHour(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 功能描述：返回分钟
	 * 
	 * @param date
	 *            日期
	 * @return 返回分钟
	 */
	public static int getMinute(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 返回秒钟
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回秒钟
	 */
	public static int getSecond(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 功能描述：返回毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	/**
	 * 功能描述：返回字符型日期
	 * 
	 * @param date
	 *            日期
	 * @return 返回字符型日期 yyyy/MM/dd 格式
	 */
	public static String getDate(Date date)
	{
		return format(date, YYYY_MM_DD);
	}

	/**
	 * 功能描述：返回字符型时间
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回字符型时间 HH:mm:ss 格式
	 */
	public static String getTime(Date date)
	{
		return format(date, "HH:mm:ss");
	}

	/**
	 * 功能描述：返回字符型日期时间
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回字符型日期时间 yyyy/MM/dd HH:mm:ss 格式
	 */
	public static String getDateTime(Date date)
	{
		return format(date, YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * 功能描述：日期相加
	 * 
	 * @param date
	 *            Date 日期
	 * @param day
	 *            int 天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int day)
	{
		Calendar calendar = Calendar.getInstance();
		long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	/**
	 * 功能描述：日期相减
	 * 
	 * 返回的 是long 的毫秒
	 * 
	 * @param date
	 *            Date 日期
	 * @param date1
	 *            Date 日期
	 * @return 返回相减后的日期
	 */
	public static long diffDate(Date date, Date date1)
	{
		return ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}

	/**
	 * 功能描述：取得指定月份的第一天
	 * 
	 * @param strdate
	 *            String 字符型日期
	 * @return String yyyy-MM-dd 格式
	 */
	public static String getMonthBegin(String strdate)
	{
		Date date = parseDate(strdate);
		return format(addDate(null, 0), "yyyy-MM") + "-01";
	}

	/**
	 * 功能描述：取得指定月份的最后一天
	 * 
	 * @param strdate
	 *            String 字符型日期
	 * @return String 日期字符串 yyyy-MM-dd格式
	 */
	public static String getMonthEnd(String strdate)
	{
		Date date = parseDate(getMonthBegin(strdate));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return formatDate(calendar.getTime());
	}

	/**
	 * 功能描述：常用的格式化日期
	 * 
	 * @param date
	 *            Date 日期
	 * @return String 日期字符串 yyyy-MM-dd格式
	 */
	public static String formatDate(Date date)
	{
		return formatDateByFormat(date, YYYY_MM_DD);
	}

	/**
	 * 功能描述：以指定的格式来格式化日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return String 日期字符串
	 */
	public static String formatDateByFormat(Date date, String format)
	{
		String result = "";
		if (date != null)
		{
			try
			{
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 得到当前日期是星期几。
	 * 
	 * @return 当为周日时，返回0，当为周一至周六时，则返回对应的1-6。
	 */
	public static final int getCurrentDayOfWeek()
	{
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
	}

}
