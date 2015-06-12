package com.kerkr.edu.utill;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author hyr
 *         格式化工具类
 */
public class VADataFormat {
    
    /**
     * 服务器时间格式化
     */
    public static String DateFormat(long serverDateMilliseconds, String pattern) {
        Date date = new Date(1000 * serverDateMilliseconds);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
    
    /**
     * 格式化金额
     */
    public static String format(String pattern, double value) {
        String pre = "";
        DecimalFormat df = new DecimalFormat("0.##");
        //        df.applyPattern(pattern);
        pre = df.format(value);
        return pre;
    }
    
    public static double format2Double(String pattern, double value) {
        String pre = "";
        DecimalFormat df = new DecimalFormat("0.##");
        pre = df.format(value);
        return Double.parseDouble(pre);
    }
    
    /**
     * 格式化折扣
     */
    public static String formatDiscount(double value) {
        String pre = "";
        DecimalFormat df = new DecimalFormat("0.0");
        pre = df.format(value);
        return pre;
    }
    
    /**
     * 格式化一位小数
     */
    public static String formatSingle(double value) {
        String pre = "";
        DecimalFormat df = new DecimalFormat("0.#");
        pre = df.format(value);
        return pre;
    }
    
    /**
     * 格式化为全角
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
    
    /**
     * 格式化星期
     */
    public static String formatDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String day = null;
        int dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        switch (dayForWeek) {
            case 0:
                day = "星期日";
                break;
            case 1:
                day = "星期一";
                break;
            case 2:
                day = "星期二";
                break;
            case 3:
                day = "星期三";
                break;
            case 4:
                day = "星期四";
                break;
            case 5:
                day = "星期五";
                break;
            case 6:
                day = "星期六";
                break;
            default:
                break;
        }
        return day;
    }
    
    //获得当天24点时间
    public static long getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
    /**
     * 服务器时间与本地时间差（天数）
     */
    public static int DateCompare(long serverDateMilliseconds) {
        //        Date serverDate = new Date(1000 * serverDateMilliseconds);
        //        Date localDate = new Date(System.currentTimeMillis ());
        int days = 0;
        long betweenTime = 0;
        betweenTime = 1000 * serverDateMilliseconds - System.currentTimeMillis();
        
        if (betweenTime > 0) {
            days = (int) (betweenTime / (1000 * 60 * 60 * 24));
//            days++;
        }
        return days;
    }
}
