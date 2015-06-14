/*
 * Copyright (c) 2014.
 * Jackrex
 */

package com.kerkr.edu.http;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseBooleanArray;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.kerkr.edu.String.JsonParser;
import com.kerkr.edu.app.Constans;
import com.kerkr.edu.http.request.BaseRequest;
import com.kerkr.edu.http.response.BaseResponse;
import com.kerkr.edu.log.VALog;
import com.kerkr.edu.volley.MultiPartStringRequest;

/**
 * Created by Jackrex on 2/20/14.
 */
public class VolleyHttpClient {
    
    private static final String CLIENT_ID = ""; // replace
                                                // your
                                                // clientid;
    
    private static final String CLIENT_SECRET = "";
    
    public static String host = Constans.SERVICE_URL;
    
    public static String getAbsoluteUrl(String relativeUrl) {
        return host + relativeUrl;
    }
    
    public static void httpPost(String url, Class clazz, Map<String, String> params, Response.Listener listener,
            Response.ErrorListener errorListener, int retryCount) {
        
        final JacksonRequest<BaseResponse> request = new JacksonRequest<BaseResponse>(Method.POST, url, clazz, params, listener, errorListener,
                retryCount);
        HttpService.getHttpService().addToRequestQueue(request);
    }
    
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param taskId
     * @param messageEx
     * @param clazz
     * @param listener [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void httpPost(final int taskId, BaseRequest messageEx, Class clazz, final VolleyListener listener) {
        httpPost(taskId, messageEx, clazz, listener, 1);
    }
    
    public static boolean isTaskRunning(int taskId) {
        return IS_TASK_RUNNING.get(taskId);
    }
    
    public final static SparseBooleanArray IS_TASK_RUNNING = new SparseBooleanArray();
    
    public static void httpPost(final int taskId, BaseRequest messageEx, Class clazz, final VolleyListener listener, int retryCount) {
        IS_TASK_RUNNING.put(taskId, true);
        final Response.Listener<BaseResponse> responseListener = new Response.Listener<BaseResponse>() {
            
            @Override
            public void onResponse(BaseResponse response) {
                IS_TASK_RUNNING.delete(taskId);
                VALog.d("请求成功" + JsonParser.getInstance().getJsonFromObject(response));
                if (listener != null) {
                    
                    //                    listener.OnFinished(response);
                }
            }
        };
        final Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                IS_TASK_RUNNING.delete(taskId);
                VALog.d("请求错误" + error.toString());
                if (listener != null) {
                    listener.OnFinished(null);
                }
            }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("msg", JsonParser.getInstance().getJsonFromObject(messageEx));
        httpPost(getAbsoluteUrl(""), clazz, params, responseListener, errorListener, retryCount);
        VALog.i("http post请求" + JsonParser.getInstance().getJsonFromObject(messageEx));
    }
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param url
     * @param method
     * @param files
     * @param params
     * @param responseListener
     * @param errorListener
     * @param tag
     * @param requestClassType [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void uploadFileStringReq(final String url, int method, final Map<String, File> files, final Map<String, String> params,
            final Listener<String> responseListener, final ErrorListener errorListener, final Object tag) {
        if (null == url || null == responseListener) {
            return;
        }
        MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(method, url, responseListener, errorListener) {
            
            @Override
            public Map<String, File> getFileUploads() {
                return files;
            }
            
            @Override
            public Map<String, String> getStringUploads() {
                return params;
            }
            
        };
        
        HttpService.getHttpService().addToMultiPartRequestQueue(multiPartRequest);
    }
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param url
     * @param method
     * @param files
     * @param params
     * @param listener
     * @param tag [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void uploadFileStringReq(final String url, int method, final Map<String, File> files, final Map<String, String> params,
            final VolleyListener listener, final Object tag) {
        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            
            @Override
            public void onResponse(String response) {
                if (listener != null) {
                    
                    //                    listener.OnFinished(response);
                }
            }
        };
        final Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VALog.d("请求错误" + error.toString());
                if (listener != null) {
                    listener.OnFinished(null);
                }
            }
        };
        uploadFileStringReq(url, method, files, params, responseListener, errorListener, tag);
        
    }
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param header
     * @return [参数说明]
     * 
     * @return HashMap<String,String> [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    
    public HashMap<String, String> setOAuthTokenParams(HashMap<String, String> header ) {

        String accessToken = "";
        header.put("Authorization", "Bearer" + " " + accessToken );
        Base64.encodeToString(
                (CLIENT_ID + ":" + CLIENT_SECRET).getBytes(),
                Base64.NO_WRAP);
       return header;
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param url
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String getCacheData(String url){

        if (HttpService.httpQueue.getCache().get(getAbsoluteUrl(url)) != null) {
            String cacheStr = new String(HttpService.httpQueue.getCache()
                    .get(getAbsoluteUrl(url)).data);
            return cacheStr;
        }
        return "";
    }

}
