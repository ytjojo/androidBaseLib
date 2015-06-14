/*
 * 文 件 名:  CreateOrderFragment.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-8
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.ui.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.drjane.promise.R;
import com.kerkr.edu.app.BaseFragment;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-8]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@EFragment
public class CreateOrderFragment extends BaseFragment {
    
    @ViewById(R.id.tv_shootingDate)
    TextView mShootingDatTv;
    
    @ViewById(R.id.edit_customerName)
    EditText mCustomerNameEditText;
    
    @ViewById(R.id.edit_mmId)
    EditText mMMIdEditText;
    
    @ViewById(R.id.edit_orderPackege)
    EditText mOrderPackegeEditText;
    
    @ViewById(R.id.edit_phone)
    EditText mPhoneEditText;
    
    @ViewById(R.id.edit_remarks)
    EditText mRemarksEditText;
    
    @Click(R.id.layout_selectDate)
    void selectDate() {
        startFragment(new DatePickerFragment_());
    }
    
    @Click(R.id.layout_selectRemind)
    void selectRemindType() {
        
    }
    
    /**
     * @return
     */
    @Override
    public int getLayoutResource() {
        // TODO Auto-generated method stub
        return R.layout.fragment_createorder;
    }
    
    @AfterViews
    void afterViewInit() {
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
    }
    
    /**
     * 
     */
    @Override
    public void onUserVisble() {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_empty, menu);
        
    }
    
    /**
     * 
     */
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
