package com.kerkr.edu.widget;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class OverlayContainer extends LinearLayout {
	private Drawable mForeground;
	private final Rect mBound = new Rect();

	public OverlayContainer(Context context) {
		super(context);
	}

	public OverlayContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Drawable getForegroundDrawable() {
		return mForeground;
	}

	public void setForegroundDrawable(Drawable draw) {
		if (draw == mForeground) {
			return;
		}
		if (mForeground != null) {
			unscheduleDrawable(mForeground);
			mForeground.setCallback(null);
		}
		mForeground = draw;
		if (draw != null) {
			if (draw.isStateful()) {
				draw.setState(getDrawableState());
			}
			draw.setCallback(this);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mBound.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		if (mForeground != null && mForeground.isStateful()) {
			mForeground.setState(getDrawableState());
		}
	}

	@Override
	protected boolean verifyDrawable(Drawable who) {
		return super.verifyDrawable(who) || (who == mForeground);
	}

	@Override
	public void dispatchDraw(Canvas canvas) {
		
		if (mForeground == null) {
			super.dispatchDraw(canvas);
			
		} else {
			final int sc = canvas.save();
			super.dispatchDraw(canvas);
			mForeground.setBounds(mBound);
			mForeground.draw(canvas);
			canvas.restoreToCount(sc);
		}
		super.dispatchDraw(canvas);
	}
}
