package com.kerkr.edu.manager;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.util.LruCache;

/**
 * 自定义字体使用管理
 * Created by wanggang on 15/4/29.
 */
public class TypefaceManager {

    private static final String ROBOTO_LIGHT_FILENAME = "Roboto-Light.ttf";
    private static final String ROBOTO_CONDENSED_FILENAME = "RobotoCondensed-Regular.ttf";
    private static final String ROBOTO_CONDENSED_BOLD_FILENAME = "RobotoCondensed-Bold.ttf";
    private static final String ROBOTO_CONDENSED_LIGHT_FILENAME = "RobotoCondensed-Light.ttf";
    private static final String ROBOTO_SLAB_FILENAME = "RobotoSlab-Regular.ttf";

    private static final String ROBOTO_LIGHT_NATIVE_FONT_FAMILY = "sans-serif-light";
    private static final String ROBOTO_CONDENSED_NATIVE_FONT_FAMILY = "sans-serif-condensed";
    
    private static final String TYPE_NAME = "";
    private static final String ROBOTO_REGULAR = "";

    private LruCache<String, Typeface> mCahce;
    private AssetManager assertManager;

    public TypefaceManager(AssetManager assertManager) {
        this.assertManager = assertManager;
        mCahce = new LruCache<String, Typeface>(3);
    }

    /**
     * 返回指定的字体
     *
     * @return 返回的需要字体
     */
    public Typeface getRobotoRegular() {
        return getTypeface(ROBOTO_REGULAR);
    }

    /**
     * 获取字体
     *
     * @param filename 字体名称
     * @return 返回获取的字体对象
     */
    private Typeface getTypeface(String filename) {
        Typeface typeface = mCahce.get(filename);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(assertManager, "fonts/" + filename);
            mCahce.put(filename, typeface);
        }
        return typeface;
    }
    

    public Typeface getRobotoLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return Typeface.create(ROBOTO_LIGHT_NATIVE_FONT_FAMILY, Typeface.NORMAL);
        }
        return getTypeface(ROBOTO_LIGHT_FILENAME);
    }

    public Typeface getRobotoCondensed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return Typeface.create(ROBOTO_CONDENSED_NATIVE_FONT_FAMILY, Typeface.NORMAL);
        }
        return getTypeface(ROBOTO_CONDENSED_FILENAME);
    }

    public Typeface getRobotoCondensedBold() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return Typeface.create(ROBOTO_CONDENSED_NATIVE_FONT_FAMILY, Typeface.BOLD);
        }
        return getTypeface(ROBOTO_CONDENSED_BOLD_FILENAME);
    }

    public Typeface getRobotoCondensedLight() {
        return getTypeface(ROBOTO_CONDENSED_LIGHT_FILENAME);
    }

    public Typeface getRobotoSlab() {
        return getTypeface(ROBOTO_SLAB_FILENAME);
    }
}