package com.drjane.promise.ui.fragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.drjane.promise.R;
import com.kerkr.edu.app.BaseFragment;

@EFragment
public class LoginFragment extends BaseFragment {
    
    @ViewById(R.id.edit_phone)
    EditText mPhoneEditText;
    @ViewById(R.id.edit_password)
    EditText mPasswordEditText;
    @Override
    public int getLayoutResource() {
        // TODO Auto-generated method stub
        return R.layout.fragment_login;
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
