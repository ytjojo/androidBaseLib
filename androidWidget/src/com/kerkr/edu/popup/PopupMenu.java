package com.kerkr.edu.popup;

import java.util.ArrayList;
import java.util.List;

import com.kerkr.edu.utill.DensityUtil;
import com.kerkr.edu.utill.ScreenUtils;
import com.ytjojo.widget.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.ListPopupWindow;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopupMenu {
    
    private Activity mContext;
    
    private LayoutInflater mInflater;
    
    private WindowManager mWindowManager;
    
    private PopupWindow mPopupWindow;
    
    private LinearLayout mContentView;
    
    private ListView mItemsView;
    
    private TextView mHeaderTitleView;
    
    private OnItemSelectedListener mListener;
    
    private int width;
    
    private List<MenuItem> mItems;
    
    private int mWidth = 240;
    
    private float mScale;
    
    private Rect mPaddingRect;
    
    public PopupMenu(Activity context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mScale = metrics.scaledDensity;
        mItems = new ArrayList<MenuItem>();
        int left = DensityUtil.dip2px(context, 2);
        int top = DensityUtil.dip2px(context, 8);
        int right = DensityUtil.dip2px(context, 2);
        int bottom = DensityUtil.dip2px(context, 2);
        mPaddingRect = new Rect(left, top, right, bottom);
        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setTouchInterceptor(new OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        
        setContentView(new LinearLayout(context));
    }
    
    /**
     * Sets the popup's content.
     *
     * @param contentView
     */
    private void setContentView(LinearLayout contentView) {
        mContentView = contentView;
        mItemsView = new ListView(mContext);
        mItemsView.setDivider(null);
        mItemsView.setCacheColorHint(Color.TRANSPARENT);
        mItemsView.setFadingEdgeLength(0);
        mItemsView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,LinearLayout.LayoutParams.WRAP_CONTENT);
        mContentView.addView(mItemsView, params);
        
        //        mHeaderTitleView = (TextView) contentView.findViewById(R.id.header_title);
        
        mPopupWindow.setContentView(contentView);
    }
    
    public void popUpMyOverflow(PopupWindow popWind, View parentView) {
        /**
         * 定位PopupWindow，让它恰好显示在Action Bar的下方。 通过设置Gravity，确定PopupWindow的大致位置。
         * 首先获得状态栏的高度，再获取Action bar的高度，这两者相加设置y方向的offset样PopupWindow就显示在action
         * bar的下方了。 通过dp计算出px，就可以在不同密度屏幕统一X方向的offset.但是要注意不要让背景阴影大于所设置的offset，
         * 否则阴影的宽度为offset.
         */
        // 获取状态栏高度
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        //        状态栏高度：frame.top
        int actionbarheight = ScreenUtils.getActionbarHeight();
        int yOffset = frame.top + actionbarheight - 25;//减去阴影宽度，适配UI.
        int xOffset = DensityUtil.dip2px(mContext, 5); //设置x方向offset为5dp
        
        popWind.showAtLocation(parentView, Gravity.RIGHT | Gravity.TOP, xOffset, yOffset);
    }
    
    /**
     * Add menu item.
     *
     * @param itemId
     * @param titleRes
     * @param iconRes
     *
     * @return item
     */
    public MenuItem add(int itemId, int titleRes) {
        MenuItem item = new MenuItem();
        item.setItemId(itemId);
        item.setTitle(mContext.getString(titleRes));
        mItems.add(item);
        
        return item;
    }
    
    /**
     * Show popup menu.
     */
    public void show() {
        show(null);
    }
    
    /**
     * Show popup menu.
     *
     * @param anchor
     */
    public void show(View anchor) {
        
        if (mItems.size() == 0) {
            throw new IllegalStateException("PopupMenu#add was not called with a menu item to display.");
        }
        
        preShow();
        
        MenuItemAdapter adapter = new MenuItemAdapter(mContext, mItems);
        mItemsView.setAdapter(adapter);
        mItemsView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onItemSelected(mItems.get(position));
                }
                mPopupWindow.dismiss();
            }
        });
        
        if (anchor == null) {
            View parent = ((Activity) mContext).getWindow().getDecorView();
            mPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            return;
        }
        
        int xPos, yPos;
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        
        Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[0] + anchor.getHeight());
        
        mContentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mContentView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        
        int rootHeight = mContentView.getMeasuredHeight();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        
        // Set x-coordinate to display the popup menu
        xPos = anchorRect.centerX() - mPopupWindow.getWidth() / 2;
        
        int dyTop = anchorRect.top;
        int dyBottom = screenHeight + rootHeight;
        boolean onTop = dyTop > dyBottom;
        
        // Set y-coordinate to display the popup menu
        if (onTop) {
            yPos = anchorRect.top - rootHeight;
        }
        else {
            if (anchorRect.bottom > dyTop) {
                yPos = anchorRect.bottom - 20;
            }
            else {
                yPos = anchorRect.top - anchorRect.bottom + 50;
            }
        }
        
        mPopupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
    }
    
    private void preShow() {
        int width = (int) (mWidth * mScale);
        mPopupWindow.setWidth(width);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.attr.colorPrimary)));
    }
    
    /**
     * Dismiss the popup menu.
     */
    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
    
    /**
     * Sets the popup menu header's title.
     *
     * @param title
     */
    public void setHeaderTitle(CharSequence title) {
        mHeaderTitleView.setText(title);
        mHeaderTitleView.setVisibility(View.VISIBLE);
        mHeaderTitleView.requestFocus();
    }
    
    /**
     * Change the popup's width.
     *
     * @param width
     */
    public void setWidth(int width) {
        mWidth = width;
    }
    
    /**
     * Register a callback to be invoked when an item in this PopupMenu has
     * been selected.
     *
     * @param listener
     */
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mListener = listener;
    }
    
    /**
     * Interface definition for a callback to be invoked when
     * an item in this PopupMenu has been selected.
     */
    public interface OnItemSelectedListener {
        public void onItemSelected(MenuItem item);
    }
    
    static class ViewHolder {
        ImageView icon;
        
        TextView title;
    }
    
    private class MenuItemAdapter extends ArrayAdapter<MenuItem> {
        
        public MenuItemAdapter(Context context, List<MenuItem> objects) {
            super(context, 0, objects);
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.popup_list_item, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            MenuItem item = getItem(position);
            if (item.getIcon() != null) {
                holder.icon.setImageDrawable(item.getIcon());
                holder.icon.setVisibility(View.VISIBLE);
            }
            else {
                holder.icon.setVisibility(View.GONE);
            }
            if (position == getCount() - 1) {
                convertView.findViewById(R.id.divider).setVisibility(View.GONE);
            }
            holder.title.setText(item.getTitle());
            
            return convertView;
        }
    }
}