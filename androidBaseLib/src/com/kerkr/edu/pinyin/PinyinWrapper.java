package com.kerkr.edu.pinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.text.TextUtils;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class PinyinWrapper<T extends Comparable<T>> {
    
    /**
     * 获取排序后的新数据
     * 
     * @param persons
     * @return
     */
    private List<String> mGroupList = new ArrayList<String>();
    
    private HashMap<String, List<T>> mHashMap = new HashMap<String, List<T>>();;
    
    private List<List<T>> mChildLists = new ArrayList<List<T>>();
    
    public void sort(List<T> srcList) {
        if (srcList == null || srcList.size() == 0) {
            return;
        }
        
        String compareField = "";
        String headChar;
        IComparablePinyin icomparable = null;
        for (T item : srcList) {
            icomparable = ((IComparablePinyin) item);
            compareField = icomparable.getPinyinField();
            if (!TextUtils.isEmpty(compareField)) {
                headChar = StringUtils.getPinYinHeadChar(compareField).substring(0, 1);
                char ch = headChar.charAt(0);
                headChar = String.valueOf(ch);
                if (Character.isLetter(ch)) {
                    
                }
                else {
                    headChar = "#";
                }
                
            }
            else {
                headChar = "#";
            }
            if (!mGroupList.contains(headChar)) {
                
                mGroupList.add(headChar);
            }
            List<T> childItems = mHashMap.get(headChar);
            if (childItems == null) {
                childItems = new ArrayList<T>();
                mHashMap.put(headChar, childItems);
            }
            childItems.add(item);
        }
        
        Collections.sort(mGroupList);
        
        for (String str : mGroupList) {
            List<T> list = mHashMap.get(str);
            Collections.sort(list);
            mChildLists.add(list);
        }
    }
    
    public List<String> getGroup() {
        return mGroupList;
    }
    
    public List<List<T>> getChildLists() {
        return mChildLists;
    }
    
    public void scrollListView(int group, ExpandableListView listView) {
        long position = listView.getPackedPositionForGroup(group);
        if (((ListView) listView).getHeaderViewsCount() > 0) {// 防止ListView有标题栏，本例中没有。
        
            listView.setSelectionFromTop((int) position + listView.getHeaderViewsCount(), 0);
        }
        else {
            listView.setSelectionFromTop((int) position, 0);// 滑动到第一项
        }
    }
    
    public void deleteItem(int group, int child, ArrayList<ArrayList<T>> list) {
        list.get(group).remove(child);
        if (list.get(group).isEmpty()) {
            list.remove(group);
        }
    }
}
