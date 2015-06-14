package com.kerkr.edu.observer;

import com.kerkr.edu.app.CallKit;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;


/**
 * 
 * @author jayqqaa12
 * 
 * 观察 通话记录 是否产生 ，产生后删除
 *
 */
public class PhoneNumberObserver extends ContentObserver
{
	
	
	private String incomingnumber;
	private Context context;
	
	public PhoneNumberObserver(Handler handler,String incomingnumber,Context context) {
		super(handler);
		this.incomingnumber = incomingnumber;
		this.context = context;
	}

	@Override
	public void onChange(boolean selfChange) {
	
		super.onChange(selfChange);
		CallKit.deleteCallLog(incomingnumber);
		
		//当删除了呼叫记录后 反注册内容观察者
		context.getContentResolver().unregisterContentObserver(this);
	}

}
