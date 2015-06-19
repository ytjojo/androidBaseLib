/*
 * 文 件 名:  BaseRequest.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-5-11
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.kerkr.edu.http;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-5-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class BaseRequest implements Serializable{
  public int cityId;
    
    // 请求的时候同时赋值检测用
    public int type;
    
    //设备UUID
    public String uuid;
    
    // 登录用户证书
    public String cookie;
    
    public String clientBuild;//20140604 wangc 客户端当前版本号
    
    public int appType;
   
}
