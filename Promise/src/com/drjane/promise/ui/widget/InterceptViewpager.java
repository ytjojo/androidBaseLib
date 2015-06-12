/*
 * 文 件 名:  IntercepViewpager.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-3
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-3]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class InterceptViewpager extends ViewPager {
    
    /** <默认构造函数>
     */
    public InterceptViewpager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    /** <默认构造函数>
     */
    public InterceptViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    private float preX;
    
    private float preY;
    
    /**
     * @param arg0
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean res = super.onInterceptTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            preX = event.getX();
            preY = event.getY();
        }
        else {
            if (Math.abs(event.getX() - preX) > 5 && Math.abs(event.getX() - preX) > Math.abs(event.getY() - preY)) {
                return true;
            }
            else {
                preX = event.getX();
                preY = event.getY();
            }
        }
        return res;
    }
    
    /**
     * @param arg0
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        ViewParent viewParent = getParent();
        
        switch(event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                viewParent.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                viewParent.requestDisallowInterceptTouchEvent(false);
                break;
        
        }
        return super.onTouchEvent(event);
        
    }
}
