package com.kerkr.edu.http;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.kerkr.edu.app.BaseApplication;
import com.kerkr.edu.volley.MultiPartStack;

public class HttpService {
    public static String TAG = HttpService.class.getName();
    
    public static String DEFAULT_HTTP_TAG = "DEFAULT HTTP REQUEST";
    public static String UPLOAD_TAG = "UPLOAD HTTP REQUEST";
    
    public static RequestQueue httpQueue;
    public static RequestQueue uploadFillQueue;
    
    private static HttpService httpService;
    
    private HttpService(Context context) {
        httpQueue = Volley.newRequestQueue(context);
    }
    
    public static HttpService getHttpService() {
        if(httpService == null){
            httpService = new HttpService(BaseApplication.getInstance());
        }
        return httpService;
    }
    
    
    /**
     * 往全局队列里加入一个新的http请求
     * 
     * @param request
     */
    public <T> void addToRequestQueue(Request<T> request) {
        this.addToRequestQueue(request, DEFAULT_HTTP_TAG);
    }
    
    /**
     * 往全局队列里加入一个新的http请求
     * 
     * @param request
     * @param tag
     *            请求tag, 可以通过tag取消请求
     */
    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? DEFAULT_HTTP_TAG : tag);
        VolleyLog.d("Adding request to queue: %s", request.getUrl());
        if (httpQueue == null) {
            
            httpQueue = Volley.newRequestQueue(BaseApplication.getInstance());
        }
        httpQueue.add(request);
        
    }
   
    
    public <T> void addToMultiPartRequestQueue(Request<T> request ,String tag){
        request.setTag(TextUtils.isEmpty(tag) ? UPLOAD_TAG : tag);
        if(uploadFillQueue ==null){
           uploadFillQueue = Volley.newRequestQueue(BaseApplication.getInstance(), new MultiPartStack());
        }
        uploadFillQueue.add(request);
    }
    
    public <T> void addToMultiPartRequestQueue(Request<T> request ){
        addToMultiPartRequestQueue( request ,null);
    }
    
    /**
     * 取消指定tag的请求
     * 
     * @param tag
     */
    public void cancelRequests(Object tag) {
        if (this.httpQueue != null) {
            this.httpQueue.cancelAll(tag);
        }
    }
    
}
