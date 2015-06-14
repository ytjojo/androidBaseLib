package com.kerkr.edu.app;

import java.io.File;
import java.util.Map.Entry;

import com.kerkr.edu.receiver.AlarmReceiver;
import com.kerkr.edu.receiver.DeleteNotificationsReceiver;

import android.R.integer;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification.Style;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

public class Notifier {
    
    private static final int NOTIFICATION_ID_1 = 0;
    
    private static final int NOTIFICATION_ID_2 = 1;
    
    private static final int NOTIFICATION_ID_3 = 2;
    
    private static final int NOTIFICATION_ID_4 = 3;
    
    private static final int NOTIFICATION_ID_5 = 4;
    
    private static final int NOTIFICATION_ID_6 = 5;
    
    private static final int NOTIFICATION_ID_7 = 6;
    
    private static final int NOTIFICATION_ID_8 = 7;
    
    public static final String ACCOUNT_INDICES = "account_indices_key_v2";
    
    public static final String NOTIFICATION_LAST_ACTIONED_MENTION_ID = "notification_last_actioned_mention_id_v1_";
    
    public static final String NOTIFICATION_LAST_DISPLAYED_MENTION_ID = "notification_last_displayed_mention_id_v1_";
    
    public static final String NOTIFICATION_COUNT = "notification_count_";
    
    public static final String NOTIFICATION_SUMMARY = "notification_summary_";
    
    public static final String VERSION = "prefs_version";
    
    public static final String CURRENT_ACCOUNT_ID = "current_account_id_key_v2";
    
    public static final String TUTORIAL_COMPLETED = "tutorial_completed_v2";
    
    public static final String NOTIFICATION_LAST_ACTIONED_DIRECT_MESSAGE_ID = "notification_last_actioned_direct_message_id_v1_";
    
    public static final String NOTIFICATION_LAST_DISPLAYED_DIRECT_MESSAGE_ID = "notification_last_displayed_direct_message_id_v1_";
    
    public static final String NOTIFICATION_TYPE_MENTION = "_mention";
    
    public static final String NOTIFICATION_TYPE_DIRECT_MESSAGE = "_directmessage";
    
    private static long mNotificationTime = 0;
    
    public static class BigInboxStyleBuilder {
        //        private Context mContext;
        NotificationCompat.InboxStyle mInboxStyle;
        
        //        NotificationCompat.Builder mBuilder;
        public boolean isVilide() {
            if (Build.VERSION.SDK_INT >= 16) {
                return true;
            }
            return false;
        }
        
        public BigInboxStyleBuilder init() {
            //            mContext =c;
            //            mBuilder = new Builder(c);
            mInboxStyle = new NotificationCompat.InboxStyle();
            return this;
        }
        
        public BigInboxStyleBuilder setBigContentTitle(String title) {
            mInboxStyle.setBigContentTitle(title);
            
            return this;
        }
        
        public BigInboxStyleBuilder setSummaryText(String title) {
            mInboxStyle.setSummaryText(title);
            return this;
        }
        
        public BigInboxStyleBuilder addLine(String line) {
            mInboxStyle.addLine(line);
            
            return this;
        }
        
        public NotificationCompat.InboxStyle create() {
            return mInboxStyle;
        }
        
    }
    
    public static class BigPictureStyleBuilder {
        //        private Context mContext;
        NotificationCompat.BigPictureStyle mInboxStyle;
        
        //        NotificationCompat.Builder mBuilder;
        public boolean isVilide() {
            if (Build.VERSION.SDK_INT >= 16) {
                return true;
            }
            return false;
        }
        
        public BigPictureStyleBuilder init() {
            //            mContext =c;
            //            mBuilder = new Builder(c);
            mInboxStyle = new NotificationCompat.BigPictureStyle();
            return this;
        }
        
        public BigPictureStyleBuilder setBigContentTitle(String title) {
            mInboxStyle.setBigContentTitle(title);
            
            return this;
        }
        
        public BigPictureStyleBuilder setSummaryText(String title) {
            mInboxStyle.setSummaryText(title);
            return this;
        }
        
