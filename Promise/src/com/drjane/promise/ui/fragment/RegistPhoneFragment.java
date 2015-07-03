package com.drjane.promise.ui.fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.drjane.promise.R;
import com.kerkr.edu.String.StrRegexUtil;
import com.kerkr.edu.app.BaseFragment;
import com.kerkr.edu.app.DrawerToast;
import com.umeng.socialize.media.GooglePlusShareContent;
@EFragment
public class RegistPhoneFragment extends BaseFragment {
    
    @ViewById(R.id.edit_phone)
    EditText mPhoneTv;
    
    @Override
    public int getLayoutResource() {
        // TODO Auto-generated method stub
        return R.layout.fragment_regist_phone;
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
    @Click(R.id.btn_next)
    public void nextClick(){
        String  phoneStr= new String(mPhoneTv.getText().toString());
        if(TextUtils.isEmpty(phoneStr)||! StrRegexUtil.checkPhone(phoneStr)){
            DrawerToast.getInstance().show("手机号输入错误");
            return;
        }
        
        RegistSmsFragment_ f = (RegistSmsFragment_) RegistSmsFragment_.builder()
                .mPhoneNum(phoneStr)
                .build();
        startFragment(f);
    }
    
}
