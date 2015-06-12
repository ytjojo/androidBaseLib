/*
 * 文 件 名:  JsonParser.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-5-11
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.kerkr.edu.utill;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

public class JsonParser {
    private static JsonParser _instance = null;
    
    private JsonGenerator _jsonGenerator = null;
    
    private ObjectMapper _objectMapper = null;
    
    protected JsonParser() {
        _objectMapper = new ObjectMapper();
        try {
            _jsonGenerator = _objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
            _objectMapper.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            //            _objectMapper.disable(Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static JsonParser getInstance() {
        if (_instance == null) {
            synchronized (JsonParser.class) {
                _instance = new JsonParser();
            }
        }
        return _instance;
    }
    
    public <T> T getObjectFromJson(String jsonString, Class<T> valueType) {
        try {
            return _objectMapper.readValue(jsonString, valueType);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public <T> T getObjectFromJson(File jsonFile, Class<T> valueType) {
        try {
            return _objectMapper.readValue(jsonFile, valueType);
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public String getJsonFromObject(Object obj) {
        try {
            //String filePath = VAAppAplication.getVACacheDir() + "temp.json";
            //File file  = new File(filePath);
            return _objectMapper.writeValueAsString(obj);
            
            //String data =  VAConfigCache.getTextFile(filePath);
            //return data;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public <T> List<T> parseList(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return new ArrayList<T>();
        }
        try {
            //            用ObjectMapper 的getTypeFactory().constructParametricType(collectionClass, elementClasses);
            JavaType javaType = _objectMapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
            return _objectMapper.readValue(json, javaType);
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        return new ArrayList<T>();
    }
    
    public <T> String listToJsonArray(List<T> list) {
        if (!CollectionUtils.isValid(list)) {
            return "";
        }
        try {
            return _objectMapper.writeValueAsString(list);
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }
    
}

