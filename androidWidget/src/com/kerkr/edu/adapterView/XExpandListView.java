/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * 		Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.kerkr.edu.adapterView;


import com.kerkr.edu.app.CompatUtil;
import com.kerkr.edu.log.VALog;
import com.ytjojo.widget.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class XExpandListView extends ExpandableListView implements OnScrollListener {
    private final static String TAG = "XExpandListView";
    
    private float mLastY = -1; // save event y
    
    private Scroller mScroller; // used for scroll back
    
    private OnScrollListener mScrollListener; // user's scroll listener
    
    // the interface to trigger refresh and load more.
    private IXListViewListener mListViewListener;
    
    // -- header view
    private XListViewHeader mHeaderView;
    
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    
    private TextView mHeaderTimeView;
    
    private int mHeaderViewHeight; // header view's height
    
    private boolean mEnablePullRefresh = true;
    
    private boolean mPullRefreshing = false; // is refreashing.
    
    // -- footer view
    private XListViewFooter mFooterView;
    
    private boolean mEnablePullLoad;
    
    private boolean mPullLoading;
    
    private boolean mIsFooterReady = false;
    
    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;
    
    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    
    private final static int SCROLLBACK_HEADER = 0;
    
    private final static int SCROLLBACK_FOOTER = 1;
    
    private final static int SCROLL_DURATION = 400; // scroll back duration
    
    private final static int PULL_LOAD_MORE_DELTA = 10; // when pull up >= 50px
                                                        // at bottom, trigger
                                                        // load more.
    
    private final static float OFFSET_RADIO = 1.3f; // support iOS like pull
    
    private static final boolean DEBUG = true;
    
    private int lastFirstVisibleGroupPos = -1;
    
    private View mPinnerHeaderView;//warnning :如果子view有Textview 不能用 singleline=ture否则无法显示textview
    
    private View mTouchTarget;
    
    private int mHeaderWidth;
    
    private int mHeaderHeight;
    
    private boolean mActionDownHappened = false;
    
    protected boolean mIsHeaderGroupClickable = true;
    
    private ExpandableListAdapter mAdapter;
    
    private enum State {
        
    }
    
    // feature.
    
    /**
     * @param context
     */
    public XExpandListView(Context context) {
        super(context);
        initWithContext(context);
    }
    
    public XExpandListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }
    
    public XExpandListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }
    
    private void initWithContext(Context context) {
        setFadingEdgeLength(0);
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);
        
        // init header view
        mHeaderView = new XListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
        mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.xlistview_header_time);
        addHeaderView(mHeaderView);
        
        // init footer view
        mFooterView = new XListViewFooter(context);
        
        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeaderViewHeight = mHeaderViewContent.getHeight();
                
                CompatUtil.removeGlobalListener(getViewTreeObserver(), this);
            }
        });
    }
    
    /**
     * 给group添加点击事件监听
     * @param onGroupClickListener 监听
     * @param isHeaderGroupClickable 表示header是否可点击<br/>
     * note : 当不想group可点击的时候，需要在OnGroupClickListener#onGroupClick中返回true，
     * 并将isHeaderGroupClickable设为false即可
     */
    public void setOnGroupClickListener(OnGroupClickListener onGroupClickListener, boolean isHeaderGroupClickable) {
        mIsHeaderGroupClickable = isHeaderGroupClickable;
        super.setOnGroupClickListener(onGroupClickListener);
    }
    
    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        // make sure XListViewFooter is the last footer view, and only add once.
        mAdapter = adapter;
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(mFooterView);
        }
       
        super.setAdapter(adapter);
        setPinnerView(adapter);
    }
    
    public void setPinnerView(ExpandableListAdapter adapter) {
        if (adapter == null) {
            
            mHeaderView = null;
            mHeaderWidth = mHeaderHeight = 0;
            return;
        }
        if (mPinnerHeaderView != null|| adapter.getGroupCount() ==0) {
            return;
        }
        
        mPinnerHeaderView = adapter.getGroupView(0, true, null, this);
        if(mPinnerHeaderView == null){
            return;
        }
        if(mPinnerHeaderView.getLayoutParams() == null){
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            mPinnerHeaderView.setLayoutParams(params);
        }
        //	        int firstVisiblePos = getFirstVisiblePosition();
        //	        int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
        //	        adapter.getGroupView(firstVisibleGroupPos, true, mPinnerHeaderView, this);
        requestLayout();
        postInvalidate();
    }
    private int widthMeasureSpec,heightMeasureSpec;
    /**
    * @param widthMeasureSpec
    * @param heightMeasureSpec
    */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
        if (mPinnerHeaderView == null) {
            return;
        }
        measureChild(mPinnerHeaderView, widthMeasureSpec, heightMeasureSpec);
        mHeaderWidth = mPinnerHeaderView.getMeasuredWidth();
        mHeaderHeight = mPinnerHeaderView.getMeasuredHeight();
    }
    
    /**
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
        
        if (mPinnerHeaderView == null) {
            return;
        }
        int delta = mPinnerHeaderView.getTop();
        mPinnerHeaderView.layout(0, delta, mHeaderWidth, mHeaderHeight + delta);
    }
    
    /**
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.dispatchDraw(canvas);
        if (mPinnerHeaderView != null && getFirstVisiblePosition()>=getHeaderViewsCount()) {
            drawChild(canvas, mPinnerHeaderView, getDrawingTime());
        }
    }
    
    /**
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int pos = pointToPosition(x, y);
        if (mPinnerHeaderView != null && y >= mPinnerHeaderView.getTop() && y <= mPinnerHeaderView.getBottom()) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                mTouchTarget = getTouchTarget(mPinnerHeaderView, x, y);
                mActionDownHappened = true;
            }
            else if (ev.getAction() == MotionEvent.ACTION_UP) {
                View touchTarget = getTouchTarget(mPinnerHeaderView, x, y);
                if (touchTarget == mTouchTarget && mTouchTarget.isClickable()) {
                    mTouchTarget.performClick();
                    invalidate(new Rect(0, 0, mHeaderWidth, mHeaderHeight));
                }
                else if (mIsHeaderGroupClickable) {
                    int groupPosition = getPackedPositionGroup(getExpandableListPosition(pos));
                    if (groupPosition != INVALID_POSITION && mActionDownHappened) {
                        if (isGroupExpanded(groupPosition)) {
                            collapseGroup(groupPosition);
                        }
                        else {
                            expandGroup(groupPosition);
                        }
                    }
                }
                mActionDownHappened = false;
            }
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
    
    private View getTouchTarget(View view, int x, int y) {
        if (!(view instanceof ViewGroup)) {
            return view;
        }
        
        ViewGroup parent = (ViewGroup) view;
        int childrenCount = parent.getChildCount();
        final boolean customOrder = isChildrenDrawingOrderEnabled();
        View target = null;
        for (int i = childrenCount - 1; i >= 0; i--) {
            final int childIndex = customOrder ? getChildDrawingOrder(childrenCount, i) : i;
            final View child = parent.getChildAt(childIndex);
            if (isTouchPointInView(child, x, y)) {
                target = child;
                break;
            }
        }
        if (target == null) {
            target = parent;
        }
        
        return target;
    }
    
    public void requestRefreshHeader() {
        refreshPinnerHeader();
        invalidate(new Rect(0, 0, mHeaderWidth, mHeaderHeight));
    }
    
    protected void refreshPinnerHeader() {
        if (mPinnerHeaderView == null) {
            return;
        }
  
        int firstVisiblePos = getFirstVisiblePosition();
        int pos = firstVisiblePos + 1;
        int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
        int group = getPackedPositionGroup(getExpandableListPosition(pos));
        if (DEBUG) {
//            Log.d(TAG, "refreshHeader firstVisibleGroupPos=" + firstVisibleGroupPos);
        }
        
        if (group == firstVisibleGroupPos + 1) {
            View view = getChildAt(1);
            if (view == null) {
                Log.w(TAG, "Warning : refreshHeader getChildAt(1)=null");
                return;
            }
            if (view.getTop() < mHeaderHeight) {
                int delta = mHeaderHeight - view.getTop();
                mPinnerHeaderView.layout(0, -delta, mHeaderWidth, mHeaderHeight - delta);
                
            }
            else {
                //TODO : note it, when cause bug, remove it
                mPinnerHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
                
            }
            //处理透明时候隐藏顶部groupview从而只显示浮在顶层的半透明层
            if (view.getTop() < 0) {
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    int count = viewGroup.getChildCount();
                    for (int i = 0; i < count; i++) {
                        viewGroup.getChildAt(i).setVisibility(View.INVISIBLE);
                    }
                }
            }
            else {
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    int count = viewGroup.getChildCount();
                    for (int i = 0; i < count; i++) {
                        viewGroup.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                }
            }
        }
        else {
            mPinnerHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
        }
        if (mAdapter!= null) {
            if(lastFirstVisibleGroupPos != firstVisibleGroupPos){
                
                lastFirstVisibleGroupPos = firstVisibleGroupPos;
                if(firstVisibleGroupPos < 0 ){
                    firstVisibleGroupPos = 0;
                }
                mPinnerHeaderView =  mAdapter.getGroupView(firstVisibleGroupPos, true, mPinnerHeaderView, null);//如果有问题就用下面的方法
// TODO               mPinnerHeaderView =  mAdapter.getGroupView(firstVisibleGroupPos, true, null, this);
                measureChild(mPinnerHeaderView, widthMeasureSpec, heightMeasureSpec);
                mPinnerHeaderView.layout(0, 0, mPinnerHeaderView.getMeasuredWidth(), mPinnerHeaderView.getMeasuredHeight());
            }
        }
    }
    
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view.isClickable() && y >= view.getTop() && y <= view.getBottom() && x >= view.getLeft() && x <= view.getRight()) {
            return true;
        }
        return false;
    }
    
    /**
     * enable or disable pull down refresh feature.
     * 
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        }
        else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * enable or disable pull up load more feature.
     * 
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            mFooterView.hide();
            mFooterView.setOnClickListener(null);
        }
        else {
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }
    
    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh() {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            mHeaderView.setState(XListViewHeader.STATE_FINISH_REFRESHING);
            resetHeaderHeight();
        }
    }
    
    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPullLoading == true) {
            mPullLoading = false;
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
        }
    }
    
    /**
     * set last refresh time
     * 
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderTimeView.setText(time);
    }
    
    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }
    
    private void updateHeaderHeight(float delta) {
        int targetHeight =(int) delta + mHeaderView.getVisiableHeight();
        if(targetHeight > 2.0f * mHeaderViewHeight){
            targetHeight = (int) (2.0f * mHeaderViewHeight);
        }
        mHeaderView.setVisiableHeight(targetHeight);
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                mHeaderView.setState(XListViewHeader.STATE_READY);
            }
            else {
                mHeaderView.setState(XListViewHeader.STATE_NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }
    public boolean isHeaderShow(){
        VALog.i("----------------------- height headview  :" + mHeaderView.getHeight());
       return mHeaderView.getVisiableHeight()>0;
    }
    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }
    
    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;
        if (mEnablePullLoad && !mPullLoading) {
            //			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
            //													// more.
            //				mFooterView.setState(XListViewFooter.STATE_READY);
            //			} else {
            //				mFooterView.setState(XListViewFooter.STATE_NORMAL);
            //			}
            checkLoadMore();
        }
//        VALog.i(mFooterView.getParent() + "footer height" + mFooterView.getHeight() + " delta" + delta);
        final int maxHeight= (int) (1.5f * mHeaderViewHeight);
        if(height >= maxHeight){
            height = maxHeight;
        }
        mFooterView.setBottomMargin(height);
        
        // setSelection(mTotalItemCount - 1); // scroll to bottom
    }
    
    private void checkLoadMore() {
        if (mEnablePullLoad && !mPullLoading) {
            if (getLastVisiblePosition() == mTotalItemCount - 1) {
                mFooterView.setState(XListViewFooter.STATE_READY);
                startLoadMore();
            }
            else {
                mFooterView.setState(XListViewFooter.STATE_NORMAL);
            }
        }
        
    }
    
    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }
    
    private void startLoadMore() {
        mPullLoading = true;
        mFooterView.setState(XListViewFooter.STATE_LOADING);
        if (mListViewListener != null) {
            mListViewListener.onLoadMore();
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mHeaderView.getState() == XListViewHeader.STATE_REFRESHING) {
            return true;
        }
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                }
                else if (getLastVisiblePosition() == mTotalItemCount - 1 && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // invoke refresh
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                        mPullRefreshing = true;
                        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
                        if (mListViewListener != null) {
                            mListViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                }else{
                    if(mHeaderView.getVisiableHeight() !=0){
                        mHeaderView.setVisiableHeight(0);
                    }
                }
                
                if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.
                    //				if (mEnablePullLoad
                    //						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
                    //					startLoadMore();
                    //				}
                    checkLoadMore();
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }
    
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            }
            else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }
    
    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }
    
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount > 0&& firstVisibleItem >=getHeaderViewsCount()) {
            refreshPinnerHeader();
        }
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        if (firstVisibleItem + visibleItemCount + 1 >= totalItemCount) {
            checkLoadMore();
        }
    }
    
    public void setXListViewListener(IXListViewListener l) {
        mListViewListener = l;
    }
    
    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }
    
    /**
     * implements this interface to get refresh/load more event.
     */
    public interface IXListViewListener {
        public void onRefresh();
        
        public void onLoadMore();
    }
    
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub
            
        }
    };
    
}
