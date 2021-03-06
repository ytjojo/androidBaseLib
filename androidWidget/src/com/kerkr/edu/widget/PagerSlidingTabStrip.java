package com.kerkr.edu.widget;

import java.util.List;
import java.util.Locale;

import com.kerkr.edu.design.DensityUtil;
import com.ytjojo.widget.R;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PagerSlidingTabStrip extends HorizontalScrollView {
    
    public interface IconTabProvider {
        public int getPageIconResId(int position);
    }
    
    public interface TitleIconTabProvider {
        public final static int NONE_ICON = -1;
        
        public int getPageIconResId(int position);
    }
    
    public interface OnTiltleClickListner {
        
        void onTitleChanged(int postion, String title);
        
        void onTitleSelected(int postion, String title, boolean isOpen);
    }
    
    public interface OnTripClickedListner {
        
        void onTitleClicked(int postion, String title);
        
    }
    
    // @formatter:off
    private static final int[] ATTRS = new int[] { android.R.attr.textSize, android.R.attr.textColor };
    
    // @formatter:on
    
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    
    private final PageListener pageListener = new PageListener();
    
    public OnPageChangeListener delegatePageListener;
    
    private LinearLayout tabsContainer;
    
    private ViewPager pager;
    
    private int tabCount;
    
    private int currentPosition = 0;
    
    private boolean isChecked;
    
    private float currentPositionOffset = 0f;
    
    private Paint rectPaint;
    
    private Paint dividerPaint;
    
    private int indicatorColor = 0xFF666666;
    
    private int underlineColor = 0x1A000000;
    
    private int dividerColor = 0x1A000000;
    
    private boolean shouldExpand = false;
    
    private boolean textAllCaps = true;
    
    private int scrollOffset = 52;
    
    private int indicatorHeight = 8;
    
    private int underlineHeight = 2;
    
    private int dividerPadding = 12;
    
    private int tabPadding = 24;
    
    private int mIndicatorOffsetX = 30;//dp
    
    private int dividerWidth = 1;
    
    private int tabTextSize = 16;
    
    private int tabTextColor = Color.parseColor("#7f7f7f");
    
    private int tabSelectedTextColor = Color.parseColor("#f33525");
    
    private Typeface tabTypeface = null;
    
    private int tabTypefaceStyle = Typeface.NORMAL;
    
    private int lastScrollX = 0;
    
    private int tabBackgroundResId = R.drawable.background_tab;
    
    private Locale locale;
    
    private int mRightDrawableResId = R.drawable.tab_indicator;
    
    private Drawable[] mIndicatorDrawables;
    
    private OnTiltleClickListner mOnTiltleClickListner;
    
    private OnTripClickedListner mOnTripClickedListner;
    
    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }
    
    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        setFillViewport(true);
        setWillNotDraw(false);
        mIndicatorOffsetX = DensityUtil.dip2px(getContext(), mIndicatorOffsetX);
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);
        
        DisplayMetrics dm = getResources().getDisplayMetrics();
        
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);
        
        // get system attrs (android:textSize and android:textColor)
        
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        
        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);
        
        a.recycle();
        
        // get custom attrs
        
        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
        
        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);
        
        a.recycle();
        
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);
        
        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);
        
        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        
        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }
    
    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        
        pager.setOnPageChangeListener(pageListener);
        
        notifyDataSetChanged();
    }
    
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }
    
    public void notifyDataSetChanged() {
        
        tabsContainer.removeAllViews();
        
        tabCount = pager.getAdapter().getCount();
        
        for (int i = 0; i < tabCount; i++) {
            
            if (pager.getAdapter() instanceof TitleIconTabProvider) {
                addTextIconTab(i, pager.getAdapter().getPageTitle(i).toString(), ((TitleIconTabProvider) pager.getAdapter()).getPageIconResId(i));
            }
            else if (pager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) pager.getAdapter()).getPageIconResId(i));
            }
            else {
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }
            
        }
        
        updateTabStyles();
        
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            
            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                
                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });
        
    }
    
    public void toggle() {
        this.isChecked = !isChecked;
        if (mIndicatorDrawables == null) {
            return;
        }
        for (int i = 0; i < tabCount; i++) {
            Drawable drawable = mIndicatorDrawables[i];
            if (drawable == null) {
                continue;
            }
            if (currentPosition == i) {
                setDrawableState(drawable);
            }
            else {
                setDrawableStateBlack(drawable);
                
            }
            
        }
        postInvalidate();
    }
    
    public void setCurrentSelected(int postion) {
        currentPosition = postion;
        changeTextColor();
    }
    
    private void setDrawableState(Drawable d) {
        if (d == null) {
            return;
        }
        if (isChecked) {
            
            d.setState(new int[] {});
        }
        else {
            int[] stateIntArr = new int[] { android.R.attr.state_checked, android.R.attr.state_focused };
            d.setState(stateIntArr);
        }
    }
    
    private void setDrawableStateBlack(Drawable d) {
        if (d == null) {
            return;
        }
        d.setState(new int[] {});
    }
    
    public void changeTextColor() {
        for (int i = 0; i < tabCount; i++) {
            TextView tv = (TextView) tabsContainer.getChildAt(i);
            if (i == currentPosition) {
                tv.setTextColor(tabSelectedTextColor);
            }
            else {
                tv.setTextColor(tabTextColor);
            }
        }
    }
    
    public void setOnTitleClickListner(OnTiltleClickListner l) {
        this.mOnTiltleClickListner = l;
    }
    
    public void setOnTripClickListner(OnTripClickedListner l) {
        this.mOnTripClickedListner = l;
    }
    
    public void setIsOpen(boolean b) {
        this.isChecked = b;
    }
    
    public void addTitleList(List<String> list, final int selectedPostion) {
        dividerColor = Color.parseColor("#b5b5b5");
        isChecked = false;
        tabPadding = 0;
        tabTextSize = DensityUtil.dip2px(getContext(), 14);
        tabsContainer.removeAllViews();
        this.mTitleList = list;
        tabCount = list.size();
        mIndicatorDrawables = new Drawable[tabCount];
        for (int i = 0; i < tabCount; i++) {
            addTextTab(i, list.get(i));
            if (mRightDrawableResId > 0) {
                Drawable drawable = getResources().getDrawable(mRightDrawableResId);
                int width = drawable.getIntrinsicWidth();
                int height = drawable.getIntrinsicHeight();
                width = (int) (width * 0.92f);
                height = (int) (height * 0.92f);
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, width,height);
                mIndicatorDrawables[i] = drawable;
                //                tab.setCompoundDrawables(null, null,drawable, null);
            }
        }
        updateTabStyles();
        
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            
            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                
                currentPosition = selectedPostion;
                scrollToChild(currentPosition, 0);
                toggle();
            }
        });
        changeTextColor();
    }
    
    private void addTextIconTab(final int position, String title, int resId) {
        
        if (resId == TitleIconTabProvider.NONE_ICON) {
            addTextTab(position, title);
            return;
        }
        
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        
        ImageView icon = new ImageView(getContext());
        icon.setImageResource(resId);
        
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        linearLayout.addView(tab, defaultTabLayoutParams);
        linearLayout.addView(icon, defaultTabLayoutParams);
        
        addTab(position, linearLayout);
    }
    
    private void addTextTab(final int position, String title) {
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        
        addTab(position, tab);
    }
    
    private void addIconTab(final int position, int resId) {
        
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        
        addTab(position, tab);
        
    }
    
    private List<String> mTitleList;
    
    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            int tabPostion = position;
            
            @Override
            public void onClick(View v) {
                if (pager != null) {
                    pager.setCurrentItem(position);
                }
                
                if (tabPostion == currentPosition) {
                    if (mOnTripClickedListner != null) {
                        mOnTripClickedListner.onTitleClicked(tabPostion, mTitleList.get(tabPostion));
                    }
                    if (mOnTiltleClickListner != null) {
                        mOnTiltleClickListner.onTitleSelected(tabPostion, mTitleList.get(tabPostion), isChecked);
                        
                    }
                }
                else {
                    currentPosition = tabPostion;
                    changeTextColor();
                    
                    if (mOnTiltleClickListner != null) {
                        
                        mOnTiltleClickListner.onTitleChanged(tabPostion, mTitleList.get(tabPostion));
                        
                    }
                    
                    if (mOnTripClickedListner != null) {
                        mOnTripClickedListner.onTitleClicked(tabPostion, mTitleList.get(tabPostion));
                    }
                    
                }
                toggle();
            }
        });
        
        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
    }
    
    public void setTitlePosition(int position, String title) {
        if (position < 0 && position >= mTitleList.size()) {
            return;
        }
        mTitleList.remove(position);
        mTitleList.add(position, title);
        TextView tv = (TextView) tabsContainer.getChildAt(position);
        if (title != null) {
            if (title.length() > 4) {
                title = title.substring(0, 3) + "...";
            }
            tv.setText(title);
        }
        
    }
    
    private void updateTabStyles() {
        
        for (int i = 0; i < tabCount; i++) {
            
            View v = tabsContainer.getChildAt(i);
            
            v.setBackgroundResource(tabBackgroundResId);
            
            if (v instanceof LinearLayout) {
                v = ((LinearLayout) v).getChildAt(0);
            }
            
            if (v instanceof TextView) {
                
                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                tab.setTypeface(tabTypeface, tabTypefaceStyle);
                tab.setTextColor(tabTextColor);
                tab.setSingleLine();
                //                tab.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
                tab.setEllipsize(TruncateAt.END);
                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    }
                    else {
                        tab.setText(tab.getText().toString().toUpperCase(locale));
                    }
                }
            }
        }
        
    }
    
    private void scrollToChild(int position, int offset) {
        
        if (tabCount == 0) {
            return;
        }
        
        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;
        
        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }
        
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
        
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (isInEditMode() || tabCount == 0) {
            return;
        }
        
        final int height = getHeight();
        
        // draw indicator line
        
        rectPaint.setColor(indicatorColor);
        
        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();
        final float mScrollX = 0;
        final float mScrollY = getScrollY();
        if (mIndicatorDrawables != null) {
            
            for (int i = 0; i < tabCount; i++) {
                canvas.save();
                View mView = tabsContainer.getChildAt(i);
                canvas.translate(mView.getRight() + mScrollX - mIndicatorOffsetX, mScrollY + height / 2 - mIndicatorDrawables[i].getIntrinsicHeight()
                        / 2);
                mIndicatorDrawables[i].draw(canvas);
                canvas.restore();
            }
        }
        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {
            
            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();
            
            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }
        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);
        //draw
        // draw underline
        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);
        // draw divider
        
        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
    }
    

    
    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }
    
    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }
    
    public int getIndicatorColor() {
        return this.indicatorColor;
    }
    
    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }
    
    public int getIndicatorHeight() {
        return indicatorHeight;
    }
    
    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }
    
    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }
    
    public int getUnderlineColor() {
        return underlineColor;
    }
    
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }
    
    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }
    
    public int getDividerColor() {
        return dividerColor;
    }
    
    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }
    
    public int getUnderlineHeight() {
        return underlineHeight;
    }
    
    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }
    
    public int getDividerPadding() {
        return dividerPadding;
    }
    
    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }
    
    public int getScrollOffset() {
        return scrollOffset;
    }
    
    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }
    
    public boolean getShouldExpand() {
        return shouldExpand;
    }
    
    public boolean isTextAllCaps() {
        return textAllCaps;
    }
    
    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }
    
    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }
    
    public int getTextSize() {
        return tabTextSize;
    }
    
    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }
    
    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }
    
    public int getTextColor() {
        return tabTextColor;
    }
    
    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }
    
    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }
    
    public int getTabBackground() {
        return tabBackgroundResId;
    }
    
    public void setTabPaddingLeftRight(int paddingLeftRight) {
        this.tabPadding = paddingLeftRight;
        updateTabStyles();
    }
    
    public int getTabPaddingLeftRight() {
        return tabPadding;
    }
    
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }
    
    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            currentPosition = position;
            currentPositionOffset = positionOffset;

            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

    }    static class SavedState extends BaseSavedState {
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
        int currentPosition;
        
        public SavedState(Parcelable superState) {
            super(superState);
        }
        
        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }
        
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }
    }
    
}