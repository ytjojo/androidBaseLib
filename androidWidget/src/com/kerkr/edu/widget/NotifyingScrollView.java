package com.kerkr.edu.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ScrollView;

/**
 * @author Cyril Mottier with modifications from Manuel Peinado
 */
public class NotifyingScrollView extends ScrollView {
	// Edge-effects don't mix well with the translucent action bar in Android
	// 2.X
	private boolean mDisableEdgeEffects = true;
	private ViewGroup contentView;

	/**
	 * @author Cyril Mottier
	 */
	public interface OnScrollChangedListener {
		void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);

		void onTop(ScrollView who);

		void onBottom(ScrollView who);
	}

	private OnScrollChangedListener mOnScrollChangedListener;

	private void disableAutoScrollToBottom() {
		this.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.requestFocusFromTouch();
				return false;
			}
		});
	}

	private void enableChildAutoScrollToBottom() {
		this.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
		this.setFocusable(false);
		this.setFocusableInTouchMode(false);
		this.setOnTouchListener(null);
	}

	public NotifyingScrollView(Context context) {
		super(context);
		setOverScrollMode(OVER_SCROLL_ALWAYS);
	}

	public NotifyingScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOverScrollMode(OVER_SCROLL_ALWAYS);
	}

	public NotifyingScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOverScrollMode(OVER_SCROLL_ALWAYS);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mOnScrollChangedListener != null) {
			mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
			checkBorder();
		}

	}

	@Override
	public void addView(View child) {
		// TODO Auto-generated method stub
		super.addView(child);
		if (child != null) {

		}
	}

	public void setOnScrollChangedListener(OnScrollChangedListener listener) {
		mOnScrollChangedListener = listener;
	}

	@Override
	protected float getTopFadingEdgeStrength() {
		// http://stackoverflow.com/a/6894270/244576
		if (mDisableEdgeEffects
				&& Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return 0.0f;
		}
		return super.getTopFadingEdgeStrength();
	}

	@Override
	protected float getBottomFadingEdgeStrength() {
		// http://stackoverflow.com/a/6894270/244576
		if (mDisableEdgeEffects
				&& Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return 0.0f;
		}
		return super.getBottomFadingEdgeStrength();
	}

	private boolean isPressed;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = MotionEventCompat.getActionMasked(ev);
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			isPressed = true;

			break;
		case MotionEvent.ACTION_UP:

			isPressed = false;
			break;
		}
		return super.onTouchEvent(ev);
	}

	public void scrollToBottom() {
		this.post(new Runnable() {

			@Override
			public void run() {
				fullScroll(ScrollView.FOCUS_DOWN);

			}
		});
	}

	public void checkBorder() {
		if (contentView != null
				&& contentView.getMeasuredHeight() <= getScrollY()
						+ getHeight()) {
			if (mOnScrollChangedListener != null) {
				mOnScrollChangedListener.onBottom(this);
			}
		} else if (getScrollY() == 0) {
			if (mOnScrollChangedListener != null) {
				mOnScrollChangedListener.onTop(this);
			}
		}
	}

	public void scrollToTop() {
		this.post(new Runnable() {

			@Override
			public void run() {
				fullScroll(ScrollView.FOCUS_UP);

			}
		});
	}

	public void scrollToYPosition(int y) {
		this.smoothScrollTo(0, y);
		this.pageScroll(View.FOCUS_DOWN);
		this.arrowScroll(View.FOCUS_UP);
	}

}