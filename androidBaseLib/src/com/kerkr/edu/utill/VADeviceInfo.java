package com.kerkr.edu.utill;

import java.util.UUID;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


public class VADeviceInfo {
    public static final long ID_BASE = 8761234;
    //获取设备 UUID
    public static String getNewUUID(Context currentContext) {
        final TelephonyManager tm = (TelephonyManager) currentContext.getSystemService (Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return getUUID();
        }
        String android_id = Secure.getString(currentContext.getContentResolver(), Secure.ANDROID_ID);
        String tmDevice =  tm.getDeviceId();

        if (TextUtils.isEmpty (android_id)) {
            return getUUID();
        }
        else if(TextUtils.isEmpty (tmDevice))
        {
            UUID id = new UUID(android_id.hashCode(),ID_BASE);
            return id.toString();
        }
            UUID id = new UUID(android_id.hashCode(),tmDevice.hashCode ());
            return id.toString();

    }
/**
*之前老的uuid生成
 */
    public static String getOldUUID(Context currentContext) {
        String android_id = Secure.getString(currentContext.getContentResolver(), Secure.ANDROID_ID);
        if (TextUtils.isEmpty (android_id)) {
            return getUUID();
        }
        UUID id = new UUID(android_id.hashCode(),ID_BASE);
        return id.toString();

    }

    //随机UUID；
    public static String getUUID() {
        UUID id = UUID.randomUUID();
        return id.toString();
    }
}
