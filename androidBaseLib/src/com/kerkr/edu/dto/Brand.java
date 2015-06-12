package com.kerkr.edu.dto;

import java.io.Serializable;
import java.util.List;

public class Brand implements Serializable {
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    public int companyId;
    
    public String name;
    
    public String logoUrlString;
    
    public String description;
    
    public String defaultMenuUrl;
    
    public boolean isFavorite;
    
    public boolean supportPrePayCashBack;
    
    public boolean supportPrePayVIPEntrance;
    
    public boolean supportPrePayGift;
    
    
    public long numberOfPreorders;//手机点餐次数
    
    public long numberOfPrepays;//手机支付次数
    
    public double acpp;//Average Cost Per Person 人均消费
    
    public double nearDistance = 1000000000000.0;
    
    public int userCompletedOrderCount = 0;
    
    public VipPolicy userPolicy;
    
    public List<VipPolicy> vipPolicies;
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        Brand brand = new Brand();
        brand.acpp = this.acpp;
        brand.companyId = this.companyId;
        
        return brand;
    }
    
}