        public BigPictureStyleBuilder bigLargeIcon(Bitmap b) {
            mInboxStyle.bigLargeIcon(b);
            
            return this;
        }
        
        public BigPictureStyleBuilder bigPicture(Bitmap b) {
            mInboxStyle.bigPicture(b);
            
            return this;
        }
        
        public NotificationCompat.BigPictureStyle create() {
            return mInboxStyle;
        }
        
    }
    
    public static class BigTextStyleBuilder {
        //        private Context mContext;
        NotificationCompat.BigTextStyle mInboxStyle;
        
        //        NotificationCompat.Builder mBuilder;
        public boolean isVilide() {
            if (Build.VERSION.SDK_INT >= 16) {
                return true;
            }
            return false;
        }
        
        public BigTextStyleBuilder init() {
            //            mContext =c;
            //            mBuilder = new Builder(c);
            mInboxStyle = new NotificationCompat.BigTextStyle();
            return this;
        }
        
        public BigTextStyleBuilder setBigContentTitle(String title) {
            mInboxStyle.setBigContentTitle(title);
            
            return this;
        }
        
        public BigTextStyleBuilder setSummaryText(String title) {
            mInboxStyle.setSummaryText(title);
            return this;
        }
        
        public BigTextStyleBuilder bigText(String t) {
            mInboxStyle.bigText(t);
            
            return this;
        }
        
        public NotificationCompat.BigTextStyle create() {
            return mInboxStyle;
        }
        
    }
    
