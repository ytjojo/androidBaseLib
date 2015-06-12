package com.kerkr.edu.http;

import java.util.HashMap;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.kerkr.edu.utill.JsonParser;

public class VolleyErrorHelper {
     
  public static String getMessage(Object error, Context context) {
      if (error instanceof TimeoutError) {
          return "服务器连接超时";
      }
      else if (isServerProblem(error)) {
          return handleServerError(error, context);
      }
      else if (isNetworkProblem(error)) {
          return "无网络连接";
      }
      return "网络错误";
  }
  
 
  private static boolean isNetworkProblem(Object error) {
      return (error instanceof NetworkError) || (error instanceof NoConnectionError);
  }
 
  private static boolean isServerProblem(Object error) {
      return (error instanceof ServerError) || (error instanceof AuthFailureError);
  }
 
  private static String handleServerError(Object err, Context context) {
      VolleyError error = (VolleyError) err;
  
      NetworkResponse response = error.networkResponse;
  
      if (response != null) {
          switch (response.statusCode) {
            case 404:
            case 422:
            case 401:
                try {
                    // server might return error like this { "error": "Some error occured" }
                    // Use "Gson" to parse the result

                    
                    HashMap<String,String> result = JsonParser.getInstance().getObjectFromJson(new String(response.data), HashMap.class);
                    if (result != null && result.containsKey("error")) {
                        return result.get("error");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // invalid request
                return error.getMessage();

            default:
                return "服务器连接超时";
            }
      }
        return "网络错误";
  }
}