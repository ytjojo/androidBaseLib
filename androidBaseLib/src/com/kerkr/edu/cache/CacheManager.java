/*
 * 文 件 名:  CacheManager.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-5-11
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.kerkr.edu.cache;

import com.kerkr.edu.String.JsonParser;
import com.kerkr.edu.app.BaseApplication;
import com.kerkr.edu.dto.CacheData;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-5-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CacheManager {
    public CacheData mCacheData = new CacheData();
    
    final static String APP_CACHE = "cache";
    
    private static CacheManager mInstance;
    
    public static CacheManager getInstance() {
        if (mInstance == null) {
            synchronized (CacheManager.class) {
                if (mInstance == null) {
                    mInstance = new CacheManager();
                }
            }
        }
        return mInstance;
    }
    
    public void save() {
        
        String cacheJson = JsonParser.getInstance().getJsonFromObject(mCacheData);
        if (TextUtils.isEmpty(cacheJson)) {
            return;
        }
        SharedPreferences pref = BaseApplication.getInstance().getSharedPreferences(APP_CACHE, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(APP_CACHE, cacheJson);
        editor.commit();
        
    }
    
    public void reload() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(APP_CACHE, Context.MODE_APPEND);
        String jsonString = sp.getString(APP_CACHE, "");
        if(TextUtils.isEmpty(jsonString)){
            return;
        }
        mCacheData = (CacheData) JsonParser.getInstance().getObjectFromJson(jsonString, CacheData.class);
    }
    
    public void clean() {
        
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(APP_CACHE, Context.MODE_APPEND);
        sp.edit().clear().commit();
    }
    
    public CacheData getCacheData() {
        reload();
        if (mCacheData == null) {
            mCacheData = new CacheData();
        }
        return mCacheData;
    }
    public boolean isLogin(){
        if(mCacheData != null && !TextUtils.isEmpty(mCacheData.getPhone())){
            return true;
        }
        return false;
    }
}
