/*
 * Copyright (c) 2014.
 * Jackrex
 */

package com.kerkr.edu.http;

import java.util.Map;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.kerkr.edu.String.JsonParser;
import com.kerkr.edu.log.VALog;

//region Description
//<editor-fold desc="Description">

/**
 * Created by Jackrex on 2/18/14.
 */
public class JacksonRequest<T> extends Request<T> {
    private static int SOCKET_TIMEOUT = 10000;
    
    private final Class<T> clazz;
    
    private final Map<String, String> headers;
    
    private final Response.Listener<T> listener;
    
    private String url;
    
    private Map<String, String> params;
    
    private int mTaskId;
    
    private int mRetryCount;
    
    /**
     * 带参数 带头(Header)的 GET POST 请求
     * 
     * @param method
     * @param url
     * @param clazz
     * @param headers
     * @param params
     * @param listener
     * @param errorListener
     */
    public JacksonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Map<String, String> params,
            Response.Listener<T> listener, Response.ErrorListener errorListener ,int retryCount) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.url = url;
        this.params = params;
        this.mRetryCount = retryCount;
        this.setShouldCache(false);
        this.setRetryPolicy(getRetryPolicy());
    }
    
    /**
     * 不带参数的请求 GET POST
     * 
     * @param method
     * @param url
     * @param clazz
     * @param listener
     * @param errorListener
     */
    public JacksonRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, url, null, null, listener, errorListener,1);
    }
    
    /**
     * 带参数的请求 GET POST
     * 
     * @param method
     *            GET POST
     * @param url
     * @param clazz
     * @param params
     *            带参数的请求
     * @param listener
     * @param errorListener
     */
    public JacksonRequest(int method, String url, Class<T> clazz, Map<String, String> params, Response.Listener<T> listener,
            Response.ErrorListener errorListener,int retryCount) {
        this(method, url, clazz, null, params, listener, errorListener,retryCount);
        
    }
    
    /**
     * 不带参数默认GET 请求方法
     * 
     * @param url
     *            传入url http://.... 格式错误可能报NULLPointer
     * @param clazz
     *            需要转化的实体类
     * @param listener
     *            传入成功监听listener
     * @param errorListener
     *            失败listener
     */
    public JacksonRequest(String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, clazz, null, null, listener, errorListener,1);
    }
    
    // default for POST PUT
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null ? params : super.getParams();
    }
    
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }
    
    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
    
    /**
     * 处理网络返回
     * 
     * @param response
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//            VALog.i("response json: ==  " + json + " statusCode" + response.statusCode);
            VALog.i("response json: ==  " + json + " statusCode" );
            
            T obj = JsonParser.getInstance().getObjectFromJson(json, clazz);
            
            return Response.success(obj, HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
        
    }
    
    @Override
    public RetryPolicy getRetryPolicy() {
        RetryPolicy retryPolicy = new DefaultRetryPolicy(SOCKET_TIMEOUT,
                mRetryCount, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return retryPolicy;
    }
    
    // @Override
    // public Map<String, String> getHeaders() throws AuthFailureError
    // {
    // Map<String, String> headers = new HashMap<String, String>();
    // headers.put("Charset", "UTF-8");
    // headers.put("Content-Type", "application/x-javascript");
    // headers.put("Accept-Encoding", "gzip,deflate");
    // return headers;
    // }
    
}
