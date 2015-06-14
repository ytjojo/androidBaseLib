package com.kerkr.edu.app;

import java.lang.reflect.Field;
import java.util.List;

import com.kerkr.edu.app.BaseActivity;
import com.kerkr.edu.app.Constans;
import com.kerkr.edu.eventbus.DefaultEvent;
import com.kerkr.edu.log.VALog;
import com.kerkr.edu.utill.CollectionUtils;
import com.kerkr.edu.utill.DrawerToast;
import com.kerkr.edu.utill.NetChecker;
import com.kerkr.edu.utill.ScreenUtils;
import com.umeng.analytics.MobclickAgent;
import com.ytjojo.androidapplib.R;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewDebug.FlagToString;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment {
    public static Fragment.SavedState mFragmentState = null;
    
    public BaseActivity mActivity;
    
    public View mContentView;
    
    public boolean aleryInit;
    
    public boolean isVisible;
    
    private void init() {
        setInitialSavedState(mFragmentState);
    }
    
    public boolean isAleryInit() {
        return aleryInit;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutResource() > 0) {
            
            mContentView = inflater.inflate(getLayoutResource(), container, false);
        }
        else {
            mContentView = createContentView(inflater, container, savedInstanceState);
        }
        aleryInit = true;
        return mContentView;
    }
    
    public View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }
    
    
    public void setTitle(String title) {
        mActivity.setMiddleTitle(title);
    }
    
    public Toolbar getToolbar() {
        return mActivity.mToolbar;
    }
    
    public abstract int getLayoutResource();
    
    public View findViewById(int id) {
        return mContentView.findViewById(id);
    }
    
    public View getContentView() {
        return mContentView;
    }
    
    public Context getApplicationContext() {
        if (mActivity != null) {
            return mActivity.getApplicationContext();
        }
        return null;
    }
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.e("uservisible", " setUserVisibleHint-------------------------" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            
            onUserVisble();
            
        }
        else {
            onHidden();
            isVisible = false;
            
        }
    }
    
    /** <一句话功能简述>
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void onHidden() {
        // TODO Auto-generated method stub
        
    }
    
    public abstract void onUserVisble();
    
    public abstract void setNavigations();
    
    /**
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable
    Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        mloadDialog = new Dialog(getActivity(), R.style.Dialog_Translucent_NoTitle);
//        if (getUserVisibleHint() && !isVisible) {
//            setUserVisibleHint(true);
//        }
        /* 
         toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
             @Override
             public boolean onMenuItemClick(MenuItem item) {
                 return false;
             }
         });*/
    }
    
    public void setNavigation(int drawableId, View.OnClickListener l) {
        getToolbar().setNavigationIcon(drawableId);
        getToolbar().setNavigationOnClickListener(l);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
    	setRetainInstance(true);
    	setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
        
    }
    
    /**
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        setNavigations();
        
    }
    
    /**
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }
    
    /**
     * 
     */
    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
        mActivity = null;
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
            
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
    	// TODO Auto-generated method stub
    	super.onHiddenChanged(hidden);
    	if(hidden){
    		onHidden();
    	}else{
    		onUserVisble();
    	}
    }
    
    public void umengEvent(String eventId, String label) {
        if (Constans.DEBUG_MODE || mActivity == null) {
            return;
        }
        MobclickAgent.onEvent(getApplicationContext(), eventId, label);
    }
    
    public void finish() {
        if (isContextEnable()) {
            getActivity().finish();
        }
    }
    
    /**
     * 
     */
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    
    private void onEventMainThread(DefaultEvent e) {
        
    }
    
    public void saveState() {
        mFragmentState = getFragmentManager().saveFragmentInstanceState(this);
    }
    
    private Dialog mloadDialog;
    
    public void showProgressBar(String loadingStr) {
        if (!isContextEnable() || (mloadDialog != null && mloadDialog.isShowing()) || !NetChecker.isConnected(getActivity())) {
            return;
        }
        
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View progressView = inflater.inflate(R.layout.dialog_loading, null);
        TextView infoTextView = (TextView) progressView.findViewById(R.id.dialog_loading_title);
        infoTextView.setText(loadingStr);
        mloadDialog.setContentView(progressView);
        mloadDialog.setCancelable(true);
        // 设置ProgressDialog 是否可以按退回按键取消
        mloadDialog.setCanceledOnTouchOutside(false);
        mloadDialog.show(); // 让ProgressDialog显示
    }
    
    public void hideProgressBar() {
        if (mloadDialog != null && mloadDialog.isShowing()) {
            mloadDialog.dismiss();
        }
    }
    
    public void startFragment(BaseFragment f) {
        if (!isContextEnable()) {
            return;
        }
        mActivity.startFragment(f);
    }
    
    public void popToFragment(Class<?> cls) {
        mActivity.popToFragment(cls);
    }
    
    public void popToRoot() {
        mActivity.popToRoot();
    }
    
    public void popBackFragment() {
        mActivity.popBackFragent();
    }
    
    /**
     * bundel带参数activity跳转
     * 
     * @param claz
     *            目标类
     * @param bundleName
     *            bundle名称
     * @param bundle
     *            参数
     */
    public void startBundleActivity(Class<?> claz, String bundleName, Bundle bundle) {
        Intent intent = new Intent(getActivity(), claz);
        intent.putExtra(bundleName, bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    
    public boolean isContextEnable() {
        mActivity = (BaseActivity) getActivity();
        if (mActivity == null || !mActivity.isActive()) {
            return false;
        }
        return true;
    }
    
    @SuppressWarnings("unchecked")
    final public <E extends View> E findView(int id) {
        try {
            return (E) findViewById(id);
        }
        catch (ClassCastException e) {
            throw e;
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName()); //统计页面
    }
    
    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(this.getClass().getName());
        super.onPause();
        
    }
    
}
