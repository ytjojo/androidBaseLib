package com.kerkr.edu.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.Args;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.kerkr.edu.app.ActivityTack;
import com.kerkr.edu.app.Constans;
import com.kerkr.edu.design.PaletteUtils;
import com.kerkr.edu.design.ScreenUtils;
import com.kerkr.edu.eventbus.DefaultEvent;
import com.kerkr.edu.log.VALog;
import com.kerkr.edu.utill.CollectionUtils;
import com.kerkr.edu.widget.PagerSlidingTabStrip;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;
import com.ytjojo.androidapplib.R;

import de.greenrobot.event.EventBus;

public abstract class BaseActivity extends AppCompatActivity {
	private static final String TAG = "BaseActivity";

	public static final String EXTRA_IMAGE = "DetailActivity:image";

	public static final String KEY_TITLLE = "key_title";

	public Dialog mypDialog;

	private UmengNotificationListener umengNotificationListener;

	// 支付时弹出dialog
	protected ProgressDialog progressDialog;

	public Toolbar mToolbar;

	public TextView mMiddleTitleTV;

	public ViewGroup mContentView;

	public FrameLayout mWorkSpaceView;

	public String mTitle;

	
	public FragmentStack mFragmentStack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		isDestroyed = false;
		setContentView(R.layout.base_fragment);
		mFragmentStack = new FragmentStack(this, R.id.fragment_Container,getRootFragmentClass());
		if(isNeedSetUpFragment()){
			mFragmentStack.setupRoot(savedInstanceState);
		}
		mWorkSpaceView = (FrameLayout) findViewById(R.id.fragment_Container);
		mContentView = (ViewGroup) findViewById(R.id.fragment_layout);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setSubtitle("");
		mMiddleTitleTV = (TextView) findViewById(R.id.actionbar_title);
		mTitle = (String) getTitle();
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		restoreTitle(savedInstanceState);
		ActivityTack.getInstance().addActivity(this);
		EventBus.getDefault().registerSticky(this);

		

	}

	/**
	 * 显示menicon <功能详细描述>
	 * 
	 * @param featureId
	 * @param menu
	 *            [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void showMenuIcon(Menu menu) {
		if (menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @return [参数说明]
	 * 
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public abstract boolean isNeedSetUpFragment();

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @return [参数说明]
	 * 
	 * @return BaseFragment [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public abstract Class<? extends BaseFragment> getRootFragmentClass();

	// @Override
	// protected void attachBaseContext(Context newBase) {
	// // This allows inflation magic
	// // Ideally you would do this in your BaseActivity or Application instead
	// of per activity
	// super.attachBaseContext(MrVector.wrap(newBase));
	// }
	public void setMiddleTitle(CharSequence c) {
		mToolbar.setTitle("");
		mToolbar.setSubtitle("");
		mTitle = c.toString();
		mMiddleTitleTV.setVisibility(View.VISIBLE);
		mMiddleTitleTV.setText(c);
	}

	public View getView(String idString) {
		if (TextUtils.isEmpty(idString)) {
			return null;
		}
		int titleId = Resources.getSystem().getIdentifier(idString, "id",
				"android");
		if (titleId < 0) {
			return null;
		}
		return findViewById(titleId);
	}

	protected String getFragmentTag(Class<? extends BaseFragment> cls) {
		StringBuilder sb = new StringBuilder(cls.toString());
		return sb.toString();
	}

	public boolean isContainFragment(Class<BaseFragment> clazz) {
		List<Fragment> list = getSupportFragmentManager().getFragments();
		for (Fragment f : list) {
			if (f.getClass() == clazz) {
				return true;
			}
		}
		return false;
	}


	public void startFragment(BaseFragment fragment) {
		mFragmentStack.startFragment(fragment);
	}

	public void popBackFragent() {
		mFragmentStack.popBackFragent();
	}

	public void pushFragmentToBackStack(
			Class<? extends BaseFragment> cls,Bundle args) {
		if (cls == null) {
			return;
		}
		mFragmentStack.pushFragmentToBackStack(mFragmentStack.createRootFragment(cls, args));
	}

	public void popToFragment(Class<?> cls) {
		mFragmentStack.popToFragment(cls);
	}

	public void popToRoot() {
		mFragmentStack.popToRoot();
	}

	/**
	 * 永远显示右边更多icon 一般是三个点的 <功能详细描述> [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	private void setOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void launch(BaseActivity activity, View transitionView, String url,
			Class clazz) {
		ActivityOptionsCompat options = ActivityOptionsCompat
				.makeSceneTransitionAnimation(activity, transitionView,
						EXTRA_IMAGE);
		Intent intent = new Intent(activity, clazz);
		intent.putExtra(EXTRA_IMAGE, url);
		ActivityCompat.startActivity(activity, intent, options.toBundle());
	}

	public void setTransitionName(View image) {
		ViewCompat.setTransitionName(image, EXTRA_IMAGE);
	}

	/**
	 * 界面颜色的更改
	 */
	public void colorChange(Bitmap bitmap,
			final PagerSlidingTabStrip pagerSlidingTabStrip) {
		// Palette的部分
		Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
			/**
			 * 提取完之后的回调方法
			 */
			@Override
			public void onGenerated(Palette palette) {
				Palette.Swatch vibrant = palette.getVibrantSwatch();

				if (pagerSlidingTabStrip != null) {

					/* 界面颜色UI统一性处理,看起来更Material一些 */
					pagerSlidingTabStrip.setBackgroundColor(vibrant.getRgb());
					pagerSlidingTabStrip.setTextColor(vibrant
							.getTitleTextColor());
					// 其中状态栏、游标、底部导航栏的颜色需要加深一下，也可以不加，具体情况在代码之后说明
					pagerSlidingTabStrip.setIndicatorColor(PaletteUtils
							.colorBurn(vibrant.getRgb()));
				}

				mToolbar.setBackgroundColor(vibrant.getRgb());
				if (android.os.Build.VERSION.SDK_INT >= 21) {
					Window window = getWindow();
					// 很明显，这两货是新API才有的。
					window.setStatusBarColor(PaletteUtils.colorBurn(vibrant
							.getRgb()));
					window.setNavigationBarColor(PaletteUtils.colorBurn(vibrant
							.getRgb()));
				}
			}
		});
	}

	/**
	 * @param layoutResID
	 */
	public void setContentViewInternal(int layoutResID) {
		if (layoutResID == R.layout.base_fragment) {
			super.setContentView(layoutResID);

		} else {
			View v = LayoutInflater.from(this).inflate(layoutResID,
					mWorkSpaceView, false);
			FrameLayout.LayoutParams params = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			int actionbarHeight = ScreenUtils.getActionbarHeight();
			params.setMargins(0, 0, 0, 0);
			mWorkSpaceView.addView(v, params);

		}
	}

	/**
	 * @param view
	 * @param params
	 */
	public void setContentViewInternal(View view,
			android.view.ViewGroup.LayoutParams params) {
		// TODO Auto-generated method stub
		if (view.getId() == R.layout.base_fragment) {
			super.setContentView(view);

		} else {

			FrameLayout.LayoutParams lp = (LayoutParams) params;
			mWorkSpaceView.addView(view, lp);
		}
	}

	public void setOverlay(boolean b) {
		FrameLayout.LayoutParams params = (LayoutParams) mWorkSpaceView
				.getLayoutParams();
		if (params != null) {
			if (b) {

				params.setMargins(0, 0, 0, 0);
			} else {
				int actionbarHeight = ScreenUtils.getActionbarHeight();
				params.setMargins(0, actionbarHeight, 0, 0);
			}
			mWorkSpaceView.setLayoutParams(params);
		}
	}

	/**
	 * @param view
	 */
	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		super.setContentView(view);
	}

	public void restoreTitle(Bundle savedInstanceState) {
		if (savedInstanceState != null)
			mTitle = savedInstanceState.getString(KEY_TITLLE);

		mToolbar.setTitle(mTitle);
	}

	public Toolbar getToolbar() {
		return mToolbar;
	}

	private boolean isDestroyed;

	private boolean isDestroy() {

		return isDestroyed;
	}

	public boolean isActive() {
		if (isFinishing() || isDestroyed) {
			return false;
		}
		return true;
	}

	private boolean isResumed;

	@Override
	protected void onResume() {
		super.onResume();
		isResumed = true;

		if (!BaseApplication.isActive) {
			// app 从后台唤醒，进入前台
			BaseApplication.isActive = true;

			VALog.e("返回app");
		}
		// 友盟统计分析
		MobclickAgent.onResume(this);
	}

	@SuppressWarnings("unchecked")
	final public <E extends View> E findView(int id) {
		try {
			return (E) findViewById(id);
		} catch (ClassCastException e) {

			throw e;
		}
	}

	private void onEventMainThread(DefaultEvent e) {

	}

	@Override
	protected void onPause() {
		super.onPause();
		isResumed = false;
		// 友盟统计分析
		MobclickAgent.onPause(this);
	}

	private boolean isResumed() {
		return isResumed;
	}

	@Override
	protected void onDestroy() {
		ActivityTack.getInstance().removeActivity(this);
		mFragmentStack.ondestroy();
		EventBus.getDefault().unregister(this);
		super.onDestroy();
		isDestroyed = true;

	}
	 

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_right_out);
		mFragmentStack.onbackPressed();
	}

	/**
	 * 点击标题栏返回事件
	 */
	public void clickTitleBack() {
		finish();
		// overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_right_out);
	}

	/**
	 * @param outState
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString(KEY_TITLLE, mTitle);
		getSupportFragmentManager().putFragment(outState,
				getRootFragmentClass().toString(), mFragmentStack.getRootFragment());
	}

	public final Handler mHandler = new Handler();

	protected void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("加载中");
			progressDialog.setCancelable(true);
		}
		progressDialog.show();
	}

	protected void hideProgressDialog() {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN
				&& !isActive()) {
			return;
		}
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}

	public void showProgressBar() {
		if (!this.isActive() || mypDialog.isShowing()) {
			return;
		}

		LayoutInflater inflater = getLayoutInflater();
		View progressView = inflater.inflate(R.layout.dialog_loading, null);
		progressView.findViewById(R.id.dialog_loading_title).setVisibility(
				View.VISIBLE);
		mypDialog.setContentView(progressView);
		mypDialog.setCancelable(true);
		// 设置ProgressDialog 是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
		mypDialog.show(); // 让ProgressDialog显示
	}

	public synchronized void hideProgressBar() {
		if (isActive()) {
			return;
		}
		if (mypDialog != null && mypDialog.isShowing()) {
			mypDialog.dismiss();
		}
	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		hideProgressBar();

	}

	public void umengEvent(String eventId, String label) {
		if (Constans.DEBUG_MODE) {
			return;
		}
		MobclickAgent.onEvent(getApplicationContext(), eventId, label);
	}

	@Override
	protected void onStop() {
		if (!AppUtil.isAppOnForeground(this)) {

			BaseApplication.isActive = false;
		}
		super.onStop();
	}

	public DisplayImageOptions getDisplayImageOptions(int defaultDrawable,
			boolean iscacheOnDisk, int corner) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(defaultDrawable)
				.showImageForEmptyUri(defaultDrawable)
				.showImageOnFail(defaultDrawable).cacheInMemory(true)
				.cacheOnDisk(iscacheOnDisk).considerExifParams(false)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// 16bit 降低内存开销
				.displayer(new RoundedBitmapDisplayer(corner)).build();
		return options;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleNotificationIntent(intent);
	}

	protected void handleNotificationIntent(Intent intent) {
		if (intent != null && umengNotificationListener != null) {
			Bundle bd = intent.getExtras();
			if (bd != null && bd.containsKey(Constans.NOTIFY_TYPE)) {
				String notifyType = bd.getString(Constans.NOTIFY_TYPE);
				String value = bd.getString(Constans.NOTIFY_VALUE);
				if (value == null) {
					value = "";
				}
				umengNotificationListener.onNotificationIntentReceived(
						notifyType, value, false);
			}
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	public void hideKeyboardForCurrentFocus() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getCurrentFocus() != null) {
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);
		}
	}

	public void showKeyboardAtView(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager
				.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	}

	protected void exitFullScreen() {
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	}

	public void setUmengNotificationListener(UmengNotificationListener listener) {
		umengNotificationListener = listener;
	}

	public UmengNotificationListener getNotificationLIstener() {
		return umengNotificationListener;
	}

	public interface UmengNotificationListener {
		// 通知跳转页面的intent获得后
		void onNotificationIntentReceived(String notifyType, String value,
				boolean needDelete);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void startActivityLollipop(View view, Intent intent, ImageView hero) {
		((ViewGroup) hero.getParent()).setTransitionGroup(false);

		ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
				this, hero, "photo_hero");
		startActivity(intent, options.toBundle());
	}

	private void startActivityGingerBread(View view, Intent intent, int resId) {
		int[] screenLocation = new int[2];
		view.getLocationOnScreen(screenLocation);
		intent.putExtra("left", screenLocation[0])
				.putExtra("top", screenLocation[1])
				.putExtra("width", view.getWidth())
				.putExtra("height", view.getHeight());
		startActivity(intent);

		// Override transitions: we don't want the normal window animation in
		// addition to our
		// custom one
		overridePendingTransition(0, 0);

		// The detail activity handles the enter and exit animations. Both
		// animations involve a
		// ghost view animating into its final or initial position respectively.
		// Since the detail
		// activity starts translucent, the clicked view needs to be invisible
		// in order for the
		// animation to look correct.
		ViewPropertyAnimator.animate(findViewById(resId)).alpha(0.0f);
	}
}
