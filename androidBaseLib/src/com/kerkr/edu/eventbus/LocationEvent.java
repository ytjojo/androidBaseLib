package com.kerkr.edu.eventbus;

import com.baidu.location.BDLocation;

public class LocationEvent {
    public BDLocation location;
    
    public LocationEvent(BDLocation l) {
        this.location = l;
    }
}
