package com.kerkr.edu.utill;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.kerkr.edu.app.Constans;
import com.kerkr.edu.consts.NotificationsCustomType;
import com.kerkr.edu.dto.UMengPush;
import com.kerkr.edu.eventbus.PushNotifyEvent;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by Hyr on 2015/1/15.
 * Desc:
 */
public class UMengPushManager {
    public static List<UMengPush> messageList = new ArrayList<UMengPush>();
    //饿汉模式
    private static UMengPushManager INSTANCE = null;
    private Context mContext;
    
    private UMengPushManager(Context context) {
        mContext = context;
    }
    
    public static UMengPushManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (UMengPushManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UMengPushManager(context);
                }
            }
        }
        return INSTANCE;
    }
    
    public void AddNewNotification(UMengPush message) {
        //        SharedPreferences sharedPreferences=mContext.getSharedPreferences (Constans.APP_NOTIFICATON_SET,Context.MODE_PRIVATE);
        if (message == null) {
            return;
        }
        Map<String, String> msgExtra = message.extra;
        if (msgExtra != null && msgExtra.size() > 0) {
            String notifyType = msgExtra.get(Constans.NOTIFY_TYPE);
            //点单因为多个订单的需要共存，单独处理
            if (notifyType.equals(String.valueOf(NotificationsCustomType.NOTIFICATIONS_ORDERDETAIL))) {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constans.APP_NOTIFICATON_SET_FOR_ORDER, Context.MODE_PRIVATE);
                String json = JsonParser.getInstance().getJsonFromObject(message);
                //即订单号码
                String notifyValue = msgExtra.get(Constans.NOTIFY_VALUE);
                Editor editor = sharedPreferences.edit();
                editor.putString(notifyValue, json);
                editor.commit();
            }
            else {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constans.APP_NOTIFICATON_SET, Context.MODE_PRIVATE);
                String json = JsonParser.getInstance().getJsonFromObject(message);
                Editor editor = sharedPreferences.edit();
                editor.putString(notifyType, json);
                editor.commit();
            }
        }
        notifyToUIThread();
    }
    
    //通知主线程收到推送
    public void notifyToUIThread() {
        if (AppUtil.isAppOnForeground(mContext)) {
            //            MainContentTabActivity mainActivity= (MainContentTabActivity) VAAppManager.getAppManager ().getBaseActivityByName ("MainContentTabActivity");
            //
            //            if (mainActivity!=null)
            //            {
            EventBus.getDefault().postSticky(new PushNotifyEvent());
            //            }
        }
        
    }
    
    public List<UMengPush> getSavedNotification() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constans.APP_NOTIFICATON_SET, Context.MODE_PRIVATE);
        Map<String, String> savedPush = (Map<String, String>) sharedPreferences.getAll();
        //保存的订单类推送
        SharedPreferences sharedPreferencesForOrder = mContext.getSharedPreferences(Constans.APP_NOTIFICATON_SET_FOR_ORDER, Context.MODE_PRIVATE);
        Map<String, String> savedPushForOrder = (Map<String, String>) sharedPreferencesForOrder.getAll();
        List<UMengPush> messageList = new ArrayList<UMengPush>();
        if (savedPush != null && savedPush.size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = savedPush.entrySet().iterator();
            
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (!TextUtils.isEmpty(entry.getValue())) {
                    UMengPush message = JsonParser.getInstance().getObjectFromJson(entry.getValue(), UMengPush.class);
                    messageList.add(message);
                }
            }
        }
        
        if (savedPushForOrder != null && savedPushForOrder.size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = savedPushForOrder.entrySet().iterator();
            
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (!TextUtils.isEmpty(entry.getValue())) {
                    UMengPush message = JsonParser.getInstance().getObjectFromJson(entry.getValue(), UMengPush.class);
                    messageList.add(message);
                }
            }
        }
        
        return messageList;
        
    }
    
    public void removeNotification(String notifyType, String value) {
        if (TextUtils.isEmpty(notifyType)) {
            return;
        }
        if (notifyType.equals(String.valueOf(NotificationsCustomType.NOTIFICATIONS_ORDERDETAIL))) {
            if (TextUtils.isEmpty(value)) {
                return;
            }
            //删除对ing点单推送
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constans.APP_NOTIFICATON_SET_FOR_ORDER, Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.remove(value);
            editor.commit();
        }
        else {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constans.APP_NOTIFICATON_SET, Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.remove(notifyType);
            editor.commit();
        }
        //删除后通知Ui
        EventBus.getDefault().postSticky(new PushNotifyEvent());
        
    }
    
    public void removeAllNotifications() {
        SharedPreferences sharedPreferencesOrder = mContext.getSharedPreferences(Constans.APP_NOTIFICATON_SET_FOR_ORDER, Context.MODE_PRIVATE);
        Editor editorOrder = sharedPreferencesOrder.edit();
        editorOrder.clear();
        editorOrder.commit();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constans.APP_NOTIFICATON_SET, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    
}
