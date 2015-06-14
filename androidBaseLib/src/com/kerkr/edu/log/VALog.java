package com.kerkr.edu.log;


import android.util.Log;

public class VALog {
    
    static LogBase myLogger;
    
    public static void setTag(String tag){
    	LogBase.customTagPrefix = tag;
    }
    /**
     * Purpose:Mark user one
     *
     * @return
     */
    public static LogBase getLogger() {
        if (myLogger == null) {
            myLogger = new LogBase("ytjlog");
        }
        return myLogger;
    }
    
    public static void d(Object str) {
        getLogger().d(str);
    }
    
    public static void e(Object str) {
        getLogger().e(str);
    }
    
    public static void e(String log, Throwable tr) {
        getLogger().e(log, tr);
    }
    
    public static void i(Object str) {
        getLogger().i(str);
    }
    public static void w(Object str) {
        getLogger().w(str);
    }
}
