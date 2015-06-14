/*
 * 文 件 名:  OrderListFragment.java
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
import java.util.Collections;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.baidu.platform.comapi.map.o;
import com.drjane.promise.R;
import com.drjane.promise.model.Order;
import com.kerkr.edu.adapter.AppBaseAdapter;
import com.kerkr.edu.adapter.AppBaseExpandAdapter;
import com.kerkr.edu.adapter.ViewHolder;
import com.kerkr.edu.adapterView.XExpandListView;
import com.kerkr.edu.adapterView.XExpandListView.IXListViewListener;
import com.kerkr.edu.app.BaseFragment;
import com.kerkr.edu.log.VALog;
import com.kerkr.edu.pinyin.PinyinWrapper;
import com.kerkr.edu.utill.CollectionUtils;
import com.kerkr.edu.utill.DrawerToast;

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
public class OrderListFragment extends BaseFragment {
    
    @ViewById(R.id.listView)
    XExpandListView mListView;
    
    AppBaseExpandAdapter<String, Order> mAdapter;
    
    private int mLastListPosition;
    
    private int mTop;
    
    List<List<Order>> mChildLists;
    
    /**
     * @return
     */
    @Override
    public int getLayoutResource() {
        // TODO Auto-generated method stub
        return R.layout.fragment_orderlist;
    }
    
    @AfterViews
    public void initView() {
    	VALog.i("createView");
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setOnChildClickListener(new OnChildClickListener() {
            
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                
                DrawerToast.getInstance().show("click");
                BaseFragment fragment = OrderDetailFragment_.builder().arg("order", mChildLists.get(groupPosition).get(childPosition)).build();
                startFragment(fragment);
                return false;
            }
        });
        mListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				return true;
			}
		});
        //点击不收缩
        mListView.setOnGroupClickListener(new OnGroupClickListener() {
            
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        mListView.setXListViewListener(new IXListViewListener() {
            
            @Override
            public void onRefresh() {
                
                requestListVolley();
                
            }
            
            @Override
            public void onLoadMore() {
                
            }
        });
        
       
    }
    
    private void requestListVolley() {
        // TODO Auto-generated method stub
        
    }
    
    private void onLoad() {// 显示拉出来时候的一些信息
        if (mListView != null) {
            mListView.stopRefresh();
            mListView.stopLoadMore();
            
        }
    }
    
    /**
     * 
     */
    @Override
    public void onUserVisble() {
        VALog.e("visiable ___________________________-");
        createDate();
        
    }
    
    /**
     * 
     */
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mListView != null) {
            
            mLastListPosition = mListView.getFirstVisiblePosition();
            View view = mListView.getChildAt(0);
            mTop = (view == null) ? 0 : view.getTop();
        }
    }
    
    private void createDate() {
        ArrayList<Order> mArrayList = new ArrayList<Order>();
        String[] arrStrings = { "中12", "呵呵", "你", "啦", "sdw", "和同", "啊哈", "北京", "哈哈", "烧地卧", "哈", "啦个" };
        
        for (int i = 0; i < 12; i++) {
            Order order = new Order();
            order.customerName = arrStrings[i] + i;
            order.shootingTime = "2015-01-05";
            order.mmId = "ytjojo";
            order.customerPhoneNumber = "186225544545";
            mArrayList.add(order);
            
        }
        setData(mArrayList);
    }
    
    private void setData(List<Order> list) {
        onLoad();
        if (!CollectionUtils.isValid(list)) {
            return;
        }
        PinyinWrapper<Order> wrapper = new PinyinWrapper<Order>();
        wrapper.sort(list);
        
        final List<String> mGroupList = wrapper.getGroup();
        mChildLists = wrapper.getChildLists();
        mAdapter = new AppBaseExpandAdapter<String, Order>(mActivity, R.layout.list_item_group, R.layout.list_item_child, mGroupList, mChildLists) {
            
            @Override
            public void bindGroupDataView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent, String groupItem) {
                
                TextView mLetterTv = ViewHolder.get(convertView, R.id.tv_letter);
                mLetterTv.setText(mGroupList.get(groupPosition));
                
            }
            
            @Override
            public void bindChildDataView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent,
                    Order childItem) {
                TextView nameTv = ViewHolder.get(convertView, R.id.tv_costmerName);
                TextView mDateTv = ViewHolder.get(convertView, R.id.tv_shootingDate);
                
                Order order = mChildLists.get(groupPosition).get(childPosition);
                nameTv.setText(order.customerName);
                mDateTv.setText(order.shootingTime);
                
            }
        };
        
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        
        //展开所有
        for (int i = 0; i < mGroupList.size(); i++) {
            mListView.expandGroup(i);
        }
        
        if (mLastListPosition > 0) {
            mListView.setSelectionFromTop(mLastListPosition, mTop);
            
        }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	VALog.i("create");
    }
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	VALog.i("destory");
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
    @Override
    public void onHiddenChanged(boolean hidden) {
    	// TODO Auto-generated method stub
    	super.onHiddenChanged(hidden);
    	VALog.i("hiddenchanged" + hidden);
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
