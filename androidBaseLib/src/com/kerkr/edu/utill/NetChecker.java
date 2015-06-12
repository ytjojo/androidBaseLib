package com.kerkr.edu.utill;

import com.kerkr.edu.log.VALog;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class NetChecker {
    public static final String DEFAULT_WIFI_ADDRESS = "00-00-00-00-00-00";

 
    public static final int NETWORN_NONE = 0;
    
    public static final int NETWORN_WIFI = 1;
    
    public static final int NETWORN_MOBILE = 2;
    
    private static String convertIntToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "."
                + (0xFF & paramInt >> 16) + "." + (0xFF & paramInt >> 24);
    }

    
    
    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        //Wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            State state = wifiInfo.getState();
            if (state == State.CONNECTED || state == State.CONNECTING) {
                return NETWORN_WIFI;
            }
        }
        
        //3G
        NetworkInfo mobileiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != mobileiInfo) {
            State state = mobileiInfo.getState();
            if (state == State.CONNECTED || state == State.CONNECTING) {
                return NETWORN_MOBILE;
            }
        }
        
        return NETWORN_NONE;
    }
    
    public static boolean isConnected(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        if (context == null) {
            return false;
        }
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    /***
     *获取wifi 地址
     * 
     * @param pContext
     * @return
     */

    public static String getWifiAddress(Context pContext) {
        String address = DEFAULT_WIFI_ADDRESS;
        if (pContext != null) {
            WifiInfo localWifiInfo = ((WifiManager) pContext
                    .getSystemService("wifi")).getConnectionInfo();
            if (localWifiInfo != null) {
                address = localWifiInfo.getMacAddress();
                if (address == null || address.trim().equals(""))
                    address = DEFAULT_WIFI_ADDRESS;
                return address;
            }

        }
        return DEFAULT_WIFI_ADDRESS;
    }

    /***
     *获取wifi ip地址
     * 
     * @param pContext
     * @return
     */
    public static String getWifiIpAddress(Context pContext) {
        WifiInfo localWifiInfo = null;
        if (pContext != null) {
            localWifiInfo = ((WifiManager) pContext.getSystemService("wifi"))
                    .getConnectionInfo();
            if (localWifiInfo != null) {
                String str = convertIntToIp(localWifiInfo.getIpAddress());
                return str;
            }
        }
        return "";
    }
    

    /**
     * 方法描述： 判断手机是否开启GPS </br> 创 建 人： YAR</br> 创建时间：2015-3-3</br>
     * 
     * @param Context
     *            </br>
     * @return true表示开启
     * */
    public static boolean isGPS(Context context) {
        // 通过GPS卫星定位，定位级别可以精确到街
        android.location.LocationManager locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
//      boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 方法描述： 打开系统网络设置界面 </br> 创 建 人： </br> 创建时间： </br>
     * */
    public static boolean openSttingForWlan(Context context) {
        Intent intent = null;
        // 判断手机系统的版本 即API大于10 就是3.0或以上版本
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);

        return true;
    }

    /**
     * 方法描述： 打开系统GPS设置界面 </br> 创 建 人： </br> 创建时间：2015-3-3 </br>
     * 返回类型：true或false</br>
     * 
     * @param Context
     *            </br>
     * */
    public static boolean openSttingForGPS(Context context) {
        boolean flagSetting = false;
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);

        } catch (ActivityNotFoundException ex) {

            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                context.startActivity(intent);
                flagSetting = true;
            } catch (Exception e) {
                flagSetting = false;
            }
        }
        return flagSetting;
    }

    /**
     * 方法描述： 尝试帮用户直接开启GPS </br> 创 建 人： </br> 创建时间： </br>
     * 
     * @param context
     *            </br>
     * */
    @SuppressWarnings("deprecation")
    public static void openGPS(Context context) {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        context.sendBroadcast(intent);
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { // if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }

    }
    
    /**
     * 判断网络是否为漫游
     */
    public static boolean isNetworkRoaming(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
           VALog.i( "couldn't get connectivity manager");
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null
                    && info.getType() == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null && tm.isNetworkRoaming()) {
                    VALog.i(  "network is roaming");
                    return true;
                } else {
                    VALog.i(  "network is not roaming");
                }
            } else {
                VALog.i(  "not using mobile network");
            }
        }
        return false;
    }

}