    /**
     * <activity
            android:name="com.example.notification.OtherActivity"
             android:label="@string/title_activity_other"
             android:parentActivityName=".MainActivity" >
             <meta-data
                 android:name="android.support.PARENT_ACTIVITY"
                 android:value=".MainActivity" />
         </activity>
     * <一句话功能简述>
     * <功能详细描述>
     * 
     * @author  lijing
     * @version  [版本号, 2015-5-14]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    public static class NotifyBuilder {
        
        private RemoteViews mRemoteViews;
        
        private Context mContext;
        
        NotificationCompat.Builder mBuilder;
        
        private String tiger;
        
        Class<Activity> mMainActivityClass;
        
        boolean isBigContentView;
        
        private int smallIconDrawable;
        
        private int notificationFlag = Notification.FLAG_ONGOING_EVENT;//Notification.FLAG_ONGOING_EVENT;//FLAG_ONGOING_EVENT 在顶部常驻，
        
        public NotifyBuilder init(Context c, int layoutId) {
            mBuilder = new Builder(c);
            mRemoteViews = new RemoteViews(c.getPackageName(), layoutId);
            return this;
        }
        
        public NotifyBuilder setMainActivityClass(Class<Activity> clazz) {
            mMainActivityClass = clazz;
            return this;
        }
        
        public NotifyBuilder setDrawable(int imgId, int drawableId) {
            
            mRemoteViews.setImageViewResource(imgId, drawableId);
            return this;
        }
        
        public NotifyBuilder addBackIntent(Intent resultIntent) {
            if (mMainActivityClass == null) {
                throw new NullPointerException("mainActivityClass can't be null");
            }
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
            stackBuilder.addParentStack(mMainActivityClass);
            stackBuilder.addNextIntent(resultIntent);
            int requestCode = (int) (Math.random() * Integer.MAX_VALUE);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            return this;
        }
        
        public NotifyBuilder addAction(int icon, String title, Intent intent) {
            int requestCode = (int) (Math.random() * Integer.MAX_VALUE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.addAction(icon, title, pendingIntent);
            return this;
        }
        
        public NotifyBuilder setText(int tvId, String content) {
            
            mRemoteViews.setTextViewText(tvId, content);
            return this;
        }
        
        public NotifyBuilder setStyle(android.support.v4.app.NotificationCompat.Style style) {
            
            mBuilder.setStyle(style);
            return this;
        }
        
        public NotifyBuilder setBigContentView(boolean isBigContentView) {
            this.isBigContentView = isBigContentView;
            return this;
        }
        
        public NotifyBuilder setFullScreenIntent(Intent intent, boolean highPriority) {
            int requestCode = (int) (Math.random() * Integer.MAX_VALUE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setFullScreenIntent(pendingIntent, highPriority);
            return this;
        }
        
        public NotifyBuilder setClickBtn(int id, Intent intent) {
            
            //如果版本号低于（3。0），那么不显示按钮  
            if (!CompatUtil.hasHoneycomb()) {
                mRemoteViews.setViewVisibility(id, View.GONE);
            }
            else {
                mRemoteViews.setViewVisibility(id, View.VISIBLE);
            }
            
            int requestCode = (int) (Math.random() * Integer.MAX_VALUE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mRemoteViews.setOnClickPendingIntent(id, pendingIntent);
            return this;
        }
        
        public NotifyBuilder setTicker(String tiger) {
            
            this.tiger = tiger;
            return this;
        }
        
        public NotifyBuilder setAutoCancel(boolean autoCancel) {
            
            mBuilder.setAutoCancel(autoCancel);
            return this;
        }
        
        public NotificationCompat.Builder getInnerBuilder() {
            return mBuilder;
        }
        
        public NotifyBuilder setOnlyAlertOnce(boolean onlyAlertOnce) {
            
            mBuilder.setOnlyAlertOnce(onlyAlertOnce);
            return this;
        }
        
        public NotifyBuilder setNotificationFlag(int flag) {
            
            this.notificationFlag = flag;
            return this;
        }
        
        public NotifyBuilder setSmallIcon(int drawable) {
            
            this.smallIconDrawable = drawable;
            return this;
        }
        
        public NotifyBuilder setDeleteReciever(String accountKey, String type, long postId) {
            Intent deleteIntent = new Intent(mContext, DeleteNotificationsReceiver.class);
            deleteIntent.putExtra("account_key", accountKey);
            deleteIntent.putExtra("notification_post_id", postId);
            deleteIntent.putExtra("notification_type", type);
            int requestCode = (int) (Math.random() * Integer.MAX_VALUE);
            PendingIntent deletePendingIntent = PendingIntent.getBroadcast(mContext, requestCode, deleteIntent, 0);
            
            mBuilder.setDeleteIntent(deletePendingIntent);
            return this;
        }
        
        public NotifyBuilder create(int notifyId) {
            
            mBuilder.setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT)).setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示  
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    // 设置该通知优先级  
                    .setOngoing(true);
            if (smallIconDrawable != 0) {
                mBuilder.setSmallIcon(smallIconDrawable);
            }
            if (!TextUtils.isEmpty(tiger)) {
                mBuilder.setTicker(tiger);
            }
            if (!isBigContentView || !CompatUtil.hasJellyBean()) {
                mBuilder.setContent(mRemoteViews);
            }
            Notification notify = mBuilder.build();
            if (isBigContentView && CompatUtil.hasJellyBean()) {
                notify.bigContentView = mRemoteViews;
            }
            notify.flags = notificationFlag;
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notifyId, notify);
            return this;
        }
        
        public PendingIntent getDefalutIntent(int flags) {
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, new Intent(), flags);
            return pendingIntent;
        }
    }
    
    public static void notify(int iconId, Class<Activity> mainClazz, String title, String text, String bigText, Boolean autoCancel, int id,
            String accountKey, String type, long postId, Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setTicker(text)
                .setSmallIcon(iconId)
                .setAutoCancel(autoCancel)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText));
        
        if (AppSettings.get().isNotificationVibrationEnabled()) {
            long[] pattern = { 200, 500, 200 };
            builder.setVibrate(pattern);
        }
        
        Uri ringtone = AppSettings.get().getRingtoneUri();
        if (ringtone != null) {
            builder.setSound(ringtone);
        }
        
        Intent resultIntent = new Intent(context, mainClazz);
        resultIntent.putExtra("account_key", accountKey);
        resultIntent.putExtra("notification_type", type);
        resultIntent.putExtra("notification_post_id", postId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(mainClazz);
        stackBuilder.addNextIntent(resultIntent);
        int requestCode = (int) (Math.random() * Integer.MAX_VALUE);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
        
        builder.setContentIntent(resultPendingIntent);
        
        Intent deleteIntent = new Intent(context, DeleteNotificationsReceiver.class);
        deleteIntent.putExtra("account_key", accountKey);
        deleteIntent.putExtra("notification_post_id", postId);
        deleteIntent.putExtra("notification_type", type);
        requestCode = (int) (Math.random() * Integer.MAX_VALUE);
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context, requestCode, deleteIntent, 0);
        
        builder.setDeleteIntent(deletePendingIntent);
        
        saveLastNotificationDisplayed(context, accountKey, type, postId);
        
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
        
    }
    
    public static void cancel(Context context, String accountKey, String type) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel((accountKey + type).hashCode());
        
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(NOTIFICATION_COUNT + accountKey + type, 0);
        edit.putString(NOTIFICATION_SUMMARY + accountKey + type, "");
        edit.commit();
        
        Notifier.setDashclockValues(context, accountKey, type, 0, "");
    }
    
    public static void setNotificationAlarm(Context context) {
        if (AppSettings.get().isShowNotificationsEnabled()) {
            long mNewNotificationTime = AppSettings.get().getNotificationTime();
            if (mNotificationTime != mNewNotificationTime) {
                if (mNotificationTime > 0) {
                    cancelNotificationAlarm(context);
                }
                mNotificationTime = mNewNotificationTime;
                setupNotificationAlarm(context);
            }
        }
        else {
            cancelNotificationAlarm(context);
        }
    }
    
    private static void setupNotificationAlarm(Context context) {
        //Create a new PendingIntent and add it to the AlarmManager
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), mNotificationTime, pendingIntent);
    }
    
    private static void cancelNotificationAlarm(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.cancel(pendingIntent);
    }
    
    public static void saveLastNotificationActioned(Context context, String accountKey, String type, long postId) {
        
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String pref = type.equals(NOTIFICATION_TYPE_MENTION) ? NOTIFICATION_LAST_ACTIONED_MENTION_ID : NOTIFICATION_LAST_ACTIONED_DIRECT_MESSAGE_ID;
        long lastDisplayedMentionId = preferences.getLong(pref + accountKey, 0);
        
        if (postId > lastDisplayedMentionId) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putLong(pref + accountKey, postId);
            edit.commit();
            
            saveLastNotificationDisplayed(context, accountKey, type, postId);
            Notifier.setDashclockValues(context, accountKey, type, 0, "");
        }
    }
    
    private static void saveLastNotificationDisplayed(Context context, String accountKey, String type, long postId) {
        
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String pref = type.equals(NOTIFICATION_TYPE_MENTION) ? NOTIFICATION_LAST_DISPLAYED_MENTION_ID : NOTIFICATION_LAST_DISPLAYED_DIRECT_MESSAGE_ID;
        long lastDisplayedMentionId = preferences.getLong(pref + accountKey, 0);
        
        if (postId > lastDisplayedMentionId) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putLong(pref + accountKey, postId);
            edit.commit();
        }
    }
    
    public static void setDashclockValues(Context context, String accountKey, String type, int count, String detail) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(NOTIFICATION_COUNT + accountKey + type, count);
        edit.putString(NOTIFICATION_SUMMARY + accountKey + type, detail);
        edit.commit();
    }
    
    public static Intent getApkIntent(String path) {
        Intent apkIntent = new Intent();
        apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        apkIntent.setAction(android.content.Intent.ACTION_VIEW);
        //注意：这里的这个APK是放在assets文件夹下，获取路径不能直接读取的，要通过COYP出去在读或者直接读取自己本地的PATH，这边只是做一个跳转APK，实际打不开的
        String apk_path = "file:///android_asset/cs.apk";
        //      Uri uri = Uri.parse(apk_path);
        Uri uri = Uri.fromFile(new File(path));
        apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        return apkIntent;
    }
    
    /**
     * 关闭通知栏
     * <功能详细描述>
     * @param c [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void closeNotify(Context c) {
        c.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }
}
