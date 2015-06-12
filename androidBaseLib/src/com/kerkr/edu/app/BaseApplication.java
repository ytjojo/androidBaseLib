/*
 * 文 件 名:  BaseApplication.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-5-11
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.kerkr.edu.app;

import java.lang.reflect.Field;

import com.baidu.mapapi.SDKInitializer;
import com.kerkr.edu.http.HttpService;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.ViewConfiguration;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-5-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class BaseApplication extends Application{
    
    public static String mAppName;
    
//    public static int mNetWorkState = NetChecker.NETWORN_NONE;
    
    //此路径只保存新版本APK文件，下载新版本时会被清空
    //    public final static String mDownloadPath = VAConst.DOWNLOAD_CACHE;
    public static int mVersionCode;
    
    public static String mVersionName;
    
    public static boolean mShowUpdate = true;
    
    private static BaseApplication mInstance = null;
    
    public static boolean isActive ;
    
    /**
     * 
     */
    @Override
    public void onCreate() {
        mInstance =this;
        SDKInitializer.initialize(this);
        super.onCreate();
        // 崩溃反馈
        initImageLoader();
        HttpService.getHttpService();
        initLocalVersion();
        showMenuView();
        
    }
    
    public void showMenuView(){
        try {
            ViewConfiguration config = ViewConfiguration.get(this);         
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }
    public void initImageLoader() {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // .writeDebugLogs()
                // Remove for release app
                .memoryCacheSizePercentage(20)
                // .diskCacheSize(50 * 1024 * 1024)
                .threadPoolSize(3)
                .build();
        ImageLoader.getInstance().init(config);
    }
    public static BaseApplication getInstance() {
        return mInstance;
    }
    public void initLocalVersion() {
        PackageInfo pinfo;
        try {
            pinfo = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            mVersionCode = pinfo.versionCode;
            mVersionName = pinfo.versionName;
        }
        catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 重新启动应用 慎动
     */
    public void handleRestart() {
        ActivityTack.getInstance().AppExit(true);
//        Intent intent = new Intent(this, LaunchActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        System.exit(10);
    }
}
