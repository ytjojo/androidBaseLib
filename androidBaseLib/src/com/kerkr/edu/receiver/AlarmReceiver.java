package com.kerkr.edu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            mContext = context;
            checkForNewNotifications();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    private void checkForNewNotifications() {
       
    }

}
