package com.kerkr.edu.app;

import java.util.ArrayList;
import java.util.List;


import com.kerkr.edu.log.VALog;
import com.kerkr.edu.utill.CollectionUtils;
import com.ytjojo.androidapplib.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;

public class FragmentStack implements
		OnBackStackChangedListener {

	public Class<? extends BaseFragment> mRootFragmentClass;
	public BaseFragment mRootFragment;
	public BaseActivity mActivity;
	public FragmentManager mFragmentManager;
	ArrayList<Fragment> mFragmentStack = new ArrayList<Fragment>();
	private ArrayList<String> topLevelTags = new ArrayList<String>();
	private int mContainerId;

	public FragmentStack(BaseActivity activity, int containerId,Class<? extends BaseFragment> clazz) {
		this.mActivity = activity;
		this.mContainerId = containerId;
		this.mFragmentManager = mActivity.getSupportFragmentManager();
		this.mFragmentManager.addOnBackStackChangedListener(this);
		this.mRootFragmentClass = clazz;
	}
	public BaseFragment getRootFragment(){
	
		return mRootFragment;
	}

	@SuppressWarnings("unchecked")
	public void setupRoot(Bundle savedInstanceState) {

		if (savedInstanceState == null) {

			mRootFragment = (BaseFragment) Fragment.instantiate(mActivity, 
					mRootFragmentClass.getName(), savedInstanceState);
			if (mRootFragment != null) {
				mFragmentManager.beginTransaction()
						.replace(mContainerId, mRootFragment).commit();
			}
		} else {
			mRootFragment = (BaseFragment) mFragmentManager.getFragment(
					savedInstanceState,
					getFragmentTag(mRootFragment.getClass()));
			if (mRootFragment == null) {
				return;
			}

			if (mRootFragment.isAdded()) {
				mFragmentManager.beginTransaction().show(mRootFragment)
						.commit();
			} else {
				mFragmentManager.beginTransaction()
						.replace(mContainerId, mRootFragment).commit();

			}
		}
	}

	public void pushFragmentToBackStack(BaseFragment fragment) {
		if (!mActivity.isActive() || fragment == null) {
			return;
		}
		// Fragment addedFragment =
		// getSupportFragmentManager().findFragmentByTag(getFragmentTag(fragment.getClass()));
		// if (addedFragment != null) {
		// Log.e("addd ff", "   mi;;ulllnulll______________________________ ");
		// getSupportFragmentManager().beginTransaction().remove(addedFragment).commitAllowingStateLoss();
		// getSupportFragmentManager().executePendingTransactions();
		// }

		FragmentTransaction ft = mFragmentManager.beginTransaction();
		String fragmentTag = getFragmentTag(fragment.getClass());

		ft.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out);
		List<Fragment> list = mFragmentManager.getFragments();

		if (fragment.isAdded()) {

		} else {
			ft.add(mContainerId, fragment, fragmentTag);
			ft.addToBackStack(fragmentTag);

		}
		if (CollectionUtils.isValid(list)) {

			for (Fragment f : list) {
				if (f != null && f.isAdded() && !f.isHidden())
					ft.hide(f);

			}
		}
		ft.show(fragment);
		ft.commitAllowingStateLoss();
		mFragmentStack.add(fragment);
		// getSupportFragmentManager().executePendingTransactions();
	}


	public void startFragment(BaseFragment fragment) {
		if (!mActivity.isActive() || fragment == null) {
			return;
		}
		pushFragmentToBackStack(fragment);
	}

	public void popBackFragent() {
		if (!mActivity.isActive()) {
			return;
		}

		int size = mFragmentManager.getBackStackEntryCount();
		
		if (size == 0) {

			mActivity.finish();// 当是最初的fragment时候调用这个方法会退出
			return;
		}
		if(mFragmentStack.size()>0)
		mFragmentStack.remove(mFragmentStack.size() - 1);
		mFragmentManager.popBackStackImmediate();
		hideAndShow();
		//

	}
	public void onbackPressed(){
		if(mFragmentStack.size() >0)
		mFragmentStack.remove(mFragmentStack.size() - 1);
		VALog.i("pressed back  " + mFragmentStack.size());
		
		
	}
	private void hideAndShow(){
		if(mFragmentStack.size()> 0){
			Fragment cur = mFragmentStack.get(mFragmentStack.size() -1);
			FragmentTransaction ft = mFragmentManager.beginTransaction().show(cur);
			for(Fragment f: mFragmentStack){
				if(f != cur && !f.isHidden()){
					ft.hide(f);
				}
			}
			ft.commitAllowingStateLoss();
		}
	}
	public void popToFragment(Class<?> cls) {
		if (cls == null) {
			return;
		}
		BaseFragment fragment = (BaseFragment) mFragmentManager
				.findFragmentByTag(cls.toString());
		mFragmentManager.popBackStackImmediate(cls.toString(), 0);
	}

	public void popToRoot() {
		FragmentManager fm = mFragmentManager;
		while (fm.getBackStackEntryCount() >= 1) {
			fm.popBackStackImmediate();
		}
	}
	@SuppressWarnings("unchecked")
	public BaseFragment createRootFragment(Class<? extends BaseFragment> clazz, Bundle args) {
		return (BaseFragment) Fragment.instantiate(mActivity, mRootFragment.getClass()
				.getName(), args);
	}

	protected String getFragmentTag(Class<? extends Fragment> cls) {
		StringBuilder sb = new StringBuilder(cls.toString());
		return sb.toString();
	}

	@Override
	public void onBackStackChanged() {
		VALog.i("backstack  ..." + mFragmentStack.size());
		
	}
	public void ondestroy(){
		mActivity = null;
		mRootFragment = null;
		mRootFragmentClass = null;
		mFragmentManager = null;
		mFragmentStack .clear();
		
	}

}
