package com.kerkr.edu.pay;

/**
 * Created by ybk on 2015/4/8.
 * 调用接口：
 https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
 返回：
 {
 "access_token":"ACCESS_TOKEN", 
 "expires_in":7200, 
 "refresh_token":"REFRESH_TOKEN",
 "openid":"OPENID", 
 "scope":"SCOPE" 
 }
 */
public class WXCodeResponseBean {
    private String access_token;
    
    private int expires_in;
    
    private String refresh_token;
    
    private String openid;
    
    private String scope;
    
    public String getAccess_token() {
        return access_token;
    }
    
    public int getExpires_in() {
        return expires_in;
    }
    
    public String getRefresh_token() {
        return refresh_token;
    }
    
    public String getOpenid() {
        return openid;
    }
    
    public String getScope() {
        return scope;
    }
    
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
    
    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
    
    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
    
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
    
}
