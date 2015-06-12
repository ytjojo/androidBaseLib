/*
 * 文 件 名:  URiUtil.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-5-29
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.kerkr.edu.kit;

import java.io.File;
import java.security.PublicKey;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.Contacts.People;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.StaticLayout;
import android.util.Log;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-5-29]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UriUtil {
    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        }
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[] { ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param c
     * @param absPath
     * @return [参数说明]
     * 
     * @return Uri [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static Uri getImageUri(Context c, String absPath) {
        Uri uri = null;
        if (absPath != null) {
            final String path = Uri.decode(absPath);
            ContentResolver cr = c.getContentResolver();
            StringBuffer buff = new StringBuffer();
            buff.append("(").append(Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
            Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI, new String[] { Images.ImageColumns._ID }, buff.toString(), null, null);
            int index = 0;
            for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                index = cur.getColumnIndex(Images.ImageColumns._ID);
                // set _id value
                index = cur.getInt(index);
            }
            if (index == 0) {
                //do nothing
            }
            else {
                Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                if (uri_temp != null) {
                    uri = uri_temp;
                }
            }
        }
        return uri;
    }
    
    //打开网页
    public static void startHtml(Context c, String url) {
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        c.startActivity(it);
    }
    
    // 显示地图:
    public static void startMap(Context c, String geoUri) {
        //           Uri uri = Uri.parse("geo:38.899533,-77.036476");
        Uri uri = Uri.parse(geoUri);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        c.startActivity(it);
    }
    
    // 调用拨号程序
    
    public static void call(Context c, String phone) {
        
        //          Uri uri = Uri.parse("tel:xxxxxx");
        //          Intent it = new Intent(Intent.ACTION_DIAL, uri);  
        //          
        Uri uri = Uri.parse("tel:" + phone);
        Intent it = new Intent(Intent.ACTION_CALL, uri);
        c.startActivity(it);
        //要使用这个必须在配置文件中加入<uses-permission id="Android.permission.CALL_PHONE" />
    }
    
    //发送SMS/MMS
    // 调用发送短信的程序
    public static void sendSMS(Context c, String phone, String text) {
        Uri uri = Uri.parse("smsto:" + phone);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", text);
        c.startActivity(it);
    }
    
    //发送Email
    public static void sendMail(Context c, String[] reciver, String[] ccs, String[] mySbuject, String body, String type, String attachmentUri) {
        
        Intent it = new Intent(Intent.ACTION_SEND);
        it.setType("text/plain");
        it.putExtra(Intent.EXTRA_EMAIL, reciver);
        it.putExtra(Intent.EXTRA_CC, ccs);
        it.putExtra(Intent.EXTRA_TEXT, body);
        it.putExtra(Intent.EXTRA_SUBJECT, mySbuject);
        //            it.setType("message/rfc822");   
        //uri = "[url=]file:///sdcard/mysong.mp3[/url]";
        it.putExtra(Intent.EXTRA_STREAM, attachmentUri);
        c.startActivity(Intent.createChooser(it, "选择邮件客户端"));
    }
    
    // Uninstall 程序
    
    public static void uninstall(String packegeName, Context c) {
        Uri uri = Uri.fromParts("package", packegeName, null);
        Intent it = new Intent(Intent.ACTION_DELETE, uri);
        c.startActivity(it);
    }
    
    //调用相册
    public static final String MIME_TYPE_IMAGE_JPEG = "image/*";
    
    public static final int ACTIVITY_GET_IMAGE = 0;
    
    public static final int ACTIVITY_GET_CAMERA_IMAGE = 1;
    
    public static void choosePic(Activity c) {
        Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
        getImage.addCategory(Intent.CATEGORY_OPENABLE);
        getImage.setType(MIME_TYPE_IMAGE_JPEG);
        c.startActivityForResult(getImage, ACTIVITY_GET_IMAGE);
    }
    
    //调用系统相机应用程序，并存储拍下来的照片
    public static String takePickture(Activity c) {
        long time = System.currentTimeMillis();
        String absPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + c.getPackageName() + "/image" + time + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + c.getPackageName() + "/image", time
                        + ".jpg")));
        c.startActivityForResult(intent, ACTIVITY_GET_CAMERA_IMAGE);
        return absPath;
    }
    
    //进入联系人页面
    
    public static void startContact(Context c) {
        
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(People.CONTENT_URI);
        c.startActivity(intent);
    }
    
    public static Intent openFile(String filePath) {
        
        File file = new File(filePath);
        if (!file.exists())
            return null;
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        }
        else if (end.equals("3gp") || end.equals("mp4")) {
            return getAudioFileIntent(filePath);
        }
        else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        }
        else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        }
        else if (end.equals("ppt")) {
            return getPptFileIntent(filePath);
        }
        else if (end.equals("xls")) {
            return getExcelFileIntent(filePath);
        }
        else if (end.equals("doc")) {
            return getWordFileIntent(filePath);
        }
        else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        }
        else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        }
        else if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        }
        else {
            return getAllIntent(filePath);
        }
    }
    
    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param) {
        
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }
    
    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {
        
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }
    
    //Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param) {
        
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }
    
    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param) {
        
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }
    
    //Android获取一个用于打开Html文件的intent   
    public static Intent getHtmlFileIntent(String param) {
        
        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }
    
    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param) {
        
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }
    
    //Android获取一个用于打开PPT文件的intent   
    public static Intent getPptFileIntent(String param) {
        
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }
    
    //Android获取一个用于打开Excel文件的intent   
    public static Intent getExcelFileIntent(String param) {
        
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }
    
    //Android获取一个用于打开Word文件的intent   
    public static Intent getWordFileIntent(String param) {
        
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }
    
    //Android获取一个用于打开CHM文件的intent   
    public static Intent getChmFileIntent(String param) {
        
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }
    
    //Android获取一个用于打开文本文件的intent   
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {
        
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        }
        else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }
    
    //Android获取一个用于打开PDF文件的intent   
    public static Intent getPdfFileIntent(String param) {
        
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
}
