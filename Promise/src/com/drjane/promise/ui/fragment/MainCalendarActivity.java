/*
 * 文 件 名:  MainCalendarActivity.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-3
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.ui.fragment;

import com.drjane.promise.R;
import com.kerkr.edu.app.BaseActivity;
import com.kerkr.edu.app.BaseFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author lijing
 * @version [版本号, 2015-6-3]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MainCalendarActivity extends BaseActivity {

	/**
	 * @param arg0
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setMiddleTitle("Promise");

	}

	/**
	 * @return
	 */
	@Override
	public boolean isNeedSetUpFragment() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Class<? extends BaseFragment> getRootFragmentClass() {
		return ViewpagerFragment.class;
	}

}
