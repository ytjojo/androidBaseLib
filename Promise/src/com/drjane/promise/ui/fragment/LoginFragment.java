package com.drjane.promise.ui.fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.drjane.promise.R;
import com.kerkr.edu.app.BaseFragment;

@EFragment
public class LoginFragment extends BaseFragment{
    
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

   @Click(R.id.btn_login)
   public void loginClick(){
       onLoginSucc();
   }
   @Click(R.id.tv_forget_pass)
   public void onForgetClick(){
       
   }
   
   @Click(R.id.tv_sms_login)
   public void onSmsLogin(){
       startFragment(new RegistPhoneFragment_());
   }
   
   public void onLoginSucc(){
       finish();
       Intent intent = new Intent(mActivity, DrawerActivity_.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
       startActivity(intent);
   }

    
}
