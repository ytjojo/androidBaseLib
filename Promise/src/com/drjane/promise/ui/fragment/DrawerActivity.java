package com.drjane.promise.ui.fragment;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;


import com.drjane.promise.R;
import com.kerkr.edu.app.BaseActivity;
import com.kerkr.edu.app.BaseFragment;


@EActivity
public class DrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @ViewById(R.id.navigation_view)
    NavigationView mNavigationView;

    private Toolbar mToolbar;
  
    private @IdRes int mCurrentMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparentStatusBar();
        setContentView(R.layout.activity_drawer);
        setupToolbar();
        setupNavDrawer();
        mCurrentMenuItem = R.id.schedule_manage;
      
        //TODO make it work
        //setTransparentStatusBar();
    }

    private void setNewRootFragment(BaseFragment fragment){
        if(fragment.hasCustomToolbar()){
            hideActionBar();
        }else {
            showActionBar();
        }
        startFragment(fragment);
        mDrawerLayout.closeDrawers();
    }

    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.hide();
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.show();
    }

    private void setupNavDrawer() {
        if(mDrawerLayout == null) {
            return;
        }
//        mDrawerLayout.setDrawerListener(this);
        //TODO look at documantation => homepage do I really need like that?
        mDrawerToggle = new ActionBarDrawerToggle(this
                , mDrawerLayout
                , mToolbar
                , R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);
        
      
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        
        mNavigationView.setNavigationItemSelectedListener(this);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }




    public void openDrawer(){
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }
    public void closeDrawer(){
        mDrawerLayout.closeDrawer(mNavigationView);
    }
    @Override
    public void onBackPressed() {
        
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            closeDrawer();
            return;
        }
        // TODO Auto-generated method stub
        super.onBackPressed();
        if(mCurrentMenuItem != R.id.schedule_manage){
            mNavigationView.getMenu().getItem(mCurrentIndex).setChecked(false);
            mNavigationView.getMenu().getItem(0).setChecked(true);
            mCurrentMenuItem = R.id.schedule_manage;
            
        }
    }

    private int mCurrentIndex = 0;
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == mCurrentMenuItem) {
        
            return false;
        }
        switch (id){
            case R.id.customer_manager:
                popToRoot();
                setNewRootFragment(new OrderListFragment_());
                mCurrentIndex = 2;
                break;
            case R.id.schedule_manage:
                mCurrentIndex = 0;
                popToRoot();
                break;

            case R.id.package_manage:
                popToRoot();
                setNewRootFragment(new PackageManagerFragment_());
                mCurrentIndex = 1;
                break;

        }
        mCurrentMenuItem = id;
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();
        return false;
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void setTransparentStatusBar(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    @Override
    public boolean isNeedSetUpFragment() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public Class<? extends BaseFragment> getRootFragmentClass() {
        // TODO Auto-generated method stub
        return ViewpagerFragment.class;
    }
}
