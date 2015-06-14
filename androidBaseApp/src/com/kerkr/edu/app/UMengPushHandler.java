package com.kerkr.edu.app;

import java.util.ArrayList;
import java.util.List;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.kerkr.edu.activity.MainActivity;
import com.kerkr.edu.app.Constans;
import com.kerkr.edu.app.ActivityTack;
import com.kerkr.edu.dto.UMengPush;
import com.kerkr.edu.log.VALog;
import com.kerkr.edu.manager.UMengPushManager;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.entity.UMessage;
import com.umeng.message.tag.TagManager;
import com.ytjojo.androidapplib.R;

public class UMengPushHandler {
    public static UMengPushHandler mInstance;
    private static String TAG = UMengPushHandler.class.getSimpleName();
    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
            // TODO Auto-generated method stub
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    VALog.i("push_______________________---regist");
                }
            });
        }
    };
    /**
     * 该Handler是在IntentService中被调用，故
     * 1. 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
     * 2. IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
     * 	      或者可以直接启动Service
     * */

    UmengMessageHandler messageHandler = new UmengMessageHandler() {
        @Override
        public void dealWithCustomMessage(final Context context, final UMessage msg) {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    UTrack.getInstance(mApplication).trackMsgClick(msg);
                    Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                }
            });
        }

        /**
         * 收到推送时，预处理
         * */
        @Override
        public Notification getNotification(Context context, UMessage msg) {

            UMengPushManager.getInstance(context).AddNewNotification(getPushFromUmessage(msg));

            switch (msg.builder_id) {
                case 1:
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                    myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                    myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                    myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                    myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                    builder.setContent(myNotificationView);
                    Notification mNotification = builder.build();
                    //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                    mNotification.contentView = myNotificationView;
                    return mNotification;
                default:
                    //默认为0，若填写的builder_id并不存在，也使用默认。
                    return super.getNotification(context, msg);
            }
        }
    };
    /**
     * 该Handler是在BroadcastReceiver中被调用，故
     * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
     * */
    UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

        @Override
        public void launchApp(Context context, UMessage msg) {
            //前台展示、后台活动、后台关闭、未启动
            if (AppUtil.isAppOnForeground(context)) {
                //在前台运行时，进行特殊处理
                UMengPush push = getPushFromUmessage(msg);
                MainActivity mainActivity = (MainActivity) ActivityTack.getInstance()
                        .getBaseActivityByName ("MainActivity");

                if (mainActivity != null) {
                    String notifyType = push.extra.get(Constans.NOTIFY_TYPE);
                    String value = push.extra.get(Constans.NOTIFY_VALUE);
                    returnToMainActivity(context);
                    mainActivity.getNotificationLIstener().onNotificationIntentReceived(notifyType, value, false);
                }

            }
            else if (!ActivityTack.getInstance().isEmpty()) {
                //在后台运行时，有activity可以跳转
                UMengPush push = getPushFromUmessage(msg);
                MainActivity mainActivity = (MainActivity) ActivityTack.getInstance()
                        .getBaseActivityByName ("MainActivity");
                if (mainActivity != null) {
                    String notifyType = push.extra.get(Constans.NOTIFY_TYPE);
                    String value = push.extra.get(Constans.NOTIFY_VALUE);
                    returnToMainActivity(context);
                    mainActivity.getNotificationLIstener().onNotificationIntentReceived(notifyType, value, false);
                    //                    Intent intent=new Intent (context,MainContentTabActivity.class);
                    //                    intent.putExtra (VAConst.NOTIFY_TYPE,notifyType);
                    //                    intent.putExtra (VAConst.NOTIFY_VALUE,value);
                    //                    intent.addFlags (Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    //                    context.startActivity (intent);

                }
            }
            else {
                super.launchApp(context, msg);
            }
        }

        @Override
        public void openActivity(Context context, UMessage msg) {
            //前台展示、后台活动、后台关闭、未启动
            if (AppUtil.isAppOnForeground(context)) {
                //在前台运行时，进行特殊处理
                UMengPush push = getPushFromUmessage(msg);
                MainActivity mainActivity = (MainActivity) ActivityTack.getInstance()
                        .getBaseActivityByName ("MainActivity");

                if (mainActivity != null) {
                    String notifyType = push.extra.get(Constans.NOTIFY_TYPE);
                    String value = push.extra.get(Constans.NOTIFY_VALUE);
                    returnToMainActivity(context);
                    mainActivity.getNotificationLIstener().onNotificationIntentReceived(notifyType, value, false);
                }

            }
            else if (!ActivityTack.getInstance().isEmpty()) {
                //在后台运行时，有activity可以跳转
                UMengPush push = getPushFromUmessage(msg);
                MainActivity mainActivity = (MainActivity) ActivityTack.getInstance()
                        .getBaseActivityByName ("MainActivity");
                if (mainActivity != null) {
                    String notifyType = push.extra.get(Constans.NOTIFY_TYPE);
                    String value = push.extra.get(Constans.NOTIFY_VALUE);
                    returnToMainActivity(context);
                    mainActivity.getNotificationLIstener().onNotificationIntentReceived(notifyType, value, false);
                }
            }
            else {
                super.openActivity(context, msg);
            }
        }
    };
    private PushAgent mPushAgent;
    private Context mApplication;
    private Handler mHandler;
    
    public UMengPushHandler(Context context) {
        mPushAgent = PushAgent.getInstance(context);

        mPushAgent.onAppStart();
        mPushAgent.enable(mRegisterCallback);
        mPushAgent.setDebugMode(true);
        mApplication = context;
        init();
        //      Toast.makeText (context,"DeviceToken"+mPushAgent.getRegistrationId(),Toast.LENGTH_LONG).show ();
        //        VALog.Log ("DeviceToken"+mPushAgent.getRegistrationId());
    }
    
    public static UMengPushHandler getInstance(Context context) {
        if (mInstance == null) {
            synchronized (UMengPushHandler.class) {
                if (mInstance == null) {
                    mInstance = new UMengPushHandler(context);
                }
            }
        }
        return mInstance;
    }
    
    private void init() {

        mHandler = new Handler(Looper.getMainLooper());

        mPushAgent.setMessageHandler(messageHandler);
        //        mPushAgent.setPushIntentServiceClass (PushIntentService.class);
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }
    
    // sample code to add tags for the device / user
    public void addTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        if (!mPushAgent.isRegistered()) {
            return;
        }
        
        new AddTagTask(tag).execute();
    }
    
    // sample code to add tags for the device / user
    private void listTags() {
        if (!mPushAgent.isRegistered()) {
            return;
        }
        new ListTagTask().execute();
    }
    
    // sample code to add alias for the device / user
    public void addAlias(String alias) {
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        if (!mPushAgent.isRegistered()) {
            return;
        }
        new AddAliasTask(alias).execute();
    }
    
    private void switchPush(IUmengRegisterCallback mRegisterCallback, IUmengUnregisterCallback mUnregisterCallback) {
        String info = String.format("enabled:%s  isRegistered:%s", mPushAgent.isEnabled(), mPushAgent.isRegistered());
        VALog.i("switch Push:" + info);
        
        if (mPushAgent.isEnabled() || UmengRegistrar.isRegistered(mApplication)) {
            mPushAgent.disable(mUnregisterCallback);
        }
        else {
            mPushAgent.enable(mRegisterCallback);
        }
    }
    
    //返回之前的activity
    private void returnToMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(intent);
    }
    
    private UMengPush getPushFromUmessage(UMessage message) {
        UMengPush push = null;
        if (message != null) {
            push = new UMengPush();
            push.title = message.title;
            push.text = message.text;
            push.extra = message.extra;
        }
        return push;

    }
    
    class AddTagTask extends AsyncTask<Void, Void, String> {

        String tagString;

        String[] tags;

        public AddTagTask(String tag) {
            // TODO Auto-generated constructor stub
            tagString = tag;
            tags = tagString.split(",");
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                TagManager.Result result = mPushAgent.getTagManager().add(tags);
                VALog.i(result.toString());
                return result.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
    
    class AddAliasTask extends AsyncTask<Void, Void, Boolean> {

        String alias;

        public AddAliasTask(String aliasString) {
            // TODO Auto-generated constructor stub
            this.alias = aliasString;
        }

        protected Boolean doInBackground(Void... params) {
            try {
                return mPushAgent.addAlias(alias, Constans.UM_PUSH_OFFICIAL_CHANNEL);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (Boolean.TRUE.equals(result))
                VALog.i("alias was set successfully.");
        }

    }
    
    class ListTagTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> tags = new ArrayList<String>();
            try {
                tags = mPushAgent.getTagManager().list();
                VALog.i(String.format("list tags: %s", TextUtils.join(",", tags)));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return tags;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            StringBuilder info = new StringBuilder();
            info.append("Tags:\n");
            for (int i = 0; i < result.size(); i++) {
                String tag = result.get(i);
                info.append(tag + "\n");
            }
            info.append("\n");
            Toast.makeText(mApplication, info.toString(), Toast.LENGTH_LONG).show();
        }
    }
    
}
