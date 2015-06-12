/*
 * 文 件 名:  AppSettings.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-1
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.kerkr.edu.app;

import android.net.Uri;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-1]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AppSettings {
    
    
    private static AppSettings mInstance ;
    public static AppSettings get(){
        if(mInstance == null){
            mInstance = new AppSettings();
        }
        return mInstance;
    }
    
    public boolean isNotificationVibrationEnabled(){
        return true;
    }
    
    public long getNotificationTime(){
        return 1000L;
    }
    public boolean isShowNotificationsEnabled(){
        return true;
    }
    public Uri getRingtoneUri(){
        return null;
    }
}
