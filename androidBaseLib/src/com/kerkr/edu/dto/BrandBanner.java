package com.kerkr.edu.dto;
import java.io.Serializable;
import java.util.List;

public class BrandBanner extends Brand implements Serializable {
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    public String bannerImageUrlString;
    
    public List<Integer> Id;
    
    public String bannerName;
    
    // 广告描述
    public String bannerDescript;
    
    /*广告分类
    1为正常广告--代表门店广告，点击后直接进对应门店点菜页面
    2是优惠券广告，这个暂时取消不用了
    3为公司推广广告--是url链接广告，点击后webview打开对应的链接
    4是红包活动，点击后webview打开对应的链接，并拼接相应参数*/
    
    public int bannerType;//广告分类,1为正常广告--进点菜，3为公司推广广告--url 进网页,4---红包--url ,5--专题Banner
    
    public String bannerUrl;//广告url
}