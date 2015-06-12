package com.kerkr.edu.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private SmsManager sms = null;
	private String m_action = null;
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		
		m_action=intent.getAction();
		if(m_action.equals(SMS_RECEIVED))
		{
			sms=SmsManager.getDefault();
//TODO			
		}

	}
	public void sendSMSmsg(Intent intent)
	{
		Bundle bundle = intent.getExtras();                        //�õ���ͼ�е�bundle����
		 if (bundle != null) {
			    Object[] pdus = (Object[]) bundle.get("pdus");     //�õ����Ϊ"pdus"�Ķ���,��һ��Object����,�����ÿһ��Ԫ��Ϊһ��byte[]����
			    SmsMessage[] messages = new SmsMessage[pdus.length];  
			    for (int i = 0; i < pdus.length; i++) messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			        for (SmsMessage message : messages){
			            String msg = message.getMessageBody();    //��������
			            String to = message.getOriginatingAddress(); //���ŵ�ַ
			            sms.sendTextMessage(to, null, msg, null, null);
			     }
		 }
	}
}
