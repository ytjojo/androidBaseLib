package com.kerkr.edu.widget.nest;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * listView高度自适应最大
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author lijing
 * @version [版本号, 2013-8-24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class HoldListView extends ListView {
    
    public HoldListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public HoldListView(Context context) {
        super(context);
    }
    
    public HoldListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //根据模式计算每个child的高度和宽度
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}