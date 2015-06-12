package com.kerkr.edu.manager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.SystemClock;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.kerkr.edu.app.BaseApplication;
import com.kerkr.edu.app.Constans;
import com.kerkr.edu.cache.CacheManager;
import com.kerkr.edu.dto.CityInfo;
import com.kerkr.edu.dto.PrinvinceInfo;
import com.kerkr.edu.eventbus.ChangeCityEvent;
import com.kerkr.edu.eventbus.DefaultEvent;
import com.kerkr.edu.eventbus.LocationEvent;

import de.greenrobot.event.EventBus;

public class LocationManager {
    public static final int NEW_LOCATION_DELAY = 120;// 秒
    
    
    // 记录的上次程序经纬度
    public double lastLatitude = 0;
    
    public double lastLongitude = 0;
    
    public BDLocation mCurrentLocation = null;
    public CityInfo  currentLocationCity;
    public  List<PrinvinceInfo> mStateInfo = null;
    
    public  boolean isSucceedLocation;
    
    private long mLastGetLocationTime;
    
    private String currentAddress;
    
    public  String getAdd() {
        
        return currentAddress;
    }
    
    private LocationClient mLocationClient;
    
    private MyLocationListenner myLocationLisner;
    
    private static LocationManager instance;
    
    public static LocationManager getInstance() {
        if (instance == null) {
            synchronized (LocationManager.class) {
                if (instance == null) {
                    instance = new LocationManager();
                }
            }
        }
        return instance;
    }
    
    private LocationManager() {
        init();
    }
    
    private void init() {
        mLocationClient = new LocationClient(BaseApplication.getInstance());
        myLocationLisner = new MyLocationListenner();
        mLocationClient.registerLocationListener(myLocationLisner);
        mLocationClient.setLocOption(getLocationOpriton());
        EventBus.getDefault().registerSticky(this);
    }
    
    private LocationClientOption getLocationOpriton() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setScanSpan(1000);// 设置定位时间间隔
        option.setIsNeedAddress(true);// 是否反地理编码
        option.setOpenGps(true); // 是否打开GPS
        option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
        // option.setCoorType("gcj02");
        
        // option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
        // option.setScanSpan(VAConst.UPDATE_TIME); // 设置定时定位的时间间隔。单位毫秒
        // option.setAddrType("all");
        // option.setServiceName("com.baidu.location.service_v2.9");
        // option.setPoiExtraInfo(true);
        // option.disableCache(true);
        
