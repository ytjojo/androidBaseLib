package com.kerkr.edu.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;


public class ActivityTack {
    
    private static Stack<Activity> activityStack;
    
    private static ActivityTack instance;
    
    private Context mAppContext;
    
    private ActivityTack() {
        
    }
    
    /**
     * 单一实例
     */
    public static ActivityTack getInstance() {
        
        if (instance == null) {
            synchronized (ActivityTack.class) {
                if (instance == null) {
                    instance = new ActivityTack();
                    if (activityStack == null) {
                        activityStack = new Stack<Activity>();
                    }
                }
            }
        }
        return instance;
    }
    
    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if(mAppContext ==null){
            mAppContext = activity.getApplicationContext();
        }
        activityStack.add(activity);
    }
    
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getCurrentActivity() {
        if (activityStack == null || activityStack.size() == 0) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }
    
    public Activity getActivityAtPosition() {
        if (activityStack.size() == 0) {
            return null;
        }
        Activity activity = activityStack.get(activityStack.size() - 1);
        return activity;
    }
    
    public Activity getActivityAtPosition(int index) {
        if (activityStack.size() == 0) {
            return null;
        }
        Activity activity = activityStack.get(activityStack.size() - index);
        return activity;
    }
    
    /**
     * 根据class name获取activity
     * @param name
     * @return
     */
    public Activity getActivityByClassName(String name) {
        for (Activity ac : activityStack) {
            if (ac.getClass().getName().indexOf(name) >= 0) {
                return ac;
            }
        }
        return null;
    }
    
    @SuppressWarnings("rawtypes")
    public Activity getActivityByClass(Class cs) {
        for (Activity ac : activityStack) {
            if (ac.getClass().equals(cs)) {
                return ac;
            }
        }
        return null;
    }
    
    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (activityStack.size() == 0) {
            return;
        }
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }
    
    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    
    /**
     * 延迟结束指定的Activity
     */
    public void delayFinishActivity(final Activity activity, int milisec) {
        if (activity != null) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    activityStack.remove(activity);
                    activity.finish();
                    
                }
            };
            timer.schedule(task, milisec);
        }
    }
    
    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }
    
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        
        Iterator<Activity> it = activityStack.iterator();
        while (it.hasNext()) {
            Activity activity = it.next();
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                return;
            }
        }
        
    }
    
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        Iterator<Activity> it = activityStack.iterator();
        while (it.hasNext()) {
            Activity activity = it.next();
            activity.finish();
            activity = null;
        }
        activityStack.clear();
    }
    
    //退出栈中所有Activity version2
    public synchronized void finishAllActivityNew() {
        while (true) {
            Activity activity = getCurrentActivity();
            if (activity == null) {
                break;
            }
            finishActivity(activity);
        }
    }
    
    public Activity getBaseActivityByName(String aname) {
        Activity base = null;
        for (Activity act : activityStack) {
            String name = act.getClass().getName();
            int index = name.indexOf(aname);
            if (index >= 0) {
                base = act;
                break;
            }
        }
        return base;
    }
    
    /**
     * 获取当前是否有打开的Activity，用于判断程序是否存在后台
     */
    public boolean isEmpty() {
        return activityStack.isEmpty();
    }
    
    /**
     * 退出应用程序
     *
     * @param isBackground 是否开开启后台运行
     */
    public void AppExit(Boolean isBackground) {
        try {
            finishAllActivityNew();
            if (ImageLoader.getInstance().isInited()) {
                ImageLoader.getInstance().stop();
            }
            //        android.os.Process.killProcess(android.os.Process.myPid());
        }
        catch (Exception e) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        finally {
            // 注意，如果您有后台程序运行，请不要支持此句子
            if (!isBackground) {
                MobclickAgent.onKillProcess(mAppContext);
                System.exit(0);
            }
        }
    }
    
    /**
     * 弹出activity到
     * @param cs
     */
    @SuppressWarnings("rawtypes")
    public void popUntilActivity(Class... cs) {
        ArrayList<Activity> list = new ArrayList<Activity>();
        int size = activityStack.size();
        for (int i = size - 1; i >= 0; i--) {
            Activity ac = activityStack.get(i);
            boolean isTop = false;
            for (int j = 0; j < cs.length; j++) {
                if (ac.getClass().equals(cs[j])) {
                    isTop = true;
                    break;
                }
            }
            if (!isTop) {
                list.add(ac);
            }
            else
                break;
        }
        for (Iterator<Activity> iterator = list.iterator(); iterator.hasNext();) {
            Activity activity = iterator.next();
            finishActivity(activity);
        }
    }
}
