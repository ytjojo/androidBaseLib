package com.kerkr.edu.receiver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStateReceiver extends BroadcastReceiver {
	private static final String TAG = "PhoneStateReceiver";
	private boolean isIncoming = false;
	private String incoming_number = null;
	TelephonyManager tm = null;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			isIncoming = false;
			String phoneNumber = intent
					.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			Log.i(TAG, "call OUT:" + phoneNumber);
			
		} else if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
			// ���������
			tm = (TelephonyManager) context
					.getSystemService(Service.TELEPHONY_SERVICE);

			
			switch (tm.getCallState()) {
			case TelephonyManager.CALL_STATE_RINGING://来电
				isIncoming = true;// ��ʶ��ǰ������
				Log.i(TAG, "ring count----------");
			//TODO
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK://正在通话
				if (isIncoming) {
					Log.i(TAG, "incoming ACCEPT :" + incoming_number);
				}
				break;

			case TelephonyManager.CALL_STATE_IDLE:///空闲
				if (isIncoming) {
					Log.i(TAG, "incoming IDLE");
				}
				break;
			}

		}

	}
	 private  void dial(Context context, String number) {
	        Class<TelephonyManager> c = TelephonyManager.class;
	        Method getITelephonyMethod = null;
	        try {
	            getITelephonyMethod = c.getDeclaredMethod("getITelephony",
	                    (Class[]) null);
	            getITelephonyMethod.setAccessible(true);
	        } catch (SecurityException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (NoSuchMethodException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        try {
	            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	            Object iTelephony;
	            iTelephony = (Object) getITelephonyMethod.invoke(tManager,(Object[]) null);
	            Method dial = iTelephony.getClass().getDeclaredMethod("dial", String.class);
	            dial.invoke(iTelephony, number);
	        } catch (IllegalArgumentException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (SecurityException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (NoSuchMethodException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (InvocationTargetException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	 }
	    private ArrayList<String>  getPhoneNum(Context context) {  
	        ArrayList<String> numList = new ArrayList<String>();  
	        //�õ�ContentResolver����     
	        ContentResolver cr = context.getContentResolver();       
	        //ȡ�õ绰���п�ʼһ��Ĺ��     
	        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);     
	        while (cursor.moveToNext())     
	        {                 
	            // ȡ����ϵ��ID     
	            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));     
	            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,  
	                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);     
	            // ȡ�õ绰����(���ܴ��ڶ������)     
	            while (phone.moveToNext())     
	            {     
	                String strPhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));     
	                numList.add(strPhoneNumber);    
	                Log.v("tag","strPhoneNumber:"+strPhoneNumber);  
	            }     
	              
	            phone.close();     
	        }     
	        cursor.close();  
	        return numList;  
	    }  
}
