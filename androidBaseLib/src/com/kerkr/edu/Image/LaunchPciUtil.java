/*
 * 文 件 名:  LaunchPciUtil.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-8
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.kerkr.edu.Image;

import java.util.List;

import android.text.TextUtils;

import com.kerkr.edu.String.JsonParser;
import com.kerkr.edu.cache.SharePreferenceService;
import com.kerkr.edu.design.DensityUtil;
import com.kerkr.edu.utill.CollectionUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-6-8]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class LaunchPciUtil {
    
    public static void saveRecorde(List<String> imgUrls) {
        
        if (CollectionUtils.isValid(imgUrls)) {
            for (String url : imgUrls) {
                if (TextUtils.isEmpty(url)) {
                    continue;
                }
            }
            String jsonString = JsonParser.getInstance().listToJsonArray(imgUrls);
            
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(false)                        // 设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true)
                    // 设置下载的图片是否缓存在SD卡中 
                    .build();
            ImageLoader.getInstance().loadImage(imgUrls.get(0), new ImageSize(DensityUtil.getWidth(), DensityUtil.getHeight()), options, null);
            
            SharePreferenceService.setString("imageUrls", "launch_imagurls", jsonString);
        }
        
    }
    
    public static List<String> getLauchPicUrls() {
        String jsonUrls = SharePreferenceService.getString("imageUrls", "launch_imagurls", "");
        if (TextUtils.isEmpty(jsonUrls)) {
            return null;
        }
        return JsonParser.getInstance().parseList(jsonUrls, String.class);
    }
    
}
