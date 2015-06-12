/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drjane.promise.ui.fragment;

import java.util.Calendar;

import org.androidannotations.annotations.EFragment;

import com.drjane.promise.R;
import com.drjane.promise.adapter.CalendarAdapter;
import com.drjane.promise.calendar.Utils;
import com.drjane.promise.ui.widget.CustomGridView;
import com.kerkr.edu.app.BaseActivity;
import com.kerkr.edu.app.BaseFragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

@EFragment()
public class CalendarFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    
    private int mPageNumber;
    
    private Calendar mCalendar;
    
    private Handler mHandler = new Handler();
    
    private CalendarAdapter calendarGridViewAdapter;
    
    public static Fragment create(int pageNumber) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(false);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        mCalendar = Utils.getSelectCalendar(mPageNumber);
        mHandler.post(new Runnable() {
            
            @Override
            public void run() {
                calendarGridViewAdapter = new CalendarAdapter(getActivity(), mCalendar);
                
            }
        });
    }
    
    private CustomGridView mGridView;
    
    private void initGridView() {
        // 取得屏幕的宽度和高度
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();
        
        mGridView = new CustomGridView(getActivity());
        mGridView.setNumColumns(7);
        mGridView.setColumnWidth(40);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (Width == 720 && Height == 1280) {
            mGridView.setColumnWidth(40);
        }
        mGridView.setGravity(Gravity.CENTER_VERTICAL);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        mGridView.setVerticalSpacing(1);
        mGridView.setHorizontalSpacing(1);
        
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                // 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = calendarGridViewAdapter.getStartPositon();
                int endPosition = calendarGridViewAdapter.getEndPosition();
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    String scheduleDay = calendarGridViewAdapter.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
                    // String scheduleLunarDay =
                    // calV.getDateByClickItem(position).split("\\.")[1];
                    // //这一天的阴历
                    String scheduleYear = calendarGridViewAdapter.getShowYear();
                    String scheduleMonth = calendarGridViewAdapter.getShowMonth();
                    Log.i("TAG", "   position " + position + "year" + scheduleYear + scheduleDay);
                    // Toast.makeText(CalendarActivity.this, "点击了该条目",
                    // Toast.LENGTH_SHORT).show();
                    BaseActivity activity= (BaseActivity)getActivity();
                    activity.startFragment(new DayOrderListFragment());
                }
            }
        });
        mHandler.post(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mGridView.setAdapter(calendarGridViewAdapter);
                
            }
        });
    }
    
    /** <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void init() {
        Log.i(getClass().getSimpleName(), "init....");
        
    }
    
    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        initGridView();
        return mGridView;
    }
    
}
