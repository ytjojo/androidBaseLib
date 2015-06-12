package com.kerkr.edu.dto;

import com.kerkr.edu.app.Constans;

public class CacheData {
    private String _cookie;
    
    private String _uuid;
    
    private int _cityId;
    
    private String _currentVersion;
    
    private double _longitude;
    
    private double _latitude;
    
    private String _build;
    
    private String _phone;
    
    private String name;
    
    private String _password;
    
    private String eVip;
    
    //新增微信的unionid
    private String _unionid;
    
    
    
    public void setUnionid(String unionid) {
        this._unionid = unionid;
    }
    
    public String getUnionid() {
        return _unionid;
    }
    
    public CacheData() {
        _cookie = "";
        _uuid = "";
        _cityId = Constans.DEFAULT_CITY_ID;
        _currentVersion = "";
        _longitude = 0.0;
        _latitude = 0.0;
        eVip = "";
        _phone = "";
        _unionid = "";
    }
    
    public String getBuild() {
        return _build;
    }
    
    public void setBuild(String build) {
        _build = build;
    }
    
    public String getCookie() {
        return _cookie;
    }
    
    public void setCookie(final String cookie) {
        _cookie = cookie;
    }
    
    public String getUUID() {
        return _uuid;
    }
    
    public void setUUID(final String uuid) {
        _uuid = uuid;
    }
    
    public int getCityId() {
        return _cityId;
    }
    
    public void setCityId(int cityId) {
        _cityId = cityId;
    }
    
    public void SetCurrentVersion(final String currentVersion) {
        _currentVersion = currentVersion;
    }
    
    public String getCurrentVersion() {
        return _currentVersion;
    }
    
    public void setLonitude(double lonti) {
        _longitude = lonti;
    }
    
    public double getLongitude() {
        return _longitude;
    }
    
    public double getLatitude() {
        return _latitude;
    }
    
    public void setLatitude(double latitude) {
        _latitude = latitude;
    }
    
    public String getPhone() {
        return _phone;
    }
    
    public void setPhone(String phone) {
        _phone = phone;
    }
    
    public String getPassword() {
        return _password;
    }
    
    public void setPassword(String pwd) {
        _password = pwd;
    }
    
    public String geteVip() {
        return eVip;
    }
    
    public void seteVip(String eVip) {
        this.eVip = eVip;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "VACacheData{" + "_cookie='" + _cookie + '\'' + ", _uuid='" + _uuid + '\'' + ", _cityId=" + _cityId + ", _currentVersion='"
                + _currentVersion + '\'' + ", _longtitude=" + _longitude + ", _latitude=" + _latitude + ", _build='" + _build + '\'' + ", _phone='"
                + _phone + '\'' + ", name='" + name + '\'' + ", _password='" + _password + '\'' + ", eVip='" + eVip + '\'' + ", _unionid='"
                + _unionid + '\'' + '}';
    }
}