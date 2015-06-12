package com.kerkr.edu.widget;

import java.util.ArrayList;
import java.util.List;

import com.kerkr.edu.log.VALog;
import com.ytjojo.widget.R;


import android.R.integer;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
    
    private int mHorizontalSpacing;
    
    private int mVerticalSpacing;
    
    private Paint mPaint;
    
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        try {
            mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpacing, 0);
            mVerticalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, 0);
        }
        finally {
            a.recycle();
        }
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xffff0000);
        mPaint.setStrokeWidth(2.0f);
    }
    ArrayList<View> mVisibleViews = new ArrayList<View>();
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        boolean growHeight = widthMode != MeasureSpec.UNSPECIFIED;

        int width = 0;
        int height = getPaddingTop();

        int currentWidth = getPaddingLeft(); //当前行的宽度
        int currentHeight = 0;               //当前行的高度

        boolean breakLine = false;
        boolean newLine = true;
        int spacing = 0;
        mVisibleViews.clear();
        int count = getChildCount();
        
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if(child.getVisibility() ==View.VISIBLE){
              
               mVisibleViews.add(child);
            }
        }
        count = mVisibleViews.size();
        for (int i = 0; i < count; i++) {
            View child =  mVisibleViews.get(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            spacing = mHorizontalSpacing;
            if (lp.horizontalSpacing >= 0) {
                spacing = lp.horizontalSpacing;
            }
            breakLine =lp.breakLine;
            if (growHeight && (breakLine || currentWidth + child.getMeasuredWidth() > widthSize)) {
                newLine = true;
                width = Math.max(width, currentWidth - spacing);
                height += currentHeight + mVerticalSpacing;
                currentHeight = 0;
                currentWidth = getPaddingLeft();
                
            } else {
                newLine = false;
            }
            if(i== count-1 && newLine){
                newLine = false;
            }
            lp.x = currentWidth;
          
            lp.y = height;
           
            currentWidth += child.getMeasuredWidth() + spacing;
            currentHeight = Math.max(currentHeight, child.getMeasuredHeight());
            breakLine = lp.breakLine;
           
        }

        if (!newLine) {
            height += currentHeight;
            width = Math.max(width, currentWidth - spacing);
        }


        width += getPaddingRight();
        height += getPaddingBottom();
        mVisibleViews.clear();
        setMeasuredDimension(resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec));
    }
    
    private void setChild(int widthMeasureSpec, int heightMeasureSpec){
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        boolean growHeight = widthMode != MeasureSpec.UNSPECIFIED;

        int width = 0;
        int height = getPaddingTop();

        int currentWidth = getPaddingLeft(); //当前行的宽度
        int currentHeight = 0;               //当前行的高度

        boolean breakLine = false;
        boolean newLine = true;
        int spacing = 0;

        final int count = getChildCount();
        int maxLineHeight = 0;
        int lineCount = 0;
        
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if(child.getVisibility() ==View.GONE){
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            spacing = mHorizontalSpacing;
            if (lp.horizontalSpacing >= 0) {
                spacing = lp.horizontalSpacing;
            }
           
            if (growHeight && (breakLine || currentWidth + child.getMeasuredWidth() > widthSize)) {
                newLine = true;
                lineCount++;
                width = Math.max(width, currentWidth - spacing);
                
                height += currentHeight + mVerticalSpacing;
                currentHeight = 0;
                currentWidth = getPaddingLeft();
                
            } else {
                newLine = false;
            }
           
            lp.x = currentWidth;
          
            lp.y = height;
            currentWidth += child.getMeasuredWidth() + spacing;
            currentHeight = Math.max(currentHeight, child.getMeasuredHeight());
         
            Log.i("TAG",i+" x"+ lp.x + " y" +  lp.y+ "heigth"  + height);
            
            breakLine = lp.breakLine;
           
        }
       
        height += currentHeight;
        width = Math.max(width, currentWidth - spacing);


        width += getPaddingRight();
        height += getPaddingBottom();
        VALog.i( "-------------------------------------------------------------------------------heigth"  + height);
        setMeasuredDimension(resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec));
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if(child.getVisibility() ==GONE){
                continue;
            }
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y + child.getMeasuredHeight());
        }
        
    }
    
    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        
    }
    
    /**
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.dispatchDraw(canvas);
        Rect r = new Rect();
        getGlobalVisibleRect(r);
    }
    
    /**
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
    }
    
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean more = super.drawChild(canvas, child, drawingTime);
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (lp.horizontalSpacing > 0) {
            float x = child.getRight();
            float y = child.getTop() + child.getHeight() / 2.0f;
            canvas.drawLine(x, y - 4.0f, x, y + 4.0f, mPaint);
            canvas.drawLine(x, y, x + lp.horizontalSpacing, y, mPaint);
            canvas.drawLine(x + lp.horizontalSpacing, y - 4.0f, x + lp.horizontalSpacing, y + 4.0f, mPaint);
        }
        if (lp.breakLine) {
            float x = child.getRight();
            float y = child.getTop() + child.getHeight() / 2.0f;
            canvas.drawLine(x, y, x, y + 6.0f, mPaint);
            canvas.drawLine(x, y + 6.0f, x + 6.0f, y + 6.0f, mPaint);
        }
        return more;
    }
    
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }
    
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
    
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
    
    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }
    
    public static class LayoutParams extends MarginLayoutParams {
        int x;
        
        int y;
        
        
        
        
        public int horizontalSpacing;
        
        public boolean breakLine;
        
        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout_LayoutParams);
            try {
                horizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_LayoutParams_layout_horizontalSpacing, -1);
                breakLine = a.getBoolean(R.styleable.FlowLayout_LayoutParams_layout_breakLine, false);
            }
            finally {
                a.recycle();
            }
        }
        
        public LayoutParams(int w, int h) {
            super(w, h);
        }
    }
}
