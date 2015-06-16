package com.drjane.promise.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.drjane.promise.R;
import com.kerkr.edu.app.ActivityTack;
import com.kerkr.edu.app.AppUtil;
import com.kerkr.edu.app.BaseActivity;
import com.kerkr.edu.cache.CacheManager;

public class LaunchActivity extends AppCompatActivity {
    
    public static final int delayMillis = 2200;
    
    private Handler mHandler = new Handler();
    
    private boolean isBackPressed;
    
    @Override
    protected void onCreate(@Nullable
    Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        CacheManager.getInstance().reload();
        mHandler.postDelayed(new Runnable() {
            
            @Override
            public void run() {
                if (isBackPressed) {
                  ActivityTack.getInstance().AppExit(false);
                    return;
                }
               intentDispatch();
            }
        }, delayMillis);
        
        
    }
    
    private void intentDispatch(){
       if(CacheManager.getInstance().isLogin()){
           intentMain();
       }else{
           intentLogin();
       }
        
    }
    private void intentMain(){
        startActivity(new Intent(LaunchActivity.this, MainCalendarActivity.class));
        overridePendingTransition(R.anim.ac_transition_fade_in, R.anim.ac_transition_fade_out);
        LaunchActivity.this.finish();
    }
    private void intentLogin(){
        startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.ac_transition_fade_in, R.anim.ac_transition_fade_out);
        LaunchActivity.this.finish();
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        isBackPressed = true;
    }
}
