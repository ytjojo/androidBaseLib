package com.kerkr.edu.adapter;

import android.util.SparseArray;
import android.view.View;

/* 
 * @author  lijing
 * @version  [版本号, 2014-6-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ViewHolder {
    // I added a generic return type to reduce the casting noise in client code
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}