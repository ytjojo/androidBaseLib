/*
 * 文 件 名:  ActivityDatePickerFragment.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-2-5
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.ui.fragment;

import java.util.Calendar;
import java.util.Locale;

import org.androidannotations.annotations.EFragment;

import com.drjane.promise.R;
import com.kerkr.edu.app.BaseFragment;
import com.kerkr.edu.app.CompatUtil;

import de.greenrobot.event.EventBus;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.DatePicker;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-2-5]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@EFragment
public class DatePickerFragment extends BaseFragment {
    
    private DatePicker mDatePicker;
    
    private int mYear;
    
    private int mMouth;
    
    private int mDay;
    
    private Calendar mSelectCalendar = Calendar.getInstance();
    
    private Calendar mEndCalendar = Calendar.getInstance();
    
    private Calendar mCurCalendar;
    
    /**
     * @return
     */
    @Override
    public int getLayoutResource() {
        // TODO Auto-generated method stub
        return R.layout.fragment_activity_date_picker;
    }
    
    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable
    Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        mCurCalendar = Calendar.getInstance(Locale.CHINA);
        mYear = mCurCalendar.get(Calendar.YEAR);
        mMouth = mCurCalendar.get(Calendar.MONTH);
        mDay = mCurCalendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(mYear, mMouth, mDay, new DatePicker.OnDateChangedListener() {
            
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                
                mSelectCalendar.set(Calendar.YEAR, year);
                mSelectCalendar.set(Calendar.MONTH, monthOfYear);
                mSelectCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (isBefore(mSelectCalendar, mCurCalendar)) {
                    mDatePicker.updateDate(mYear, mMouth, mDay);
                }
            }
        });
        
        if (CompatUtil.hasJellyBean()) {
            mDatePicker.setCalendarViewShown(false);
        }
    }
    
    /**
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable
    Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        
    }
    
    public static boolean isBefore(Calendar c1, Calendar c2) {
        if (c1.get(c1.YEAR) > c2.get(c2.YEAR)) {
            return false;
        }
        else if (c1.get(c1.YEAR) < c2.get(c2.YEAR)) {
            return true;
        }
        
        if (c1.get(c1.MONTH) > c2.get(c2.MONTH)) {
            return false;
        }
        else if (c1.get(c1.MONTH) < c2.get(c2.MONTH)) {
            return true;
        }
        if (c1.get(Calendar.DAY_OF_MONTH) < c1.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_datapicker, menu);
        
    }
    
    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == R.id.action_done_datepicker) {
            
            popBackFragment();
            
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onUserVisble() {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 
     */
    @Override
    public void setNavigations() {
        setNavigation(R.drawable.ic_action_hardware_keyboard_backspace, new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popBackFragment();
            }
        });
        
    }
    
}
