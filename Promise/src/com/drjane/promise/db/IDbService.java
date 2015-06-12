/*
 * 文 件 名:  IDbService.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-5
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.db;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-5]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IDbService<T> {
    
    boolean save(T model);
    
    boolean delete(T model);
    
    boolean update(T model);
    
    T find(long id);
    
    T findFirst();
    
    T findLast();
    
    List<T> findByIds(int... id);
    
    List<T> queryAll();
    
    List<T> query(String[] sections, String[] args);
    
    List<T> rawQuery(String[] sections, String[] args);
    
    boolean saveIfNotExists(T model);//如果不存在则插入
    
    boolean saveOrUpdate(T model);
    
}
