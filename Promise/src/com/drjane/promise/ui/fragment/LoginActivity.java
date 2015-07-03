package com.drjane.promise.ui.fragment;

import com.drjane.promise.ui.fragment.LoginFragment_;
import com.kerkr.edu.app.BaseActivity;
import com.kerkr.edu.app.BaseFragment;

public class LoginActivity extends BaseActivity{

    @Override
    public boolean isNeedSetUpFragment() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public Class<? extends BaseFragment> getRootFragmentClass() {
        // TODO Auto-generated method stub
        return LoginFragment_.class;
    }
    
}
