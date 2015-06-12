/*
 * 文 件 名:  PaletteUtils.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-5-13
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.kerkr.edu.utill;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.TextView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-5-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PaletteUtils {
    
    
    /**
     * 颜色加深处理
     * 
     * @param RGBValues
     *            RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *            Android中我们一般使用它的16进制，
     *            例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *            red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *            所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    public static int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }


    public static void extract(Bitmap bitmap ,final PaletteSwatch swatch,final View  view,final TextView ... tvs) {

     // 提取颜色
        // Palette palette = Palette.generate(bitmap);   // 此方法可能会阻塞主线程，建议使用异步方法
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                // 提取完毕
                Palette.Swatch paletteSwatch = null;    //
                switch (swatch) {
                    case vibrant:
                        paletteSwatch = palette.getVibrantSwatch();    // 有活力的颜色
                      
                        break;
                    case darkVibrant:
                        paletteSwatch = palette.getDarkVibrantSwatch();    // 有活力的暗色
                        break;
                    case lightVibrant:
                        paletteSwatch = palette.getLightVibrantSwatch();    // 有活力的亮色
                        
                        break;
                    case muted:
                        paletteSwatch= palette.getMutedSwatch();    // 柔和的颜色
                        break;
                    case darkMuted:
                        paletteSwatch = palette.getDarkMutedSwatch();    // 柔和的暗色
                        break;
                    case lightMuted:
                        paletteSwatch = palette.getLightMutedSwatch();    // 柔和的亮色
                        break;
                    default:
                        break;
                }
                if(view != null)
                view.setBackgroundColor(paletteSwatch.getRgb());
                if(tvs != null){
                    for (TextView textView: tvs) {
                        
                        textView.setTextColor(paletteSwatch.getTitleTextColor());
                    }
                }
            }
        });
      
    }
     enum PaletteSwatch{
        vibrant,//"有活力的颜色"
        darkVibrant,//"有活力的暗色"
        lightVibrant,//"有活力的亮色"
        muted,//"柔和的颜色"
        darkMuted,//柔和的暗色
        lightMuted//柔和的亮色
    }
}
