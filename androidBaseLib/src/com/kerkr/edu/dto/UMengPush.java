package com.kerkr.edu.dto;

import java.util.Map;

/**
 * Created by Hyr on 2015/1/14.
 * Desc:通知轮播实体类
 */
public class UMengPush {
    public int index;
    
    public Map<String, String> extra;
    
    public String title;
    
    public String text;
    
    public UMengPush() {
        
    }
    
    public UMengPush(int index, String name) {
        this.text = name;
        this.index = index;
    }
    
}