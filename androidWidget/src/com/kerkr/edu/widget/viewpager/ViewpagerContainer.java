package com.kerkr.edu.widget.viewpager;

import java.util.ArrayList;

import com.ytjojo.widget.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kerkr.edu.utill.ImageLoaderUtill;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ViewpagerContainer extends FrameLayout {
    public static final int DEFAULT_DELAY = 3;// second
    
    private ItemOnClick clickListener = null;
    
    private Context context;
    
    private AutoScrollViewPager viewPage = null;
    
    private CircleZoomPageIndicator mIndicator;
    
    private ImageAdapter adapter = null;
    
    // 要展示锟侥斤拷锟斤拷
    
    private int sleepTime = 1;
    private ArrayList<String> urlList = new ArrayList<String>();
    
    public ViewpagerContainer(Context context) {
        super(context);
        init(context);
    }
    
    public ViewpagerContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

        init(context);
    }
    
    @SuppressLint("NewApi")
    public ViewpagerContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    
    private void init(Context c) {
        this.context = c;

    }
    
    public void setItemOnClickListener(ItemOnClick _Listener) {
        this.clickListener = _Listener;
    }
    
    public void setData(ArrayList<String> urls) {
        setData(urls, null, DEFAULT_DELAY);
    }
    
    public void setData(ArrayList<String> urls, ItemOnClick _Listener) {
        setData(urls, _Listener, DEFAULT_DELAY);
    }
    
    public void setData(ArrayList<String> urls, ItemOnClick _Listener, float second) {
        this.urlList = urls;
        setItemOnClickListener(_Listener);
        sleepTime = (int) (second * 1000);
        setAdapter();
        
    }
    
    public PagerAdapter getAdapter() {
        if (viewPage != null) {
            return viewPage.getAdapter();
        }
        return null;
    }
    
    public void setAdapter(ViewPagerAdapter adapter) {

        viewPage.setAdapter(adapter);
        viewPage.setInterval(3000);
        viewPage.startAutoScroll();
        viewPage.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % adapter.getImageCount());
        mIndicator.setTruePagerSize(adapter.getImageCount());
        mIndicator.setViewPager(viewPage);
    }
    
    private void setAdapter() {
        
        if (urlList == null || urlList.size() == 0 || mIndicator == null) {
            return;
        }
        adapter = new ImageAdapter(urlList);
        
        viewPage.setAdapter(adapter);
        mIndicator.setViewPager(viewPage);
        
    }
    
    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
//        viewPage = (AutoScrollViewPager) findViewById(R.id.pager);
//        mIndicator = (CircleZoomPageIndicator) findViewById(R.id.indicator);
        
    }
    
    public void indicatorSetTrueSize(int size) {
        mIndicator.setTruePagerSize(size);
    }
    
    public interface ItemOnClick {
        public void onItemClick(int position);
    }
    
    class ImageAdapter extends PagerAdapter {

        private ArrayList<String> urlsArrayList;

        private ArrayList<ImageView> imgLists;

        private int trueSize;

        public ImageAdapter(ArrayList<String> urls) {
            this.urlsArrayList = urls;
            this.trueSize = urlsArrayList.size();
            this.imgLists = new ArrayList<ImageView>();
            ImageView imgView = null;
            for (int i = 0; i < trueSize; i++) {
                imgView = new ImageView(context);
                imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                final int position = i;
                imgView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
                imgLists.add(imgView);
            }
        }

        @Override
        public int getCount() {
            return trueSize;

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(imgLists.get(position));
        }

        /**
         *
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView img = imgLists.get(position);
            try {
                ImageLoader.getInstance ().displayImage (urlList.get (position) , img, ImageLoaderUtill.getDisplayImageOptions(-1, true, 8));
                container.addView(img, 0);

            }
            catch (Exception e) {
                // TODO: handle exception
            }
            return img;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
    
}
