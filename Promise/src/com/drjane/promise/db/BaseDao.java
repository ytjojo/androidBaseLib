/*
 * 文 件 名:  BaseDao.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-8
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.db;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.j256.ormlite.dao.Dao;

import android.content.Context;
import android.database.SQLException;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-8]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class BaseDao<T> implements IDbService<T> {
    private Context mContext;
    
    private Dao<T, Integer> userDaoOpe;
    
    private DatabaseHelper helper;
    
    public BaseDao(Context context) {
        this.mContext = context.getApplicationContext();
        try {
            helper = DatabaseHelper.getHelper(mContext);
            userDaoOpe = helper.getDao(getTypeClass());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public abstract Class getGenericClass();
    public Class getTypeClass() {
        Class clazz = null;
        while (clazz != Object.class) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    clazz = (Class<T>) args[0];
                    break;
                }
            }
            clazz = clazz.getSuperclass();
            
        }
        return clazz;
    }
}
