/*
 * 文 件 名:  TimerSetter.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-5
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.alarm;

import java.util.Calendar;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-5]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TimerSetter {
    
    public static final long SECOND_MILLISECONDS = 1000L;
    public static final long MINUTE_MILLISECONDS = 60000L;
    public static final long HOUR_MILLISECONDS = 36000000L;
    public static final long DAY_MILLISECONDS = 36000000 * 24L;
    public static final long WEEK_MILLISECONDS = 36000000 * 24L;
    public static long YEAR_MILLISECONDS= 365L *DAY_MILLISECONDS; 
    public static void schedule(Context c,long initialDelay,long delay,int repeatCount,int id){
        AlarmManager mAlarmMgr=(AlarmManager)c.getSystemService(c.ALARM_SERVICE);
        
        Intent intent = new Intent( c.getApplicationContext(), AlarmServiceBroadcastReciever.class);
        intent.putExtra("id", id);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast( c.getApplicationContext(), 0 , intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mAlarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + initialDelay, delay, mPendingIntent);
    }
    public static void schedule(Context c,long delay,int id){
        AlarmManager mAlarmMgr=(AlarmManager)c.getSystemService(c.ALARM_SERVICE);
        Intent intent = new Intent( c.getApplicationContext(), AlarmServiceBroadcastReciever.class);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast( c.getApplicationContext(), 0 , intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mAlarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, mPendingIntent); 
      
    }
    public static void cancel(Context c,int id){
        Intent intent =new Intent(c.getApplicationContext(), AlarmServiceBroadcastReciever.class);
        intent.putExtra("id", id);
        PendingIntent sender=PendingIntent
               .getBroadcast(c.getApplicationContext(), 0, intent, 0);
        AlarmManager alarm=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
    }
    
    public long getMillisecondBetween(Calendar c1,Calendar c2){
        long time1 = c1.getTimeInMillis();                  
        long time2 = c2.getTimeInMillis();
        return Math.abs(time1 -time2);
    }
}
