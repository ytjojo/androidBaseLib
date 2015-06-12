/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kerkr.edu.widget.viewpager;


import com.ytjojo.widget.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Draws circles (one for each view). The current view position is filled and
 * others are only stroked.
 */
public class CircleZoomPageIndicator extends View implements PageIndicator {
    
    public static final int HORIZONTAL = 0;
    
    public static final int VERTICAL = 1;
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;
    private final Paint mPaintNormal;
    private final Paint mPaintCur;
    private final Paint mPaintTarget;
    private float mRadius;
    private float mRadiusCur;
    private float mRadiusTarget;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;
    private int mCurrentPage;
    private int mTargetPage = -1;
    private int mSnapPage;
    private int mCurrentOffset;
    private int mScrollState;
    private int mPageSize;
    private int mOrientation;
    private boolean mCentered;
    private boolean mSnap;
    private int mTouchSlop;
    private float mLastMotionX = -1;
    private boolean mIsDragging;
    
    private int mMaxRadius;
    
    private int mNormalCorlor;
    
    private int mSelectedCorlor;
    
    private int mTrueSize;
    
    public CircleZoomPageIndicator(Context context) {
        this(context, null);
    }
    
    public CircleZoomPageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.vpiCirclePageIndicatorStyle);
    }
    
    public CircleZoomPageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        // Load defaults from resources
        final Resources res = getResources();
        final int defaultFillColor = res.getColor(R.color.default_circle_indicator_fill_color);
        final int defaultOrientation = res.getInteger(R.integer.default_circle_indicator_orientation);
        final int defaultStrokeColor = res.getColor(R.color.default_circle_indicator_stroke_color);
        final float defaultStrokeWidth = res.getDimension(R.dimen.default_circle_indicator_stroke_width);
        final float defaultRadius = res.getDimension(R.dimen.default_circle_indicator_radius);
        final boolean defaultCentered = res.getBoolean(R.bool.default_circle_indicator_centered);
        final boolean defaultSnap = res.getBoolean(R.bool.default_circle_indicator_snap);
        
        // Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePageIndicator, defStyle, 0);
        
        mCentered = a.getBoolean(R.styleable.CirclePageIndicator_centered, defaultCentered);
        mOrientation = HORIZONTAL;
        
        mRadius = a.getDimension(R.styleable.CirclePageIndicator_radius, defaultRadius);
        mMaxRadius = (int) (mRadius * 1.25f);
        mSnap = a.getBoolean(R.styleable.CirclePageIndicator_snap, defaultSnap);
        
        mSelectedCorlor = a.getColor(R.styleable.CirclePageIndicator_strokeColor, defaultStrokeColor);
        mNormalCorlor = a.getColor(R.styleable.CirclePageIndicator_fillColor, defaultFillColor);
        a.recycle();
        
        mPaintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintNormal.setStyle(Style.FILL);
        mPaintNormal.setColor(mNormalCorlor);
        mPaintCur = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCur.setStyle(Style.FILL);
        mPaintCur.setColor(mSelectedCorlor);
        mPaintTarget = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTarget.setStyle(Paint.Style.FILL);
        mPaintTarget.setColor(mNormalCorlor);
        mRadiusCur = mMaxRadius;
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }
    
    public int getFillColor() {
        return mPaintCur.getColor();
    }
    
    public void setFillColor(int fillColor) {
        mPaintCur.setColor(fillColor);
        invalidate();
    }
    
    public int getOrientation() {
        return mOrientation;
    }
    
    public void setOrientation(int orientation) {
        switch (orientation) {
            case HORIZONTAL:
            case VERTICAL:
                mOrientation = orientation;
                updatePageSize();
                requestLayout();
                break;

            default:
                throw new IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.");
        }
    }
    
    public float getRadius() {
        return mRadius;
    }
    
    public void setRadius(float radius) {
        mRadius = radius;
        invalidate();
    }
    
    public int getStrokeColor() {
        return mPaintNormal.getColor();
    }
    
    public void setStrokeColor(int strokeColor) {
        mPaintNormal.setColor(strokeColor);
        invalidate();
    }
    
    public float getStrokeWidth() {
        return mPaintNormal.getStrokeWidth();
    }
    
    public void setStrokeWidth(float strokeWidth) {
        mPaintNormal.setStrokeWidth(strokeWidth);
        invalidate();
    }
    
    public boolean isCentered() {
        return mCentered;
    }
    
