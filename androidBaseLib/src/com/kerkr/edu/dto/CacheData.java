package com.kerkr.edu.dto;

import com.kerkr.edu.app.Constans;

public class CacheData {
    private String cookie;
    
    private String uuid;
    
    private int cityId;
    
    private String currentVersion;
    
    private double longitude;
    
    private double latitude;
    
    private String build;
    
    private String phone;
    
    private String name;
    
    private String password;
    
    private String eVip;
    
    //新增微信的unionid
    private String unionid;
    
    
    
    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
    
    public String getUnionid() {
        return unionid;
    }
    
    public CacheData() {
        cookie = "";
        uuid = "";
        cityId = Constans.DEFAULT_CITY_ID;
        currentVersion = "";
        longitude = 0.0;
        latitude = 0.0;
        eVip = "";
        phone = "";
        unionid = "";
    }
    
   
    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String geteVip() {
        return eVip;
    }

    public void seteVip(String eVip) {
        this.eVip = eVip;
    }

    @Override
    public String toString() {
        return "VACacheData{" + "cookie='" + cookie + '\'' + ", uuid='" + uuid + '\'' + ", cityId=" + cityId + ", currentVersion='"
                + currentVersion + '\'' + ", longtitude=" + longitude + ", latitude=" + latitude + ", build='" + build + '\'' + ", phone='"
                + phone + '\'' + ", name='" + name + '\'' + ", password='" + password + '\'' + ", eVip='" + eVip + '\'' + ", unionid='"
                + unionid + '\'' + '}';
    }
}