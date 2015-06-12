/*
 * 文 件 名:  DbHelper.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-5
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.db;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.drjane.promise.model.Alarm;
import com.drjane.promise.model.Order;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-5]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "sqlite-test.db";
    
    private Map<String, Dao> daos = new HashMap<String, Dao>();
    
    private DatabaseHelper(Context context) {
        super(context.getApplicationContext(), TABLE_NAME, null, 4);
    }
    
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Order.class);
            TableUtils.createTable(connectionSource, Alarm.class);
        }
        catch (java.sql.SQLException e) {
            e.printStackTrace();
        }catch (SQLException  e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Order.class, true);
            TableUtils.dropTable(connectionSource, Alarm.class, true);
            onCreate(database, connectionSource);
        }
        catch (java.sql.SQLException e) {
            e.printStackTrace();
        }catch (SQLException  e) {
            e.printStackTrace();
        }
    }
    
    private static DatabaseHelper instance;
    
    /** 
     * 单例获取该Helper 
     *  
     * @param context 
     * @return 
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }
        
        return instance;
    }
    
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        
        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            try {
                dao = super.getDao(clazz);
                daos.put(className, dao);
            }
            catch (java.sql.SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return dao;
    }
    
    /** 
     * 释放资源 
     */
    @Override
    public void close() {
        super.close();
        
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
    
}