package com.kerkr.edu.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.kerkr.edu.app.BaseApplication;

/**
 * Created by Jackrex on 2/24/14.
 */
public class SharePreferenceService {

    private static SharePreferenceService preferenceStorageService;
    private Context context;

    public SharePreferenceService(Context context) {
        this.context=context;
    }


    public static SharePreferenceService newInstance(Context context){

        if(preferenceStorageService == null){
            preferenceStorageService =  new SharePreferenceService(context);
        }

        return  preferenceStorageService;
    }


    /**
     * 第一次启动的flag
     * @return
     */
    public boolean isFirstLaunch(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  preferences.getBoolean("isfirstlaunch",true);
    }


    public void setFirstLaunch(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("isfirstlaunch", false);
        editor.commit();
    }


    /**
     * 设置某些仅仅使用一次的flag
     * @return
     */
    public boolean isFirst(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  preferences.getBoolean("isfirst",true);
    }


    public void setFirst(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("isfirst", false);
        editor.commit();
    }


    /**
     * 序列化写入文件
     * @param filename
     * @param data
     * @param <T>
     */
    public  <T> void writeToFile(String filename,T... data) {
        // TODO Auto-generated method stub
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(context.getFilesDir().toString()+"/" + filename));

            for(T list:data){
                out.writeObject(list);
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 从文件中读取
     * @param filename
     * @param <T>
     * @return
     */
    public <T> Object getFromFile(String filename){


        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(context.getFilesDir().toString()+"/" + filename));
            Object data = inputStream.readObject();
            return data;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

    
    
    public static void setString( String preference, String key, String value) {
        SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(preference, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    public static String getString( String preference, String key, String defaultValue) {
        SharedPreferences sharedPreferences =  BaseApplication.getInstance().getSharedPreferences(preference, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }
    
    public static void setLong( String preference, String key, long value) {
        SharedPreferences sharedPreferences =  BaseApplication.getInstance().getSharedPreferences(preference, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }
    
    public static long getLong(String preference, String key, long defaultValue) {
        SharedPreferences sharedPreferences =  BaseApplication.getInstance().getSharedPreferences(preference, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }
    
    public static void setBoolean( String preference, String key, boolean value) {
        SharedPreferences sharedPreferences =  BaseApplication.getInstance().getSharedPreferences(preference, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    
    public static boolean getBoolean( String preference, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences =  BaseApplication.getInstance().getSharedPreferences(preference, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }
    
    public static void setInt(String preference, String key, int value) {
        SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences(preference, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    
    public static int getInt(String preference, String key, int defaultValue) {
        SharedPreferences sharedPreferences =  BaseApplication.getInstance().getSharedPreferences(preference, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    
    
    /** 
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法 
     * @param context 
     * @param key 
     * @param object  
     */  
    public static void setParam(String name , String key, Object object){  
          
        String type = object.getClass().getSimpleName();  
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);  
        SharedPreferences.Editor editor = sp.edit();  
          
        if("String".equals(type)){  
            editor.putString(key, (String)object);  
        }  
        else if("Integer".equals(type)){  
            editor.putInt(key, (Integer)object);  
        }  
        else if("Boolean".equals(type)){  
            editor.putBoolean(key, (Boolean)object);  
        }  
        else if("Float".equals(type)){  
            editor.putFloat(key, (Float)object);  
        }  
        else if("Long".equals(type)){  
            editor.putLong(key, (Long)object);  
        }  
          
        editor.commit();  
    }  
      
      
    /** 
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值 
     * @param context 
     * @param key 
     * @param defaultObject 
     * @return 
     */  
    public static Object getParam(String name , String key, Object defaultObject){  
        String type = defaultObject.getClass().getSimpleName();  
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);  
          
        if("String".equals(type)){  
            return sp.getString(key, (String)defaultObject);  
        }  
        else if("Integer".equals(type)){  
            return sp.getInt(key, (Integer)defaultObject);  
        }  
        else if("Boolean".equals(type)){  
            return sp.getBoolean(key, (Boolean)defaultObject);  
        }  
        else if("Float".equals(type)){  
            return sp.getFloat(key, (Float)defaultObject);  
        }  
        else if("Long".equals(type)){  
            return sp.getLong(key, (Long)defaultObject);  
        }  
          
        return null;  
    }  
    
}