package com.kerkr.edu.progress;


import com.ytjojo.widget.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class UploadProgresView extends View {
	
	private Paint mPaint;//画笔
	
	
	int width,height;
	
	Context context;
	
	float mProgress=0;
	private int mTextSize = 14;
	private int mTextColor;
	
	public UploadProgresView(Context context)
	{
		this(context, null);
		  init(context);
	}

	public UploadProgresView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
		init(context);
	}

	public UploadProgresView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
		TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.UploadProgressView);
		mTextSize = ta.getDimensionPixelSize(R.styleable.UploadProgressView_textSize, 16);
		mProgress = ta.getFloat(R.styleable.UploadProgressView_progress, 0);
		mTextColor = ta.getColor(R.styleable.UploadProgressView_textColor, 0xffffff);
		ta.recycle();
	}
	private void init(Context c){
	    mPaint=new Paint();
	    this.context=context;
	    
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
        mPaint.setAntiAlias(true); // 消除锯齿  
        mPaint.setStyle(Paint.Style.FILL); 
        
        mPaint.setColor(Color.parseColor("#70000000"));//半透明
        canvas.drawRect(0, 0, getWidth(), getHeight()-getHeight()*mProgress/100, mPaint);
        
        mPaint.setColor(Color.parseColor("#00000000"));//全透明
        canvas.drawRect(0, getHeight()-getHeight()*mProgress/100, getWidth(),  getHeight(), mPaint);
        
        mPaint.setTextSize(30);
        mPaint.setColor(mTextColor);
		mPaint.setStrokeWidth(2);
		Rect rect=new Rect();
		mPaint.getTextBounds("100%", 0, "100%".length(), rect);//确定文字的宽度
		canvas.drawText(mProgress+"%", getWidth()/2-rect.width()/2,getHeight()/2, mPaint);
        
	}
	
	public void setProgress(int progress){
		this.mProgress=progress;
		postInvalidate();
	};
	public void setTextColor(int color){
	    this.mTextColor=color;
	    postInvalidate();
	};
	
	
 }  