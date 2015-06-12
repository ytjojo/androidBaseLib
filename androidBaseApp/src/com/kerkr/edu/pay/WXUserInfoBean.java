package com.kerkr.edu.pay;

import java.util.List;

/**
 * Created by ybk on 2015/4/8.
 */
public class WXUserInfoBean {
    
    protected String openid;
    
    protected String nickname;
    
    protected int sex; //2：女性；1:男性
    
    protected String language;
    
    protected String city;
    
    protected String province;
    
    protected String country;
    
    protected String headimgurl;
    
    //微信返回的是List,但是我们上传到服务端的时候需要转换成String上传
    protected List<String> privilege;
    
    protected String unionid;
    
    protected int type;
    
    public String getOpenid() {
        return openid;
    }
    
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public void setSex(int sex) {
        this.sex = sex;
    }
    
    public List<String> getPrivilege() {
        return privilege;
    }
    
    public void setPrivilege(List<String> privilege) {
        this.privilege = privilege;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
    
    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
    
    public String getNickname() {
        
        return nickname;
    }
    
    public int getSex() {
        return sex;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public String getCity() {
        return city;
    }
    
    public String getProvince() {
        return province;
    }
    
    public String getCountry() {
        return country;
    }
    
    public String getHeadimgurl() {
        return headimgurl;
    }
    
    public String getUnionid() {
        return unionid;
    }
    
    @Override
    public String toString() {
        return "WXUserInfoBean{" + "openid='" + openid + '\'' + ", nickname='" + nickname + '\'' + ", sex=" + sex + ", language='" + language + '\''
                + ", city='" + city + '\'' + ", province='" + province + '\'' + ", country='" + country + '\'' + ", headimgurl='" + headimgurl + '\''
                + ", privilege=" + privilege + ", unionid='" + unionid + '\'' + '}';
    }
}
