package com.drjane.promise.ui.fragment;

import com.kerkr.edu.app.BaseActivity;
import com.kerkr.edu.app.BaseFragment;

public class OrderDetailActivity extends BaseActivity {

    @Override
    public boolean isNeedSetUpFragment() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public Class<? extends BaseFragment> getRootFragmentClass() {
        // TODO Auto-generated method stub
        return OrderDetailFragment_.class;
    }
    
}
