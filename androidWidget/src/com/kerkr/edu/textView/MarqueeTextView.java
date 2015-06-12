package com.kerkr.edu.textView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 这是一个不用获得焦点也能实现跑马灯效果的textView
 * 
 * @author Chenzt
 * 
 */
public class MarqueeTextView extends TextView {
    private boolean isStartMarquee;
    public MarqueeTextView(Context con) {
        super(con);
    }
    
    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    public boolean isFocused() {
        return true;
    }
    
    
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        
        if (focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
       
       
        if (hasWindowFocus)
            super.onWindowFocusChanged(hasWindowFocus);
    }
    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        if(!isStartMarquee){
            isStartMarquee = true;
            super.onWindowFocusChanged(true);
        }
        super.onDraw(canvas);
    }
    /**
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // TODO Auto-generated method stub
        super.onLayout(changed, left, top, right, bottom);
        
    }
    /**
     * 
     */
    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
    }
    /**
     * 
     */
    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
//        isStartMarquee = false;
    }
}