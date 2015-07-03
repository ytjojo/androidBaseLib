package com.drjane.promise.ui.fragment;

import android.content.Context;
import android.content.Intent;

import com.kerkr.edu.app.BaseActivity;
import com.kerkr.edu.app.BaseFragment;

public class BaseFragmentActivity extends BaseActivity {
    public final static String FRAGMENTNAME="args_ fragment_full_name";

    @Override
    public boolean isNeedSetUpFragment() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public Class<? extends BaseFragment> getRootFragmentClass() {
        // TODO Auto-generated method stub
        try {
            return (Class<? extends BaseFragment>) Class.forName(getIntent().getStringExtra(FRAGMENTNAME));
        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public static void startActivity(Context c,Intent intent,String fragmentFullName){
        intent.putExtra(FRAGMENTNAME,fragmentFullName);
        c.startActivity(intent);
    }
    
}
