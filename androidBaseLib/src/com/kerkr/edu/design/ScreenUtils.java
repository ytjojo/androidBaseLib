package com.kerkr.edu.design;

import com.kerkr.edu.app.BaseApplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

//获得屏幕相关的辅助类
public class ScreenUtils {
    private ScreenUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    /**
     * 获得屏幕高度
     * 
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
    
    /**
     * 获得屏幕宽度
     * 
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
    
    /**
     * 获得状态栏的高度
     * 
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
    
    /**
     * 获取当前屏幕截图，包含状态栏
     * 
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
        
    }
    
    /**
     * 获取当前屏幕截图，不包含状态栏
     * 
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
        
    }
    
    public static int getActionbarHeight() {
        TypedValue tv = new TypedValue();
        if (BaseApplication.getInstance().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, BaseApplication.getInstance().getResources().getDisplayMetrics());
        }
        
        //	    TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[] {  
        //	            android.R.attr.actionBarSize  
        //	    });  
        //	      
        //	    float h = actionbarSizeTypedArray.getDimension(0, 0); 
        return 0;
    }
    
    /**
     * Get the screen height.
     * 
     * @param context
     * @return the screen height
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Activity context) {
        
        Display display = context.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            return size.y;
        }
        return display.getHeight();
    }
    
    /**
     * Get the screen width.
     * 
     * @param context
     * @return the screen width
     */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Activity context) {
        
        Display display = context.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            return size.x;
        }
        return display.getWidth();
    }
}
