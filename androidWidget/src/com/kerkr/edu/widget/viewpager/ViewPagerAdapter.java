package com.kerkr.edu.widget.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kerkr.edu.dto.BrandBanner;
import com.kerkr.edu.utill.ImageLoaderUtill;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Hyr on 2014/10/29.
 * Desc:
 */
public class ViewPagerAdapter extends RecyclingPagerAdapter {
    public int count;
    
    private Context _context;
    
    private ImageLoader imageLoader = ImageLoader.getInstance();
    
    private boolean isInfiniteLoop;
    
    private OnItemClickListener onItemClickListener;
    
    public ViewPagerAdapter(Activity c) {
        _context = c;
        
        isInfiniteLoop = false;
    }
    private List<BrandBanner> mCompanyBannerList = new ArrayList<BrandBanner>();
    @Override
    public int getCount()//实现图片持续滚动
    {
        if (mCompanyBannerList.size() > 1) {
            return Integer.MAX_VALUE;
        }
        return mCompanyBannerList.size();
    }
    
    @Override
    public View getView(int position, View convertView, final ViewGroup container) {
        count = mCompanyBannerList.size();
        if (count != 0) {
            position = position % count;//保证循环滚动
        }
        ViewHolder holder = null;
        if (convertView == null) {
//            convertView = LayoutInflater.from(_context).inflate(R.layout.banner_item, container, false);
//            holder = new ViewHolder();
//            holder.bannerImg = (ImageView) convertView.findViewById(R.id.iv_banner);
            holder.bannerImg.setScaleType(ImageView.ScaleType.FIT_XY);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        if (position < count) {
            imageLoader.displayImage(mCompanyBannerList.get(position).bannerImageUrlString,
                    
                    holder.bannerImg,
                   ImageLoaderUtill.getDisplayImageOptions(0, true, 0));
            holder.bannerImg.setTag(mCompanyBannerList.get(position).bannerImageUrlString);
        }
        
        if (onItemClickListener != null) {
            final int finalPosition = position;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, finalPosition);
                }
            });
        }
        return convertView;
    }
    
    public int getImageCount() {
        return mCompanyBannerList.size();
    }
    
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }
    
    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }
    
    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ViewPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
    
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    
    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }
    
    public static class ViewHolder {
        /**
         * banner图片
         */
        public ImageView bannerImg;
    }
}