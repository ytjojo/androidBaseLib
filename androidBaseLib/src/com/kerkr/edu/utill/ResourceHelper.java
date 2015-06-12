package com.kerkr.edu.utill;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.kerkr.edu.app.BaseApplication;
import com.ta.utdid2.android.utils.StringUtils;

public class ResourceHelper {

    public static CharSequence getResText(int resourceId) {
        Context context = BaseApplication.getInstance();
        if (context == null) {
            return null;
        }

        Resources resources = context.getResources();
        if (resources == null) {
            return null;
        }

        return resources.getText(resourceId);
    }

    public static String getResString(int resourceId) {
        Context context = BaseApplication.getInstance();
        if (context == null) {
            return null;
        }

        Resources resources = context.getResources();
        if (resources == null) {
            return null;
        }

        return resources.getString(resourceId);
    }

    public static String[] getStringArray(int resourceId) {
        Context context = BaseApplication.getInstance();
        if (context == null) {
            return null;
        }

        Resources resources = context.getResources();
        if (resources == null) {
            return null;
        }

        return resources.getStringArray(resourceId);
    }

    public static float getDimension(int resourceId) {
        Context context =BaseApplication.getInstance();
        if (context == null) {
            return -1;
        }

        Resources resources = context.getResources();
        if (resources == null) {
            return -1;
        }

        return resources.getDimension(resourceId);
    }

    public static float getDimensionPixelSize(int resourceId) {
        Context context =BaseApplication.getInstance();
        if (context == null) {
            return -1;
        }

        Resources resources = context.getResources();
        if (resources == null) {
            return -1;
        }

        return resources.getDimensionPixelSize(resourceId);
    }

    public static int getResourceIdentifier(String resourceName, String resourceType) {
        Context context =BaseApplication.getInstance();
        if (context == null) {
            return 0;
        }

        Resources resources = context.getResources();
        if (resources == null) {
            return 0;
        }

        String packageName = context.getPackageName();
        if (StringUtils.isEmpty(packageName)) {
            return 0;
        }
        
        return resources.getIdentifier(resourceName, resourceType, packageName);
    }

    public static InputStream openAssetStream(String fileName) {
        Context context = BaseApplication.getInstance();
        if (context == null) {
            return null;
        }

        AssetManager assets = context.getAssets();
        if (assets == null) {
            return null;
        }

        try {
            return assets.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static AssetFileDescriptor getAssetFileDescriptor(String assetName) {
        Context context = BaseApplication.getInstance();
        if (context == null) {
            return null;
        }

        AssetManager assets = context.getAssets();
        if (assets == null) {
            return null;
        }

        try {
            return assets.openFd(assetName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Drawable getDrawable(int resourceId) {
        Context context = BaseApplication.getInstance();
        if (context == null) {
            return null;
        }

        Resources resources = context.getResources();
        if (resources == null) {
            return null;
        }

        return resources.getDrawable(resourceId);
    }

    public static int getColor(int resourceId) {
        Context context = BaseApplication.getInstance();
        if (context == null) {
            return -1;
        }

        Resources resources = context.getResources();
        if (resources == null) {
            return -1;
        }

        return resources.getColor(resourceId);
    }

    public static int getActionBarHeight() {
        Context context = BaseApplication.getInstance();
        if (context == null) {
            return -1;
        }

        Theme theme = context.getTheme();
        if (theme == null) {
            return -1;
        }

        TypedArray styledAttributes = theme.obtainStyledAttributes(new int[] {android.R.attr.actionBarSize});
        if (styledAttributes == null) {
            return -1;
        }

        int actionBarHeight = (int) styledAttributes.getDimension(0, 0);

        styledAttributes.recycle();

        return actionBarHeight;
    }

    public static int getScreenOrientation() {
        Context context =BaseApplication.getInstance();
        if (context == null) {
            return -1;
        }

        Resources resources = context.getResources();
        if (resources == null) {
            return -1;
        }

        Configuration configuration = resources.getConfiguration();
        if (configuration == null) {
            return -1;
        }

        return configuration.orientation;
    }
    
    /**
     * 获取Manifest中的meta-data值
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String values = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                values = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return values;
    }
}