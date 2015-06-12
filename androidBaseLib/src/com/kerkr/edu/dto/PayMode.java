package com.kerkr.edu.dto;

import java.io.Serializable;

public class PayMode implements Serializable {
    /**
     * 注释内容
     */
    public int payModeId;
    
    public String payModeName;
    
    public int getPayModeId() {
        return payModeId;
    }
    
    public void setPayModeId(int payModeId) {
        this.payModeId = payModeId;
    }
    
    public String getPayModeName() {
        return payModeName;
    }
    
    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }
}
