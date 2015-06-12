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

import com.kerkr.edu.app.BaseApplication;
import com.kerkr.edu.consts.SharepreferConsts;
import com.kerkr.edu.dto.CacheData;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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
    
    
    private static CacheManager mInstance;
    
    public static CacheManager getInstance(){
        if(mInstance == null){
            synchronized (CacheManager.class) {
                if(mInstance == null){
                    mInstance = new CacheManager();
                }
            }
        }
        return mInstance;
    }
    
    public void save(){
        SharedPreferences pref = BaseApplication.getInstance().getSharedPreferences(SharepreferConsts.APP_LOCAL_SET, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putInt("cityID", mCacheData.getCityId());
        editor.putString("lastLatitude", mCacheData.getLatitude() + "");
        editor.putString("lastLongitude", mCacheData.getLongitude() + "");
        editor.commit();
      
    }
    
    public void reload(){
        
    }
    public void clean(){
        
        
    }
    public CacheData getCacheData(){
       return  mCacheData;
    }
}
