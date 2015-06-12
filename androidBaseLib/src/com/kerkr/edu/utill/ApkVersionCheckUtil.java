package com.kerkr.edu.utill;

import java.io.File;

import com.kerkr.edu.log.VALog;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Hyr on 2014/12/4.
 * Desc:
 */
public class ApkVersionCheckUtil {
    /**
     * 获取手机上apk文件信息类，主要是判断是否安装再手机上了，安装的版本比较现有apk版本信息
     * <a href="http://my.oschina.net/arthor" target="_blank" rel="nofollow">@author</a>  Dylan
     */
    private static int INSTALLED = 0; // 表示已经安装，且跟现在这个apk文件是一个版本
    
    private static int UNINSTALLED = 1; // 表示未安装
    
    private static int INSTALLED_UPDATE = 2; // 表示已经安装，版本比现在这个版本要低，可以点击按钮更新
    
    private Context context;
    
    public ApkVersionCheckUtil(Context context) {
        super();
        this.context = context;
    }
    
    public String getApkVersionName(File file) {
        String apkVersionName = null;
        // SD卡上的文件目录
        if (file != null && file.isFile()) {
            String name_s = file.getName();
            String apk_path = null;
            if (name_s.toLowerCase().endsWith(".apk")) {
                apk_path = file.getAbsolutePath();// apk文件的绝对路劲
                try {
                    
                    PackageManager pm = context.getPackageManager();
                    PackageInfo packageInfo = pm.getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);
                    //                ApplicationInfo appInfo = packageInfo.applicationInfo;
                    //                /**获取apk的图标 */
                    //                appInfo.sourceDir = apk_path;
                    //                appInfo.publicSourceDir = apk_path;
                    //                /** 得到包名 */
                    //                String packageName = packageInfo.packageName;
                    /** apk的版本名称 String */
                    apkVersionName = packageInfo.versionName;
                    //                /** apk的版本号码 int */
                    //                int versionCode = packageInfo.versionCode;
                    
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return apkVersionName;
    }
    
    /*检查已存在的apk文件是否为最新版本*/
    public boolean isApkFileLatestVersion(String latestVersionName, File apkFile) {
        String fileVersionName = getApkVersionName(apkFile);
        VALog.e(" 版本比较" + latestVersionName + "   " + fileVersionName);
        if (fileVersionName != null && latestVersionName.equals(fileVersionName)) {
            return true;
        }
        return false;
    }
    
}
