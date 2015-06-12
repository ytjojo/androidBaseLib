package com.kerkr.edu.adapter;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public abstract class AppBaseExpandAdapter<T1, T2> extends
		BaseExpandableListAdapter
{
	private List<T1>		mGroupList;
	private List<List<T2>>	mChildList;
	private int				mGroupLayoutId;
	private int				mChildLayoutId;
	public Context			mContext;
	private LayoutInflater	mInflater;

	public AppBaseExpandAdapter(Context c, int groupLayoutId,
			int childLayoutId, List<T1> groups, List<List<T2>> childs)
	{
		this.mGroupLayoutId = groupLayoutId;
		this.mChildLayoutId = childLayoutId;
		this.mContext = c;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mGroupList = groups;
		this.mChildList = childs;
	}

	public void init(Context c, int groupLayoutId, int childLayoutId)
	{
		this.mGroupLayoutId = groupLayoutId;
		this.mChildLayoutId = childLayoutId;
		this.mContext = c;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData(List<T1> groups, List<List<T2>> childs)
	{
		this.mGroupList = groups;
		this.mChildList = childs;
	}

	@Override
	public int getGroupCount()
	{
		return mGroupList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		// TODO Auto-generated method stub
		return mChildList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		// TODO Auto-generated method stub
		return mGroupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return mChildList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + groupPosition;
		result = prime * result + childPosition;
		return result;
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(mGroupLayoutId, parent, false);
		}
		T1 groupItem = mGroupList.get(groupPosition);
		bindGroupDataView(groupPosition, isExpanded, convertView, parent,
				groupItem);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent)
	{

		if (convertView == null)
		{
			convertView = mInflater.inflate(mChildLayoutId, parent, false);

		}
		T2 childItem = mChildList.get(groupPosition).get(childPosition);
		bindChildDataView(groupPosition, childPosition, isLastChild,
				convertView, parent, childItem);

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return true;
	}
	
	public abstract void bindGroupDataView(int groupPosition,
			boolean isExpanded, View convertView, ViewGroup parent, T1 groupItem);

	public abstract void bindChildDataView(int groupPosition,
			int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent, T2 childItem);
}
