/*
 * 文 件 名:  AppContext.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-3
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.app;

import com.drjane.promise.R;
import com.kerkr.edu.app.BaseApplication;
import com.kerkr.edu.cache.CacheManager;
import com.kerkr.edu.http.HttpConfig;
import com.kerkr.edu.kit.HttpCache;
import com.kerkr.edu.log.VALog;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-3]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AppContext extends BaseApplication {
    
    /**
     * 
     */
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mAppName = getResources().getString(R.string.app_name);
        VALog.setTag(mAppName);
        CacheManager.getInstance().reload();
    }
    
    @Override
    public void initHttpConfig() {
        HttpConfig.BASE_SERVICE_URL = "";
        HttpConfig.BASE_TEST_URL = "";
        HttpConfig.DEBUG = true;
    }
}
