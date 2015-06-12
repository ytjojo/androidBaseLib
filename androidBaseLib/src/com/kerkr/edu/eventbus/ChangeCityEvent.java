package com.kerkr.edu.eventbus;

import com.kerkr.edu.dto.CityInfo;


public class ChangeCityEvent {
    public CityInfo cityInfo;
    
    public ChangeCityEvent(CityInfo info) {
        this.cityInfo = info;
    }
}
