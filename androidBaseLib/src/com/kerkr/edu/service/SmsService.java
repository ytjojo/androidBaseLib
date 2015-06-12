package com.kerkr.edu.service;

import com.kerkr.edu.observer.SmsHandler;
import com.kerkr.edu.observer.SmsObserver;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Process;

/**
 * @author Administrator
 *
 */
public class SmsService extends Service {
    public static final String OUT_SMS_URI = "content://sms/outbox";
    public static final String IN_SMS_URI = "content://sms/inbox";
    
    private SmsObserver mObserver;
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onCreate() {
        //在这里启动
        ContentResolver resolver = getContentResolver();
        mObserver = new SmsObserver(resolver, new SmsHandler(this));
        resolver.registerContentObserver(Uri.parse("content://sms"), true, mObserver);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getContentResolver().unregisterContentObserver(mObserver);
    }
}