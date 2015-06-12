package com.kerkr.edu.utill;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kerkr.edu.log.VALog;

import android.graphics.Bitmap;
import android.util.Base64;

public class ConvertUtil {
    /**
     * url转为文件名
     */
    public static String getCacheKey(String url) {
        if (url == null) {
            //                        throw new RuntimeException ("Null url passed in");
            return "";
        }
        else {
            return url.replaceAll("[:/,%?&=]", "+").replaceAll("[+]+", "+");
        }
    }
    
    /**
     * 7.8 增加对图片是否回收判断
     */
    public static byte[] BitMapToByte(Bitmap bm) {
        if (bm != null && !bm.isRecycled()) {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            byte imgdata[] = null;
            try {
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytestream);
                imgdata = bytestream.toByteArray();
                bytestream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return imgdata;
        }
        else {
            return null;
        }
    }
    
    public static List<String> stringsToList(final String[] src) {
        if (src == null || src.length == 0) {
            return null;
        }
        final List<String> result = new ArrayList<String>();
        for (int i = 0; i < src.length; i++) {
            result.add(src[i]);
        }
        return result;
    }
    
    //将Bitmap转换成字符串
    public static String bitmaptoString(Bitmap bitmap) {
        if (bitmap == null) {
            return "";
        }
        String userIcon = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 65, bStream);
        byte[] bytes = bStream.toByteArray();
        userIcon = Base64.encodeToString(bytes, Base64.DEFAULT);
        VALog.e("userIcon===" + userIcon);
        return userIcon;
    }
    
    //url 转换为字符串
    public static String replaceUrlWithPlus(String url) {
        if (url != null) {
            return url.replaceAll("http://(.)*?/", "").replaceAll("[:/,%?&=]", "+").replaceAll("[+]+", "+");
        }
        return null;
    }
}