public void setCentered(boolean centered) {
        mCentered = centered;
        invalidate();
    }
    
    public boolean isSnap() {
        return mSnap;
    }
    
    public void setSnap(boolean snap) {
        mSnap = snap;
        invalidate();
    }
    
    @Override
    public void notifyDataSetChanged() {
        invalidate();
    }
    
        @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mCurrentPage = position;
        mCurrentOffset = positionOffsetPixels;
        if (positionOffset > 0) {
            if (mCurrentPage + 1 < mViewPager.getAdapter().getCount()) {
                mTargetPage = mCurrentPage + 1;
            }
            else {
                mTargetPage = -1;
            }
        }
        else {
            if (mCurrentPage - 1 >= 0) {
                mTargetPage = mCurrentPage - 1;
            }
            else {
                mTargetPage = -1;
            }
        }
        updateSizeAndColor(positionOffset);
        updatePageSize();
        invalidate();

        if (mListener != null) {
         
            mListener.onPageScrolled(mCurrentPage % mTrueSize, positionOffset, positionOffsetPixels);
        }
    };
    
    private void updateSizeAndColor(float offset) {
        mRadiusCur = offsetRadius(Math.abs(offset), mMaxRadius, mRadius);
        mRadiusTarget = offsetRadius(Math.abs(offset), mRadius, mMaxRadius);
        mPaintCur.setColor(offsetColor(mSelectedCorlor, mNormalCorlor, offset));
        mPaintTarget.setColor(offsetColor(mNormalCorlor, mSelectedCorlor, offset));
    }
    
    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;

        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }
    
    @Override
    public void onPageSelected(int position) {
        if (mSnap || mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position;
            mSnapPage = position;
            mTargetPage = -1;
            invalidate();
        }
        if (mListener != null) {
            mListener.onPageSelected((position+mTrueSize) % mTrueSize);
        }
    }
    
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPage = savedState.currentPage;
        mSnapPage = savedState.currentPage;
        requestLayout();
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = mCurrentPage;
        return savedState;
    }
    
    @Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        if (mViewPager == null || mViewPager.getAdapter().getCount() == 0)
            return false;

        final int action = ev.getAction();

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mLastMotionX = ev.getX();
                break;

            case MotionEvent.ACTION_MOVE: {
                final int activePointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float x = MotionEventCompat.getX(ev, activePointerIndex);
                final float deltaX = x - mLastMotionX;

                if (!mIsDragging) {
                    if (Math.abs(deltaX) > mTouchSlop) {
                        mIsDragging = true;
                    }
                }

                if (mIsDragging) {
                    if (!mViewPager.isFakeDragging()) {
                        mViewPager.beginFakeDrag();
                    }

                    mLastMotionX = x;
                    if (mViewPager.isFakeDragging()) {
                        mViewPager.fakeDragBy(deltaX);
                    }
                }

                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!mIsDragging) {
                    final int count = mViewPager.getAdapter().getCount();
                    final int width = getWidth();
                    final float halfWidth = width / 2f;
                    final float sixthWidth = width / 6f;

                    if (mCurrentPage > 0 && ev.getX() < halfWidth - sixthWidth) {
                        mViewPager.setCurrentItem(mCurrentPage - 1);
                        return true;
                    }
                    else if (mCurrentPage < count - 1 && ev.getX() > halfWidth + sixthWidth) {
                        mViewPager.setCurrentItem(mCurrentPage + 1);
                        return true;
                    }
                }

                mIsDragging = false;
                mActivePointerId = INVALID_POINTER;
                if (mViewPager.isFakeDragging()) {
                    mViewPager.endFakeDrag();
                }
                break;

            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, index);
                mLastMotionX = x;
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, mActivePointerId));
                break;
        }

        return true;
    }
    
    //	@Override
    //	public void setPagingEnabled(boolean enabled) {
    //		mViewPager.setPagingEnabled(enabled);
    //	}
    
    public float offsetRadius(float offset, float fromR, float toR) {
        float result = fromR + (toR - fromR) * offset;
        return result;
    }
    
    public int offsetColor(int fromColor, int toColor, float offset) {
        int froma = Color.alpha(fromColor);
        int fromr = Color.red(fromColor);
        int fromb = Color.blue(fromColor);
        int fromg = Color.green(fromColor);

        int toa = Color.alpha(toColor);
        int tor = Color.red(toColor);
        int tob = Color.blue(toColor);
        int tog = Color.green(toColor);

        toa = (int) (froma + (toa - froma) * offset);
        tor = (int) (fromr + (tor - fromr) * offset);
        tob = (int) (fromb + (tob - fromb) * offset);
        tog = (int) (fromg + (tog - fromg) * offset);
        return Color.argb(toa, tor, tog, tob);

    }
    
    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null)
            throw new IllegalStateException("ViewPager has not been bound.");
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        invalidate();
    }
    
    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }
    
    @Override
    public void setViewPager(ViewPager pager) {
        if (pager.getAdapter() == null)
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        mViewPager = pager;
        mViewPager.setOnPageChangeListener(this);
        mCurrentPage = mViewPager.getCurrentItem();
        updatePageSize();
        invalidate();
    }
    
    public void setTruePagerSize(int size) {
        if (size <= 0) {
            return;
        }
        this.mTrueSize = size;
        if (mViewPager != null && mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() > 0) {
            
            postInvalidate();
        }
    }
    
    @Override
    public void setViewPager(ViewPager pager, int initialPosition) {
        setViewPager(pager);
        setCurrentItem(initialPosition);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (mViewPager == null || mTrueSize <= 0)
            return;
        final int count = mTrueSize;
        if (count == 0)
            return;
        
        int longSize;
        int longPaddingBefore;
        int longPaddingAfter;
        int shortPaddingBefore;
        if (mOrientation == HORIZONTAL) {
            longSize = getWidth();
            longPaddingBefore = getPaddingLeft();
            longPaddingAfter = getPaddingRight();
            shortPaddingBefore = getPaddingTop();
        }
        else {
            longSize = getHeight();
            longPaddingBefore = getPaddingTop();
            longPaddingAfter = getPaddingBottom();
            shortPaddingBefore = getPaddingLeft();
        }
        
        final float threeRadius = mRadius * 3;
        final float shortOffset = shortPaddingBefore + mRadius;
        float longOffset = longPaddingBefore + mRadius;
        if (mCentered) {
            longOffset += (longSize - longPaddingBefore - longPaddingAfter) / 2.0f - count * threeRadius / 2.0f;
        }
        
        float dX;
        float dY;
        
        // Draw stroked circles
        for (int iLoop = 0; iLoop < count; iLoop++) {
            float drawLong = longOffset + iLoop * threeRadius;
            if (mOrientation == HORIZONTAL) {
                dX = drawLong;
                dY = shortOffset;
            }
            else {
                dX = shortOffset;
                dY = drawLong;
            }
            if (iLoop == mCurrentPage % mTrueSize && mTrueSize > 1) {
                canvas.drawCircle(dX, dY, mRadiusCur, mPaintCur);
            }
            else if (iLoop == mTargetPage % mTrueSize) {
                canvas.drawCircle(dX, dY, mRadiusTarget, mPaintTarget);
            }
            else {
                canvas.drawCircle(dX, dY, mRadius, mPaintNormal);
                
            }
        }
        //
        //		// Draw the filled circle according to the current scroll
        //		float cx = (mSnap ? mSnapPage : mCurrentPage) * threeRadius;
        //		if (!mSnap && mPageSize != 0) {
        //			cx += mCurrentOffset * 1.0f / mPageSize * threeRadius;
        //		}
        //		if (mOrientation == HORIZONTAL) {
        //			dX = longOffset + cx;
        //			dY = shortOffset;
        //		} else {
        //			dX = shortOffset;
        //			dY = longOffset + cx;
        //		}
        //		canvas.drawCircle(dX, dY, mRadius, mPaintFill);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOrientation == HORIZONTAL) {
            setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
        }
        else {
            setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec));
        }
    }
    
    /**
     * Determines the width of this view
     * 
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureLong(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        
        if (specMode == MeasureSpec.EXACTLY || mViewPager == null) {
            // We were told how big to be
            result = specSize;
        }
        else {
            // Calculate the width according the views count
            final int count = mViewPager.getAdapter().getCount();
            result = (int) (getPaddingLeft() + getPaddingRight() + count * 2 * mRadius + (count - 1) * mRadius + 1);
            // Respect AT_MOST value if that was what is called for by
            // measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    
    /**
     * Determines the height of this view
     * 
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureShort(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        }
        else {
            // Measure the height
            result = (int) (2 * mRadius + getPaddingTop() + getPaddingBottom() + 1);
            // Respect AT_MOST value if that was what is called for by
            // measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    
    private void updatePageSize() {
        if (mViewPager != null) {
            mPageSize = mOrientation == HORIZONTAL ? mViewPager.getWidth() : mViewPager.getHeight();
        }
    }
    
    static class SavedState extends BaseSavedState {
        
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int currentPage;
        
        public SavedState(Parcelable superState) {
            super(superState);
        }
        
        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }
        
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }
    }
}