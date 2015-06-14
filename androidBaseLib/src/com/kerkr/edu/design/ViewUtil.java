/*
 * 文 件 名:  ViewUtil.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-5-12
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.kerkr.edu.design;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.kerkr.edu.resource.ResourceHelper;

import android.annotation.TargetApi;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-5-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ViewUtil {
    
    /**
     * get ListView height according to every children
     * 
     * @param view
     * @return
     */
    public static int getListViewHeightBasedOnChildren(ListView view) {
        int height = getAbsListViewHeightBasedOnChildren(view);
        ListAdapter adapter;
        int adapterCount;
        if (view != null && (adapter = view.getAdapter()) != null && (adapterCount = adapter.getCount()) > 0) {
            height += view.getDividerHeight() * (adapterCount - 1);
        }
        return height;
    }
    
    /**
    * get GridView height according to every children
    *
    * @param view
    * @return
    */
    public static int getGridViewHeightBasedOnChildren(GridView view) {
        int height = getAbsListViewHeightBasedOnChildren(view);
        ListAdapter adapter;
        int adapterCount, numColumns = getGridViewNumColumns(view);
        if (view != null && (adapter = view.getAdapter()) != null && (adapterCount = adapter.getCount()) > 0 && numColumns > 0) {
            int rowCount = (int) Math.ceil(adapterCount / (double) numColumns);
            height = rowCount * (height / adapterCount + getGridViewVerticalSpacing(view));
        }
        return height;
    }
    
    /**
    * get GridView columns number
    *
    * @param view
    * @return
    */
    public static int getGridViewNumColumns(GridView view) {
        if (view == null || view.getChildCount() <= 0) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return getNumColumnsCompat11(view);
            
        }
        else {
            int columns = 0;
            int children = view.getChildCount();
            if (children > 0) {
                int width = view.getChildAt(0).getMeasuredWidth();
                if (width > 0) {
                    columns = view.getWidth() / width;
                }
            }
            return columns;
        }
    }
    
    @TargetApi(11)
    private static int getNumColumnsCompat11(GridView view) {
        return view.getNumColumns();
    }
    
    private static final String CLASS_NAME_GRID_VIEW = "android.widget.GridView";
    
    private static final String FIELD_NAME_VERTICAL_SPACING = "mVerticalSpacing";
    
    /**
     * get GridView vertical spacing
     * 
     * @param view
     * @return
     */
    public static int getGridViewVerticalSpacing(GridView view) {
        // get mVerticalSpacing by android.widget.GridView
        Class<?> demo = null;
        int verticalSpacing = 0;
        try {
            demo = Class.forName(CLASS_NAME_GRID_VIEW);
            Field field = demo.getDeclaredField(FIELD_NAME_VERTICAL_SPACING);
            field.setAccessible(true);
            verticalSpacing = (Integer) field.get(view);
            return verticalSpacing;
        }
        catch (Exception e) {
            /**
             * accept all exception, include ClassNotFoundException, NoSuchFieldException, InstantiationException,
             * IllegalArgumentException, IllegalAccessException, NullPointException
             */
            e.printStackTrace();
        }
        return verticalSpacing;
    }
    
    /**
     * get AbsListView height according to every children
     * 
     * @param view
     * @return
     */
    public static int getAbsListViewHeightBasedOnChildren(AbsListView view) {
        ListAdapter adapter;
        if (view == null || (adapter = view.getAdapter()) == null) {
            return 0;
        }
        
        int height = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, view);
            if (item instanceof ViewGroup) {
                item.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            item.measure(0, 0);
            height += item.getMeasuredHeight();
        }
        height += view.getPaddingTop() + view.getPaddingBottom();
        return height;
    }
    
    /**
     * set view height
     * 
     * @param view
     * @param height
     */
    public static void setViewHeight(View view, int height) {
        if (view == null) {
            return;
        }
        
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
    }
    
    // /**
    // * set GistView height which is calculated by {@link # getGridViewHeightBasedOnChildren(GridView)}
    // *
    // * @param view
    // * @return
    // */
    // public static void setGridViewHeightBasedOnChildren(GridView view) {
    // setViewHeight(view, getGridViewHeightBasedOnChildren(view));
    // }
    
    /**
     * set ListView height which is calculated by {@link # getListViewHeightBasedOnChildren(ListView)}
     * 
     * @param view
     * @return
     */
    public static void setListViewHeightBasedOnChildren(ListView view) {
        setViewHeight(view, getListViewHeightBasedOnChildren(view));
    }
    
    /**
     * set AbsListView height which is calculated by {@link # getAbsListViewHeightBasedOnChildren(AbsListView)}
     * 
     * @param view
     * @return
     */
    public static void setAbsListViewHeightBasedOnChildren(AbsListView view) {
        setViewHeight(view, getAbsListViewHeightBasedOnChildren(view));
    }
    
    /**
     * set SearchView OnClickListener
     * 
     * @param v
     * @param listener
     */
    public static void setSearchViewOnClickListener(View v, OnClickListener listener) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = group.getChildAt(i);
                if (child instanceof LinearLayout || child instanceof RelativeLayout) {
                    setSearchViewOnClickListener(child, listener);
                }
                
                if (child instanceof TextView) {
                    TextView text = (TextView) child;
                    text.setFocusable(false);
                }
                child.setOnClickListener(listener);
            }
        }
    }
    
    /**
     * get descended views from parent.
     * 
     * @param parent
     * @param filter Type of views which will be returned.
     * @param includeSubClass Whether returned list will include views which are subclass of filter or not.
     * @return
     */
    public static <T extends View> List<T> getDescendants(ViewGroup parent, Class<T> filter, boolean includeSubClass) {
        List<T> descendedViewList = new ArrayList<T>();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            Class<? extends View> childsClass = child.getClass();
            if ((includeSubClass && filter.isAssignableFrom(childsClass)) || (!includeSubClass && childsClass == filter)) {
                descendedViewList.add(filter.cast(child));
            }
            if (child instanceof ViewGroup) {
                descendedViewList.addAll(getDescendants((ViewGroup) child, filter, includeSubClass));
            }
        }
        return descendedViewList;
    }
    
    // 不改变控件位置，修改控件大小
    public static void changeWH(View v, int W, int H) {
        LayoutParams params = (LayoutParams) v.getLayoutParams();
        params.width = W;
        params.height = H;
        v.setLayoutParams(params);
    }
    
    // 修改控件的高
    public static void changeH(View v, int H) {
        LayoutParams params = (LayoutParams) v.getLayoutParams();
        params.height = H;
        v.setLayoutParams(params);
    }
    
    public static Drawable changeDrawableColor(int resourceId, int newImageColor) {
        Drawable drawable = ResourceHelper.getDrawable(resourceId);
        drawable.setColorFilter(newImageColor, Mode.MULTIPLY);
        return drawable;
    }
    
    /**
     * 让一个UI控件永久获得焦点
     * @param view  
     */
    public void getFouces(View view) {
        view.setFocusable(true);
        view.requestFocus();
        view.setFocusableInTouchMode(true);
    }
    
    /**
     * 让EditText中的英文字母自动大写   只是看起来大写了，但实际获取的字符需要toUppCase()转换一下大写
     * @param et
     */
    public void autoToUppCase(EditText et) {
        et.setTransformationMethod(new AllCapTransformationMethod());
    }
    
    private static class AllCapTransformationMethod extends ReplacementTransformationMethod {
        @Override
        protected char[] getOriginal() {
            char[] aa = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                    'y', 'z' };
            return aa;
        }
        
        @Override
        protected char[] getReplacement() {
            char[] cc = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                    'Y', 'Z' };
            return cc;
        }
    }
}
