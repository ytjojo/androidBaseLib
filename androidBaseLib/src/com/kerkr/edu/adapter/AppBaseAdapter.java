package com.kerkr.edu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AppBaseAdapter<T> extends BaseAdapter {
    protected List<T> mData;
    
    protected Context context;
    
    int mLayoutResource = -1;
    
    public AppBaseAdapter(Context c) {
        this.context = c;
    }
    
    public AppBaseAdapter(Context c, List<T> list) {
        this.context = c;
        this.mData = list;
    }
    
    public AppBaseAdapter(Context c, List<T> list, int layoutRecource) {
        this.context = c;
        this.mData = list;
        this.mLayoutResource = layoutRecource;
    }
    
    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }
    
    /**
     *
     * @return
     */
    public int getItemLayoutRes() {
        return -1;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemResource = mLayoutResource;
        if (itemResource <= 0) {
            itemResource = getItemLayoutRes();
        }
        if (convertView == null && itemResource > 0) {
            convertView = LayoutInflater.from(context).inflate(itemResource, parent, false);
        }
        bindViewData(position, convertView, parent);
        return convertView;
    }
    
    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @param holder
     * @return
     */
    public abstract View bindViewData(int position, View convertView, ViewGroup parent);
    
    /**
     * 只填充数据
     */
    public void setDataOnly(List<T> list) {
        this.mData = list;
    }
    
    public List<T> getData() {
        return mData;
    }
    
    //setdata notify个毛啊
    public void setData(List<T> list) {
        this.mData = list;
        notifyDataSetChanged();
    }
    
}
