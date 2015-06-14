package com.kerkr.edu.widget;

import com.kerkr.edu.design.DensityUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.OverScroller;

public class StickyNavLayou extends LinearLayout {
	private ListView listView;
	public int mTopViewHeight;
	private boolean isTopHidden = false;

	private OverScroller mScroller;
	private VelocityTracker mVelocityTracker;
	private TopViewHiddenListener listener;
	private int mTouchSlop;
	private int mMaximumVelocity, mMinimumVelocity;

	private float mLastY;
	private boolean mDragging;
	private float moveDistance = 0;
	private float minBoundDistance = 0;
	private Direction direction = Direction.NONE;

	enum Direction {UP, DOWN, NONE}

	public StickyNavLayou(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		mScroller = new OverScroller(context, new AccelerateDecelerateInterpolator());
		mVelocityTracker = VelocityTracker.obtain();
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
		mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
		minBoundDistance = DensityUtil.dip2px(context, 100);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
//		listView = (ListView) findViewById(R.id.goodsList);
	}

	public void setTopViewHeight(int height) {
		mTopViewHeight = height;
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = getMeasuredHeight();
		listView.setLayoutParams(params);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			float dy = y - mLastY;
			if (Math.abs(dy) > mTouchSlop) {
				if (dy < 0) {//向上滑动
					if (getScrollY() < mTopViewHeight) {//topView没有隐藏，则拦截事件
						return true;
					}
				} else {//向下滑动
					int firstPosition = listView.getFirstVisiblePosition();
					if (firstPosition == 0 && getScrollY() <= mTopViewHeight) {//listView滚动到顶部并且topView将要显示，则拦截事件
						return true;
					}
				}
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mVelocityTracker.addMovement(event);
		int action = event.getAction();
		float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished())
				mScroller.abortAnimation();
			mVelocityTracker.clear();
			mVelocityTracker.addMovement(event);
			mLastY = y;
			return true;
		case MotionEvent.ACTION_MOVE:
			float dy = y - mLastY;
			if (!mDragging && Math.abs(dy) > mTouchSlop) {
				mDragging = true;
			}
			if (mDragging) {//y方向超过此范围才认为是拖动
				if (dy > 0) {
					direction = Direction.DOWN;
				} else {
					direction = Direction.UP;
				}
				scrollBy(0, (int) -dy);
				mLastY = y;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			mDragging = false;
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			break;
		case MotionEvent.ACTION_UP:
			mDragging = false;
			mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
			int velocityY = (int) mVelocityTracker.getYVelocity();
			if (Math.abs(velocityY) > mMinimumVelocity) {
				fling(-velocityY);
			}
			mVelocityTracker.clear();
			//做回弹动作或者滚动到顶部
			if (isTopHidden && listView.getFirstVisiblePosition() == 0) {
				moveDistance = Math.abs(mTopViewHeight - getScrollY());
				if (moveDistance > minBoundDistance) {
					mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 400);
					isTopHidden = false;
					if (listener != null) {
						listener.onTopViewVisible();
					}
				} else {
					mScroller.startScroll(0, getScrollY(), 0, (mTopViewHeight - getScrollY()), 200);
					isTopHidden = true;
				}
				invalidate();
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	public void fling(int velocityY) {
		mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
		invalidate();
	}

	@Override
	public void scrollTo(int x, int y) {
		if (y < 0) {
			y = 0;
		}
		if (y > mTopViewHeight) {
			y = mTopViewHeight;
		}
		if (y != getScrollY()) {
			super.scrollTo(x, y);
		}
		if (!isTopHidden && direction == Direction.UP && (getScrollY() == mTopViewHeight)) {
			isTopHidden = true;
			if (listener != null) {
				listener.onTopViewHidden();
			}
		}
	}

	public void scrollToTopWithAnimation(int duration) {
		isTopHidden = false;
		listView.setSelection(0);
		mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), duration);
		invalidate();//此处一定要记得刷新，shit
	}

	public void scrollToListViewWithAnimation(int duration) {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
		mScroller.startScroll(0, getScrollY(), 0, mTopViewHeight - getScrollY(), duration);
		invalidate();//此处一定要记得刷新，shit
		isTopHidden = true;
		if (listener != null) {
			listener.onTopViewHidden();
		}
	}

	public void setTopViewHiddenListener(TopViewHiddenListener listener) {
		this.listener = listener;
	}

	public interface TopViewHiddenListener {
		public void onTopViewHidden();

		public void onTopViewVisible();
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(0, mScroller.getCurrY());
			invalidate();
		}
	}
}