/*
 * 文 件 名:  AppUtill.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-5-11
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.kerkr.edu.app;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.kerkr.edu.resource.ResourceHelper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-5-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AppUtil {
    private final static String TAG = AppUtil.class.getSimpleName();
    
    /**
     * 设置activity屏幕为垂直方向。
     * 
     * @param activity
     *            Activity对象。
     */
    public static void setScreenPortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 设置activity屏幕为水平方向
     * 
     * @param activity
     *            Activity对象。
     */
    public static void setScreenLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 返回当前屏幕是否为竖屏。
     * 
     * @param context
     * @return 当且仅当当前屏幕为竖屏时返回true,否则返回false。
     */
    public static boolean isOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
    /***
     *获取top的Activity的ComponentName
     * @param paramContext
     * @return
     */
    public static ComponentName getTopActivityCompomentName(Context paramContext) {
        List<ActivityManager.RunningTaskInfo> localList = null;
        if (paramContext != null) {
            ActivityManager localActivityManager = (ActivityManager) paramContext.getSystemService("activity");
            if (localActivityManager != null) {
                localList = localActivityManager.getRunningTasks(1);
                
                if ((localList == null) || (localList.size() <= 0)) {
                    return null;
                }
            }
        }
        ComponentName localComponentName = localList.get(0).topActivity;
        return localComponentName;
    }
    
    /**
     * 程序是否在前台运行
     * 
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        
        ActivityManager activityManager = (ActivityManager)BaseApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = BaseApplication.getInstance().getPackageName();
        
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        
        return false;
    }
    
    
    /***
     * 查看是否后台
     * @param paramContext
     * @return
     */
    public static boolean isAppRunningBackground(Context paramContext) {
        String pkgName = null;
        List<RunningAppProcessInfo> localList = null;
        if (paramContext != null) {
            pkgName = paramContext.getPackageName();
            ActivityManager localActivityManager = (ActivityManager) paramContext.getSystemService("activity");
            if (localActivityManager != null) {
                localList = localActivityManager.getRunningAppProcesses();
                if ((localList == null) || (localList.size() <= 0)) {
                    return false;
                }
            }
        }
        
        for (Iterator<RunningAppProcessInfo> localIterator = localList.iterator(); localIterator.hasNext();) {
            ActivityManager.RunningAppProcessInfo info = localIterator.next();
            if (info.processName.equals(pkgName) && info.importance != 100) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isAppOnForeground(Context activity) {
        
        String packageName = activity.getApplicationContext().getPackageName();
        
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        
        if (tasksInfo.size() > 0) {
            
            //应用程序位于堆栈的顶层    
            
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
            
        }
        return false;
    }
    
    public static boolean isTopActivity(Activity ac) {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) ac.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().contains(ac.getClass().getSimpleName())) {
            isTop = true;
        }
        return isTop;
    }
    
    public static void finish(Class mainClazz, Activity ac) {
        Intent mainIntent = new Intent(ac, mainClazz);
        ActivityManager am = (ActivityManager) ac.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> appTask = am.getRunningTasks(1);
        if (appTask.size() > 0 && appTask.get(0).baseActivity.equals(mainIntent.getComponent())) {
            ac.finish();
        }
        else {
            ac.startActivity(new Intent(ac, mainClazz));
            ac.finish();
        }
    }
    
    public static boolean isActivityInMainFest(String activityName) {
        Intent intent = new Intent();
        intent.setClassName(BaseApplication.getInstance().getPackageName(), activityName);
        if (intent.resolveActivity(BaseApplication.getInstance().getPackageManager()) == null) {
            return false;
        }
        return true;
    }
    
    /**
     * @Description 判断某一apk是否被安装到设备上
     * @param context
     * @param apkPackageName
     *            app包名
     * @return boolean
     * @throws
     */
    public static boolean appInstalled(Context context, String apkPackageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(apkPackageName, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断该APK是否正在运行
     * 
     * @param apkPackageName
     *            想要判断的应用包名
     * @return true 正在运行 false 未运行
     * 
     * */
    public static boolean appIsRun(Context context, String apkPackageName) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(apkPackageName) && info.baseActivity.getPackageName().equals(apkPackageName)) {
                return true;
            }
        }
        return false;
    }

    
    //去应用商店评分
    public static void score(Context c) {
        try {
            Uri uri = Uri.parse("market://details?id=" + c.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(intent);
            
        }
        catch (Exception e) {
            
        }
    }
    
    /** 
    * 实现文本复制功能 
    * add by wangqianzhou 
    * @param content 
    */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器  
        if (CompatUtil.hasHoneycomb()) {
            
            android.content.ClipboardManager cmb = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
        }
        else {
            android.text.ClipboardManager cmb = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
        }
    }
    

    /**
     * 判断是否横屏
     * 
     * @return true 横屏,false 竖屏
     */
    public static boolean isLand(Context context) {
        Configuration cf = context.getResources().getConfiguration();
        int ori = cf.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            return false;
        }
        return false;
    }

    /**
     * 打卡软键盘
     * 
     * @param mEditText输入框
     * @param mContext上下文
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     * 
     * @param mEditText输入框
     * @param mContext上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
    
    public static Drawable getIcon(Context context){
        if (context == null) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int icon = packageInfo.applicationInfo.icon;
            Drawable drawable  = ResourceHelper.getDrawable(icon);
            return drawable;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 安装指定文件路径的apk文件
     * @param path
     */
    public static void installApk(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(path)),
                "application/vnd.android.package-archive");
        context.startActivity(intent); // 安装新版本
    }
}
