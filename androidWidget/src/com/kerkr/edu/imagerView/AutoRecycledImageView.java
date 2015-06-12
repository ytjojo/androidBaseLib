/*
 * 文 件 名:  AutoRecycledImageView.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-3-20
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.kerkr.edu.imagerView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-3-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AutoRecycledImageView  extends ImageView {


    /** <默认构造函数>
     */
    public AutoRecycledImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    /** <默认构造函数>
     */
    public AutoRecycledImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    /** <默认构造函数>
     */
    public AutoRecycledImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }
  
    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable d = getDrawable();
        if(d!= null && d instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) d;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if(bitmap == null || bitmap.isRecycled()){
                return;
            }
        }
        super.onDraw(canvas);
    }

    /**
     * @param bm
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        if(bm.isMutable()){
            
        }
        Drawable d = getDrawable();
        if(d!= null && d instanceof BitmapDrawable){
            d.setCallback(null);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) d;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if(bitmap == null || bitmap.isRecycled()){
               bitmap.recycle();
            }
        }
        super.setImageBitmap(bm);
    }
    /**
     * @param drawable
     */
    @Override
    public void setImageDrawable(Drawable drawable) {
        Drawable d = getDrawable();
        if(d!= null && d instanceof BitmapDrawable){
            d.setCallback(null);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) d;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if(bitmap == null || bitmap.isRecycled()){
               bitmap.recycle();
            }
        }
        super.setImageDrawable(drawable);
    }
    /**
     * @param resId
     */
    @Override
    public void setImageResource(int resId) {
        Drawable d = getDrawable();
        if(d!= null && d instanceof BitmapDrawable){
            d.setCallback(null);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) d;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if(bitmap == null || bitmap.isRecycled()){
               bitmap.recycle();
            }
        }
       
        super.setImageResource(resId);
    }
  
    
}
