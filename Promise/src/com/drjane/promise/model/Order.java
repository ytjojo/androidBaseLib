/*
 * 文 件 名:  Order.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-8
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.model;

import java.io.Serializable;

import android.R.id;

import com.j256.ormlite.field.DatabaseField;
import com.kerkr.edu.pinyin.IComparablePinyin;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-8]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Order implements IComparablePinyin,Comparable<Order>,Serializable {
    @DatabaseField(canBeNull = true, foreign = true, columnName = "alarm_id")
    Alarm alarm;
    
    @DatabaseField(generatedId = true)  
    public int id;
    @DatabaseField
    public String objectId;
    @DatabaseField
    public String customerName;
    @DatabaseField
    public String shootingTime;
    @DatabaseField
    public String customerPhoneNumber;
    @DatabaseField
    public String mmId;
    @DatabaseField
    public String remarks;
  
    public String userId;

    /**
     * @return
     */
    @Override
    public String getPinyinField() {
        // TODO Auto-generated method stub
        return customerName;
    }

    /**
     * @param another
     * @return
     */
    @Override
    public int compareTo(Order another) {
        return this.customerName.compareTo(another.customerName);
    }
}
