/*
 * 文 件 名:  AlarmWrapper.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-5
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.alarm;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import com.drjane.promise.model.Alarm;
import com.drjane.promise.model.Alarm.Day;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-5]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AlarmWrapper {
    
    private Alarm mAlarm;
    
    public AlarmWrapper(Alarm a) {
        this.mAlarm = a;
        
    }
    
    public void schedule(Context context) {
        mAlarm.setAlarmActive(true);
        
        Intent myIntent = new Intent(context, AlarmServiceBroadcastReciever.class);
        myIntent.putExtra("alarm", mAlarm);
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        
        alarmManager.set(AlarmManager.RTC_WAKEUP, mAlarm.getAlarmTime().getTimeInMillis(), pendingIntent);
    }
    
    public String getTimeUntilNextAlarmMessage() {
        long timeDifference = mAlarm.getAlarmTime().getTimeInMillis() - System.currentTimeMillis();
        long days = timeDifference / (1000 * 60 * 60 * 24);
        long hours = timeDifference / (1000 * 60 * 60) - (days * 24);
        long minutes = timeDifference / (1000 * 60) - (days * 24 * 60) - (hours * 60);
        long seconds = timeDifference / (1000) - (days * 24 * 60 * 60) - (hours * 60 * 60) - (minutes * 60);
        String alert = "Alarm will sound in ";
        if (days > 0) {
            alert += String.format("%d days, %d hours, %d minutes and %d seconds", days, hours, minutes, seconds);
        }
        else {
            if (hours > 0) {
                alert += String.format("%d hours, %d minutes and %d seconds", hours, minutes, seconds);
            }
            else {
                if (minutes > 0) {
                    alert += String.format("%d minutes, %d seconds", minutes, seconds);
                }
                else {
                    alert += String.format("%d seconds", seconds);
                }
            }
        }
        return alert;
    }
    
    /**
    * @return the alarmTime
    */
    public String getAlarmTimeString() {
        
        String time = "";
        if (mAlarm.getAlarmTime().get(Calendar.HOUR_OF_DAY) <= 9)
            time += "0";
        time += String.valueOf(mAlarm.getAlarmTime().get(Calendar.HOUR_OF_DAY));
        time += ":";
        
        if (mAlarm.getAlarmTime().get(Calendar.MINUTE) <= 9)
            time += "0";
        time += String.valueOf(mAlarm.getAlarmTime().get(Calendar.MINUTE));
        
        return time;
    }
    
    /**
     * @param alarmTime
     *            the alarmTime to set
     */
    public void setAlarmTime(String alarmTime) {
        
        String[] timePieces = alarmTime.split(":");
        
        Calendar newAlarmTime = Calendar.getInstance();
        newAlarmTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timePieces[0]));
        newAlarmTime.set(Calendar.MINUTE, Integer.parseInt(timePieces[1]));
        newAlarmTime.set(Calendar.SECOND, 0);
        mAlarm.setAlarmTime(newAlarmTime);
    }
    public String getRepeatDaysString() {
        StringBuilder daysStringBuilder = new StringBuilder();
        if (mAlarm.getDays().length == Day.values().length) {
            daysStringBuilder.append("Every Day");
        }
        else {
            Arrays.sort(mAlarm.getDays(), new Comparator<Day>() {
                @Override
                public int compare(Day lhs, Day rhs) {
                    
                    return lhs.ordinal() - rhs.ordinal();
                }
            });
            for (Day d : mAlarm.getDays()) {
                switch (d) {
                    case TUESDAY:
                    case THURSDAY:
                        //                  daysStringBuilder.append(d.toString().substring(0, 4));
                        //                  break;
                    default:
                        daysStringBuilder.append(d.toString().substring(0, 3));
                        break;
                }
                daysStringBuilder.append(',');
            }
            daysStringBuilder.setLength(daysStringBuilder.length() - 1);
        }
        
        return daysStringBuilder.toString();
    }
    
}
