package com.kerkr.edu.eventbus;

/**
 * Created by Hyr on 2015/2/12.
 * Desc:
 */
public class WxPayEvent
{
    public long preorderId;
    public boolean isSuccess;
    public WxPayEvent(long id,boolean isSuccess) {
        this.preorderId = id;
        this.isSuccess=isSuccess;
    }
}
