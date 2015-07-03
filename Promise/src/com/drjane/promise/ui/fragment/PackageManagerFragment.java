package com.drjane.promise.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.support.design.widget.Snackbar;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.drjane.promise.R;
import com.drjane.promise.model.OrderPackage;
import com.kerkr.edu.app.BaseFragment;
import com.kerkr.edu.recycleView.BaseRecyclerViewAdapter;
import com.kerkr.edu.recycleView.SwipeToDismissTouchListener;
import com.kerkr.edu.recycleView.SwipeToDismissTouchListener.DismissCallbacks;
import com.kerkr.edu.recycleView.SwipeToDismissTouchListener.PendingDismissData;
import com.kerkr.edu.recycleView.SwipeToDismissTouchListener.SwipeDirection;
import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;

@EFragment
public class PackageManagerFragment extends BaseFragment implements ItemTouchListenerAdapter.RecyclerViewOnItemClickListener, ActionMode.Callback {
    
    @ViewById(R.id.listview)
    android.support.v7.widget.RecyclerView mRecyclerView;
    
    BaseRecyclerViewAdapter<OrderPackage> mAdapter;
    
    private List<OrderPackage> mList;
    
    private ActionMode mActionMode;
    SwipeToDismissTouchListener swipeToDismissTouchListener;
    
    @Override
    public int getLayoutResource() {
        // TODO Auto-generated method stub
        return R.layout.fragment_packegemanager;
    }
    
    @Override
    public void onUserVisble() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void setNavigations() {
        // TODO Auto-generated method stub
        
    }
    
    @AfterViews
    public void initView() {
        mList = new ArrayList<OrderPackage>();
        for (int i = 0; i <20; i++) {
            OrderPackage item = new OrderPackage();
            item.name = "双机位"+i;
            mList.add(item);
        }
        
        mAdapter = new BaseRecyclerViewAdapter<OrderPackage>(mList, R.layout.item_package) {
            
            @Override
            public void bindDataToView(com.kerkr.edu.recycleView.BaseRecyclerViewAdapter.VH viewHolder, int Postion, OrderPackage model) {
                TextView tv = viewHolder.get(R.id.tv_name);
                tv.setText(model.name);
            }
            
        };
         swipeToDismissTouchListener = new SwipeToDismissTouchListener(mRecyclerView, new DismissCallbacks() {
            
            @Override
            public void onDismiss(RecyclerView view, List<PendingDismissData> dismissData) {
                for (SwipeToDismissTouchListener.PendingDismissData data : dismissData) {
                    mAdapter.removeItem(data.position);
                    mAdapter.notifyItemRemoved(data.position);
                }
                Snackbar
                .make(mContentView,"撤销删除", Snackbar.LENGTH_LONG)
                .setAction("撤销", new View.OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        
                        
                        
                    }
                })
                .show();
            }
            
            @Override
            public SwipeDirection canDismiss(int position) {
                // TODO Auto-generated method stub
                return SwipeDirection.RIGHT;
            }
        });
        mRecyclerView.addOnItemTouchListener(new ItemTouchListenerAdapter(mRecyclerView, this));
        mRecyclerView.addOnItemTouchListener(swipeToDismissTouchListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }
    
    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        mActionMode.setTitle("Selected " + mAdapter.getSelectedItemCount());
    }
    
    @Override
    public void onItemClick(RecyclerView parent, View clickedView, int position) {
        if (this.mActionMode != null) {
            toggleSelection(position);
        }
        
    }
    
    @Override
    public void onItemLongClick(RecyclerView parent, View clickedView, int position) {
        mActivity.startSupportActionMode(this);
        toggleSelection(position);
    }
    
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.actionmode_package, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        swipeToDismissTouchListener.setEnabled(false);
        this.mActionMode = mode;
        return false;
    }
    
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
       
        return true;
    }
    
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // TODO Auto-generated method stub
        swipeToDismissTouchListener.setEnabled(true);
        mAdapter.clearSelections();
        this.mActionMode = null;

    }
    
}
