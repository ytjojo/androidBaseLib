package com.drjane.promise.ui.fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.drjane.promise.R;
import com.kerkr.edu.app.BaseFragment;

@EFragment
public class RegistSmsFragment extends BaseFragment {
    
    @Click(R.id.btn_Regist_finish)
    public void onRegistFinish() {
        finish();
        Intent intent = new Intent(mActivity, DrawerActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(intent);
    }
    
    @FragmentArg
    public String mPhoneNum;
    
    @Override
    public int getLayoutResource() {
        // TODO Auto-generated method stub
        return R.layout.fragment_regist_sms;
    }
    
    @Override
    public void onUserVisble() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void setNavigations() {
        setNavigation(R.drawable.ic_action_hardware_keyboard_backspace, new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popBackFragment();
            }
        });
    }
    
}
