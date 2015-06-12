package com.kerkr.edu.kit;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.kerkr.edu.app.BaseApplication;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class UmengKit
{

    
    /**
     * 获取测试信息
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context)
    {
        try
        {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = tm.getDeviceId();
            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) device_id = mac;

            if (TextUtils.isEmpty(device_id))
            {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    
    /**
     * 进入反馈界面
     */
    public static void intoFeedBack(Context context){
        
        FeedbackAgent agent = new FeedbackAgent(context);
        agent.startFeedbackActivity();
    }
    
    
    /**
     * 自动更新  默认wifi 下
     * @param context
     */
    public static void autoUpdate( ){
        
        UmengUpdateAgent.setUpdateListener(null);
        UmengUpdateAgent.update(BaseApplication.getInstance());
        
    }

    
    /***
     * 手动 检测更新 无视 网络环境
     */
    public static void checkUpdate(final Context mContext){
        
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
                switch (updateStatus) {
                case UpdateStatus.Yes:
                    UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
                    break;
                case UpdateStatus.No: // has no update
                    Toast.makeText(mContext, "没有更新", Toast.LENGTH_SHORT).show();
                    break;
                case UpdateStatus.NoneWifi: // none wifi
                    Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                    break;
                case UpdateStatus.Timeout: // time out
                    Toast.makeText(mContext, "检测更新超时", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        });
        
        UmengUpdateAgent.forceUpdate(BaseApplication.getInstance());
        
        
    }
    
    
    /**
     * 静默下载 下载后 通知栏提示 
     * 
     * 默认 wifi 环境
     */
    public static void silentUpdate(){
        
        UmengUpdateAgent.silentUpdate(BaseApplication.getInstance());
    }
    
    
    
}
