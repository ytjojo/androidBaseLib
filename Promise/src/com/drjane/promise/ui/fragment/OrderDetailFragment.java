/*
 * 文 件 名:  OrderDetailFragment.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-10
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.ui.fragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.drjane.promise.R;
import com.drjane.promise.model.Order;
import com.kerkr.edu.app.BaseFragment;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@EFragment
public class OrderDetailFragment extends BaseFragment {
    
    @ViewById(R.id.tv_costmerName)
    TextView mNameTv;
    
    @ViewById(R.id.tv_phone)
    TextView mPhoneTv;
    
    @ViewById(R.id.tv_mmId)
    TextView mMMId;
    
    @ViewById(R.id.tv_shootingLocation)
    TextView mLocationTv;
    
    @ViewById(R.id.tv_shootingDate)
    TextView mDateTv;
    
    @ViewById(R.id.tv_orderPackege)
    TextView mOrderPackegeTv;
    
    @ViewById(R.id.tv_remarks)
    TextView mRemarksTv;
    
    @FragmentArg("order")
    Order mOrder;
    
    /**
     * @return
     */
    @Override
    public int getLayoutResource() {
        // TODO Auto-generated method stub
        return R.layout.fragment_orderdetail;
    }
    
    /**
     * 
     */
    @Override
    public void onUserVisble() {
        // TODO Auto-generated method stub
        
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
