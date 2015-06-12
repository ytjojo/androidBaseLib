package com.kerkr.edu.dto;

import java.io.Serializable;

public class VipPolicy implements Serializable {
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    public int requirement;
    
    public int nextRequirement;
    
    public double discount;
    
    public String name;
    
    public long policyId;
}
