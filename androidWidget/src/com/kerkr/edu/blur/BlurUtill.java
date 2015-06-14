package com.kerkr.edu.blur;



import com.kerkr.edu.design.FastBlur;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.View;
import android.view.ViewGroup;

public class BlurUtill {
    
    public static Bitmap getActivityBackBitmap(Activity context) {
        context.getRequestedOrientation();
        
        final ViewGroup decorView = (ViewGroup) context.getWindow().getDecorView();
        ViewGroup viewParent = (ViewGroup) decorView.findViewById(android.R.id.content);
        View v = (ViewGroup) viewParent.getChildAt(0);
        
        View rootView = v.getRootView();
        
        rootView.setDrawingCacheEnabled(true);
        
        rootView.buildDrawingCache();
        
        Bitmap bitmap = rootView.getDrawingCache();
        
        return bitmap;
        
    }
    
    public static Bitmap getWholeScreen(Activity activity) {
        
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bitmap = decorView.getDrawingCache();
        decorView.destroyDrawingCache();
        decorView.setDrawingCacheEnabled(false);
        return bitmap;
    }
    
    public static Bitmap blurView(Bitmap bkg, View view, boolean isDownScale, int alpha, int filterColor) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 1;
        float radius = 20;
        if (isDownScale) {
            scaleFactor = 8;
            radius = 2;
        }
        
        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setAlpha(alpha);
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        
        overlay = FastBlur.doBlurRenderScript(view.getContext(), overlay, (int) radius, true);
        
        Drawable drawable = new BitmapDrawable(view.getContext().getResources(), overlay);
        if (filterColor >= 0) {
            drawable.setColorFilter(filterColor, Mode.MULTIPLY);
            // ToDo
            // 当用带alpha的颜色值时候无效，Color.parse("#20000000");
        }
        return overlay;
    }
    
}
