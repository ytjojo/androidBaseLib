/*
 * 文 件 名:  ContractService.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-4-2
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.kerkr.edu.app;

import java.io.InputStream;

import com.kerkr.edu.log.VALog;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-4-2]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ContractService {
    
    /**获取库Phon表字段**/
    private static final String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
    
    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    
    /**电话号码**/
    private static final int PHONES_NUMBER_INDEX = 1;
    
    /**头像ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;
    
    /**联系人的ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;
    
    /**得到手机通讯录联系人信息**/
    private void getPhoneContacts() {
        ContentResolver resolver = BaseApplication.getInstance().getContentResolver();
        
        // 获取手机联系人  
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        
        if (phoneCursor != null && phoneCursor.moveToFirst()) {
            do {
                
                //得到手机号码  
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环  
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                
                //得到联系人名称  
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                
                //得到联系人ID  
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                
                //得到联系人头像ID  
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
                
                //得到联系人头像Bitamp  
                Bitmap contactPhoto = null;
                
                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的  
                if (photoid > 0) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                }
                else {
                    //         contactPhoto = BitmapFactory.decodeResource(VAAppAplication.getInstance().getResources(), R.drawable.contact_photo);  
                }
                
            }
            while (phoneCursor.moveToNext());
            
            phoneCursor.close();
        }
    }
    
    /**得到手机SIM卡联系人人信息**/
    private static void getSIMContacts() {
        ContentResolver resolver = BaseApplication.getInstance().getContentResolver();
        // 获取Sims卡联系人  
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);
        
        if (phoneCursor != null && phoneCursor.moveToFirst()) {
            
            do {
                
                // 得到手机号码  
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环  
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称  
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                VALog.i("sim卡 联系人" + contactName + "手机号" + phoneNumber);
                //Sim卡中没有联系人头像  
            }
            while (phoneCursor.moveToNext());
            
            phoneCursor.close();
        }
    }
    
    /*
     * 自定义显示Contacts提供的联系人的方法
     */
    public static void printContacts() {
        //生成ContentResolver对象
        ContentResolver contentResolver = BaseApplication.getInstance().getContentResolver();
        
        // 获得所有的联系人
        /*Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
         */
        //这段代码和上面代码是等价的，使用两种方式获得联系人的Uri
        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/contacts"), null, null, null, null);
        
        // 循环遍历
        if (cursor.moveToFirst()) {
            
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            int displayNameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            
            do {
                // 获得联系人的ID
                String contactId = cursor.getString(idColumn);
                // 获得联系人姓名
                String displayName = cursor.getString(displayNameColumn);
                
                //使用Toast技术显示获得的联系人信息
                VALog.i("联系人姓名：" + displayName);
                // 查看联系人有多少个号码，如果没有号码，返回0
                int phoneCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                
                if (phoneCount > 0) {
                    // 获得联系人的电话号码列表
                    Cursor phoneCursor = BaseApplication.getInstance()
                            .getContentResolver()
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                                    null,
                                    null);
                    if (phoneCursor.moveToFirst()) {
                        do {
                            //遍历所有的联系人下面所有的电话号码
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //使用Toast技术显示获得的号码
                            if (TextUtils.isEmpty(phoneNumber)) {
                                continue;
                            }
                            VALog.i("联系人电话：" + phoneNumber);
                        }
                        while (phoneCursor.moveToNext());
                    }
                    phoneCursor.close();
                }
                
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }
    
    public static class ContactTask extends AsyncTask<Void, Void, Void> {
        
        /**
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {
            try {
                printContacts();
                getSIMContacts();
            }
            catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }
        
        /**
         * @param result
         */
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }
        
        
    }
    
    public static void getContracts() {
        ContactTask task = new ContactTask();
        task.execute();
    }
}