        return option;
    }
    
    public void startLocation() {
        mLocationClient.start();
        mLocationClient.requestLocation();
    }
    
    public void startLocationWithSaveLocation() {
        // 14.7.17 增加保存上一次的经纬度
        if (mCurrentLocation != null) {
            lastLatitude = mCurrentLocation.getLatitude();
            lastLongitude = mCurrentLocation.getLongitude();
        }
        mLocationClient.start();
        mLocationClient.requestLocation();
    }
    
    public void stopLocation() {
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }
    
    public boolean isNeadStartLocation() {
        if (mLastGetLocationTime <= 0) {
            return true;
        }
        long curTime = SystemClock.elapsedRealtime();
        if (curTime - mLastGetLocationTime >= NEW_LOCATION_DELAY * 1000) {
            return true;
        }
        return false;
    }
    
    /**
     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListenner implements BDLocationListener {
        
        @Override
        public void onReceiveLocation(BDLocation location) {
            
            if (location == null || TextUtils.isEmpty(location.getCity()) || TextUtils.isEmpty(location.getProvince())) // 無法定位
            {
                // 无法定位时，不从新赋值cityID
                // mCacheData.setCityId(mCacheData.get);
                isSucceedLocation = false;
                EventBus.getDefault().post(new com.kerkr.edu.eventbus.LocationEvent(null));
            }
            else {
                stopLocation();
                // 定位成功 currentLocationCity = cityInfo;
                
                mLastGetLocationTime = SystemClock.elapsedRealtime();
                isSucceedLocation = true;
                mCurrentLocation = location;
                currentAddress = location.getAddrStr();
                if (mStateInfo == null) {
                    location = null;
                    // mCacheData.setCityId(VAConst.DEFAULT_CITY_ID);
//                    return;
                }
                else {
                    CacheManager.getInstance().mCacheData.setCityId(getCurrentCityID());
                    CacheManager.getInstance().mCacheData.setLatitude(location.getLatitude());
                    CacheManager.getInstance().mCacheData.setLonitude(location.getLongitude());
                    
                    CacheManager.getInstance().save();
                }
                EventBus.getDefault().post(new LocationEvent(location));
             
            }
        }
        
        // public void onReceivePoi(BDLocation poiLocation) {
        // // 不需要接受POI
        // }
        
    }
    
    // eventbus必须有响应event 的方法，这个是默认的无实际功能
    private void onEventMainThread(DefaultEvent e) {
    }
    
    /*
     * 根据LOCATION信息拿到城市ID；
     */
    public  int getCurrentCityID() {
        if (!isValidStateInfo() || mCurrentLocation == null) {
            // 定位失败返回默认杭州的cityID
        }
        
        String province = mCurrentLocation.getProvince();
        String city = mCurrentLocation.getCity();
        for (int i = 0; i < mStateInfo.size(); i++) {
            PrinvinceInfo stateInfo = mStateInfo.get(i);
            // 定位的省份开通了悠先
            if (stateInfo.stateName.equals(province)) {
                for (int j = 0; j < stateInfo.onlineCityList.size(); j++) {
                    CityInfo cityInfo = stateInfo.onlineCityList.get(j);
                    if (cityInfo.cityName.equals(city)) {// 定位的城市开通了悠先
                                                         // 成功定位了，如果和上次的不一致，弹出切换提示框
                        if (   CacheManager.getInstance().mCacheData.getCityId() != cityInfo.cityId) {
                            
                            // 记录当前定位到的城市,广场中要用
                            currentLocationCity = cityInfo;
                            
                            // 只有当前页面是首页时，才弹出提示框切换城市
//                            if (VAAppAplication.currentSelectBottomBtnId == 1) {
//                                EventBus.getDefault().postSticky(new ChangeCityEvent(cityInfo));
//                            }
                        }
                        return    CacheManager.getInstance().mCacheData.getCityId();// cityInfo.cityId;
                    }
                    else {// 城市未开通
                        return    CacheManager.getInstance().mCacheData.getCityId();
                    }
                }
                
                for (int j = 0; j < stateInfo.offlineCityList.size(); j++) {
                    CityInfo cityInfo = stateInfo.offlineCityList.get(j);
                    if (cityInfo.cityName.equals(city)) {
                        return Constans.DEFAULT_CITY_ID;
                    }
                }
            }
        }
        return    CacheManager.getInstance().mCacheData.getCityId();
    }
    
    // 根据城市id找名字
    public String getCityName4Id(int cityId) {
        
        if (isValidStateInfo()) {
            for (PrinvinceInfo stateInfo : mStateInfo) {
                List<CityInfo> cityInfoList = stateInfo.onlineCityList;
                if (null != cityInfoList && !cityInfoList.isEmpty()) {
                    for (CityInfo cityInfo : cityInfoList) {
                        if (cityId == cityInfo.cityId) {
                            return cityInfo.cityName;
                        }
                    }
                }
            }
        }
        return "定位失败";
    }
    
    public  boolean isValidStateInfo() {
        return null != mStateInfo && !mStateInfo.isEmpty();
    }
    
    // ////////////////////////////////////////////////////////////////////////////////////////
    // 定位处理逻辑
    /*
     * 更新地理位置信息，并且将其保存到缓存中
     */
    // public static double getLocationDistance(double lati1, double longi1,
    // double lati2, double longi2) {
    // int lat = (int) (lati1 * 1E6);
    // int lon = (int) (longi1 * 1E6);
    // LatLng pt1 = new GeoPoint(lat, lon);
    //
    // lat = (int) (lati2 * 1E6);
    // lon = (int) (longi2 * 1E6);
    // GeoPoint pt2 = new GeoPoint(lat, lon);
    // // if (VAAppAplication.getInstance().reInitBaiduMap()) {
    // return DistanceUtil.getDistance(pt2, pt1);
    //
    // // } else {
    // // Toast.makeText(VAAppAplication.getInstance(), "定位失败!",
    // // Toast.LENGTH_LONG).show();
    // // return 0;
    // // }
    // }
    
    public static double getLocationDistance(double lati1, double longi1, double lati2, double longi2) {
        // 调用此方法需要在此之前 初始化--SDKInitializer.initialize(this);
        // ---此处已在VAAppplication onCreate()中初始化;
        return DistanceUtil.getDistance(new LatLng(lati1, longi1), new LatLng(lati2, longi2));
    }
    
    
    public List<PrinvinceInfo> getStateInfo() {
        return mStateInfo;
    }
    
    private List<CityInfo> getCityInfoList() {
        List<CityInfo> cityList = new ArrayList<CityInfo>();
        
        if (!isValidStateInfo()) {
            return cityList;
        }
        
        for (PrinvinceInfo stateInfo : mStateInfo) {
            List<CityInfo> cityInfoList = stateInfo.onlineCityList;
            if (null != cityInfoList && !cityInfoList.isEmpty()) {
                for (CityInfo cityInfo : cityInfoList) {
                    cityList.add(cityInfo);
                }
            }
        }
        return cityList;
    }
    
    public static String getDistanceShow(double distance) {
        String pre;
        if (distance > 1000.0) {
            DecimalFormat df = new DecimalFormat();
            String ft = "0.0km";
            df.applyPattern(ft);
            pre = df.format(distance / 1000);
        }
        else {
            DecimalFormat df = new DecimalFormat();
            String ft = "0m";
            df.applyPattern(ft);
            pre = df.format(distance);
        }
        return pre;
    }
    
    @Override
    protected void finalize() throws Throwable {
        EventBus.getDefault().unregister(this);
        super.finalize();
    }
}
