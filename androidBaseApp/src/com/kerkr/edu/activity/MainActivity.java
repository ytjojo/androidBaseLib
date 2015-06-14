/*
 * 文 件 名:  MainActivity.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-2
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.kerkr.edu.activity;

import com.kerkr.edu.app.BaseActivity;
import com.kerkr.edu.app.BaseFragment;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-2]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MainActivity extends BaseActivity{

    /**
     * @return
     */
    @Override
    public boolean isNeedSetUpFragment() {
        // TODO Auto-generated method stub
        return false;
    }

	@Override
	public Class<? extends BaseFragment> getRootFragmentClass() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
