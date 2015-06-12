package com.kerkr.edu.utill;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

public class CompatUtil {
    @SuppressWarnings("deprecation")
    public static void setBackground(View v, Drawable drawable) {
        if (hasJellyBean()) {
            v.setBackground(drawable);
        }
        else {
            v.setBackgroundDrawable(drawable);
        }
    }
    
    public static void removeGlobalListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        if (!hasJellyBean()) {
            observer.removeGlobalOnLayoutListener(onGlobalLayoutListener);
        }
        else {
            observer.removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }
    
    /**
     * Uses static final constants to detect if the device's platform version is Lollipop or
     * later.
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }
    
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
    /**
     * Uses static final constants to detect if the device's platform version is Lollipop or
     * later.
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
