/*
 * 文 件 名:  DayOrderListFragment.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-10
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.drjane.promise.R;
import com.drjane.promise.model.DayOrderList;
import com.drjane.promise.model.Order;
import com.kerkr.edu.adapter.AppBaseAdapter;
import com.kerkr.edu.adapter.ViewHolder;
import com.kerkr.edu.app.BaseFragment;
import com.kerkr.edu.utill.CollectionUtils;
import com.kerkr.edu.widget.viewpager.BaseRecylingPagerAdapter;
import com.kerkr.edu.widget.viewpager.RecyclingPagerAdapter;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DayOrderListFragment extends BaseFragment {
    private ViewPager mViewPager;
    
    private BaseRecylingPagerAdapter<DayOrderList> mAdapter;
    
    private List<DayOrderList> mList;
    
    private int mInitPositon= 3;
    
    /**
     * @return
     */
    @Override
    public int getLayoutResource() {
        // TODO Auto-generated method stub
        return R.layout.fragment_dayoderlist;
    }
    
    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable
    Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        
        mList = new ArrayList<DayOrderList>();
        for (int i = 0; i < 5; i++) {
            DayOrderList mDayOrderList = new DayOrderList();
            mDayOrderList.dateString = "2015-06-07"+i;
            mDayOrderList.mList = new ArrayList<Order>();
            for (int j = 0; j < 10; j++) {
                Order mOrder = new Order();
                mOrder.customerName = "三菱电机哦我";
                mDayOrderList.mList.add(mOrder);
            }
            mList.add(mDayOrderList);
          
        }
        mAdapter = new BaseRecylingPagerAdapter<DayOrderList>(mActivity, R.layout.viewpager_item_dayorderlist, mList) {
            
            @Override
            public View bindViewData(int position, View convertView, ViewGroup parent) {
                
                //                ListView listView = ViewHolder.get(convertView, R.id.listview);
                ListView listView = (ListView) convertView;
                final List<Order> mOrders = mList.get(position).mList;
                setUpListView(listView, mOrders);
                return convertView;
            }
            
        };
        mViewPager = (ViewPager) mContentView;
        mViewPager.setOffscreenPageLimit(5);
        
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            
            @Override
            public void onPageSelected(int arg0) {
                setTitle(mList.get(arg0).dateString);
                
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                
            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mInitPositon);
        
    }
    
    
    public void setUpListView(ListView listView, final List<Order> mOrders) {
        listView.setAdapter(new AppBaseAdapter<Order>(mActivity, mOrders, R.layout.list_item_dayoder) {
            
            @Override
            public View bindViewData(int position1, View convertView1, ViewGroup parent) {
                
                TextView nameTv = ViewHolder.get(convertView1, R.id.tv_costmerName);
                TextView status = ViewHolder.get(convertView1, R.id.tv_orderStatus);
                Order order = mOrders.get(position1);
                nameTv.setText(order.customerName);
                status.setText("已付款");
                return convertView1;
            }
        });
    }
    
    /**
     * 
     */
    @Override
    public void onUserVisble() {
        
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
