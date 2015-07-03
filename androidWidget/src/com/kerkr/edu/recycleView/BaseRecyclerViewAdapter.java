package com.kerkr.edu.recycleView;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.VH> {
    public List<T> mData = new ArrayList<T>();
    
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    
    public BaseRecyclerViewAdapter(List<T> list, int layoutId, OnItemClick<T> l) {
        this.mData = list;
        this.mLayoutRes = layoutId;
        this.mOnItemClick = l;
    }
    
    public BaseRecyclerViewAdapter(List<T> list, int layoutId) {
        this.mData = list;
        this.mLayoutRes = layoutId;
    }
    
    public int mLayoutRes;
    
    public static class VH extends RecyclerView.ViewHolder {
        
        SparseArray<View> viewHolder;
        
        public VH(View itemView) {
            super(itemView);
        }
        
        public <V extends View> V get(int id) {
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = itemView.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (V) childView;
        }
    }
    
    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }
    
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int arg1) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mLayoutRes, parent, false);
        VH holder = new VH(v);
        
        return holder;
    }
    
    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        viewHolder.itemView.setActivated(selectedItems.get(position, false));
        if (mOnItemClick != null) {
            viewHolder.itemView.setOnClickListener(new Click(position, mData.get(position)));
            
        }
        bindDataToView(viewHolder, position,mData.get(position));
    }
    
    public abstract void bindDataToView(VH viewHolder, int Postion, T model);
    
    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
        
    }
    
    public void setSelected(int pos) {
        selectedItems.put(pos, true);
        notifyItemChanged(pos);
        
    }
    
    public void clearSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        notifyItemChanged(pos);
    }
    
    public void clearSelections() {
        if (selectedItems.size() > 0) {
            selectedItems.clear();
            notifyDataSetChanged();
        }
    }
    
    public int getSelectedItemCount() {
        return selectedItems.size();
    }
    
    public List<Integer> getSelectedItemsPositions() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }
    
    public void removeItem(int pos) {
        mData.remove(pos);
    }
    
    private OnItemClick<T> mOnItemClick;
    
    public void setOnItemClickListener(OnItemClick<T> l) {
        this.mOnItemClick = l;
    }
    
    private class Click implements View.OnClickListener {
        int mPosition;
        
        T mCurModel;
        
        public Click(int position, T t) {
            this.mCurModel = t;
            this.mPosition = position;
        }
        
        @Override
        public void onClick(View v) {
            if (mOnItemClick != null) {
                mOnItemClick.onItemClick(v, mPosition, mCurModel);
            }
            
        }
    }
    
    public interface OnItemClick<T> {
        public void onItemClick(View v, int position, T item);
    }
}
