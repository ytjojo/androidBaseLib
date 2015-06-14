/*
 * 文 件 名:  ViewpagerFragment.java
 * 版    权:  VA Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  lijing
 * 修改时间:  2015-6-2
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.drjane.promise.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.drjane.promise.R;
import com.drjane.promise.calendar.Utils;
import com.kerkr.edu.app.BaseFragment;
import com.kerkr.edu.app.DrawerToast;
import com.kerkr.edu.log.VALog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author lijing
 * @version [版本号, 2015-6-2]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ViewpagerFragment extends BaseFragment implements
		View.OnClickListener {

	private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）

	private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)

	private int year_c = 0;

	private int month_c = 0;

	private int day_c = 0;

	private String currentDate = "";

	/** 每次添加gridview到viewflipper中时给的标记 */
	private int gvFlag = 0;

	/** 当前的年月，现在日历顶端 */
	private TextView currentMonth;

	/** 上个月 */
	private ImageView prevMonth;

	/** 下个月 */
	private ImageView nextMonth;

	private ViewPager mViewPager;

	private String month;

	private int mScrollState;

	public void init() {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date); // 当期日期
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		VALog.i("hiddenchanged" + hidden);
	}

	/**
	 * @param view
	 * @param savedInstanceState
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		currentMonth = (TextView) findViewById(R.id.currentMonth);
		prevMonth = (ImageView) findViewById(R.id.prevMonth);
		nextMonth = (ImageView) findViewById(R.id.nextMonth);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		prevMonth.setOnClickListener(this);
		nextMonth.setOnClickListener(this);
		mViewPager.setOffscreenPageLimit(5);
		final ScreenSlidePagerAdapter screenSlidePagerAdapter = new ScreenSlidePagerAdapter(
				getChildFragmentManager());
		mViewPager.setAdapter(screenSlidePagerAdapter);
		mViewPager.setCurrentItem(500);
		month = Calendar.getInstance().get(Calendar.YEAR)
				+ "-"
				+ Utils.LeftPad_Tow_Zero(Calendar.getInstance().get(
						Calendar.MONTH) + 1);
		currentMonth.setText(month);

		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						Calendar calendar = Utils.getSelectCalendar(position);
						month = calendar.get(Calendar.YEAR)
								+ "-"
								+ Utils.LeftPad_Tow_Zero(calendar
										.get(Calendar.MONTH) + 1);
						currentMonth.setText(month);
					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {

					}

					@Override
					public void onPageScrollStateChanged(int state) {
						mScrollState = state;
					}
				});
	}

	/**
	 * @param savedInstanceState
	 */
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	/**
	 * @param menu
	 * @param inflater
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_viewpager, menu);
		mActivity.showMenuIcon(menu);

	}

	/**
	 * @param item
	 * @return
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_create_order) {
			startFragment(new CreateOrderFragment_());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return CalendarFragment.create(position);
		}

		@Override
		public int getCount() {
			return 1000;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {

			CalendarFragment fragment = (CalendarFragment) object;
			fragment.init();

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			Log.d("destroyItem", position + "   " + object.toString());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.nextMonth: // 下一个月
			enterNextMonth();
			break;
		case R.id.prevMonth: // 上一个月
			enterLastMonth();
			break;
		default:
			break;
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述> [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	private void enterLastMonth() {
		if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {

			int curPosition = mViewPager.getCurrentItem();
			mViewPager.setCurrentItem(curPosition - 1, true);
		}

	}

	/**
	 * <一句话功能简述> <功能详细描述> [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	private void enterNextMonth() {
		if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {

			int curPosition = mViewPager.getCurrentItem();
			mViewPager.setCurrentItem(curPosition + 1, true);
		}

	}

	/**
	 * @return
	 */
	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.fragment_viewpager;
	}

	/**
     * 
     */
	@Override
	public void onUserVisble() {
		// TODO Auto-generated method stub

	}

	/**
     * 
     */
	@Override
	public void setNavigations() {
		setTitle("Promise");
		setNavigation(R.drawable.ic_action_image_timer_auto,
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						DrawerToast.getInstance(getApplicationContext()).show(
								"列表");
						startFragment(new OrderListFragment_());

					}
				});

	}
}
