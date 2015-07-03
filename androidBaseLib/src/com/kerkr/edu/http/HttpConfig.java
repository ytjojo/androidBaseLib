package com.kerkr.edu.http;

public abstract class HttpConfig {
    
    
    public  static String BASE_SERVICE_URL;
    public  static String BASE_TEST_URL;
    public static boolean  DEBUG=true;
    
    public static String getUrl(){
        if(DEBUG){
            return BASE_TEST_URL;
        }
        return BASE_SERVICE_URL;
    }
    
}
