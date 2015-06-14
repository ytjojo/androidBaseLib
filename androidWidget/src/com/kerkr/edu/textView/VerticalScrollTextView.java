package com.kerkr.edu.textView;

import java.util.List;


import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.kerkr.edu.design.DensityUtil;
import com.kerkr.edu.dto.UMengPush;
import com.kerkr.edu.utill.CollectionUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * @author xushilin
 * 
 */
public class VerticalScrollTextView extends TextView {
    private static final int DY = 40; // 每一行的间隔
    public int index = 0;
    public float mTouchHistoryY;
    //    private Activity mActivity;
    float pixelSize = 0;
    Handler mHandler = new Handler();
    private float mX;
    private Paint mPathPaint;
    private List<UMengPush> list;
    private int mY;
    private float middleY;// y轴中间
    private Context mContext;
    private volatile boolean isRun = true;
    private ObjectAnimator animator;
    Runnable mUpdateResults = new Runnable() {
        public void run() {
            if (list == null) {
                setText("暂时没有通知公告");
                isRun = false;
            }
            else {
                if (index<list.size ()&&list.get(index) != null && list.get(index).text != null) {
                    excuteAnimation(list.get(index).text);
                }
                if (list.size() == 1) {
                    isRun = false;
                }

            }

        }
    };
    private Thread t;
    private int showNumber = 0;
    
    public VerticalScrollTextView(Context context) {
        super(context);
        mContext = context;
        //		init();
    }

    private synchronized void excuteAnimation(final String text)
    {
            animator=ObjectAnimator.ofFloat (VerticalScrollTextView.this, "translationY", 0, -getHeight ());
            animator.setDuration (200);
            animator.removeAllListeners ();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setText (text);
                ObjectAnimator.ofFloat (VerticalScrollTextView.this, "translationY", getHeight (),0).setDuration(200).start ();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    //	private void init() {
    //		setFocusable(true);
    //		if(list==null||list.size ()==0){
    //			list=new ArrayList<UMengPush>();
    //			UMengPush sen=new UMengPush (0,"暂时没有通知公告");
    //			list.add(0, sen);
    //		}
    //
    //		// 高亮部分 当前歌词
    //		mPathPaint = new Paint();
    //		mPathPaint.setAntiAlias(true);
    //		mPathPaint.setColor(getResources ().getColor (R.color._3f3f3f));
    //        setRawTextSize(16);
    ////		mPathPaint.setTextSize(32);
    //		mPathPaint.setTypeface(Typeface.SANS_SERIF);
    //	}
    //	protected void onDraw(Canvas canvas) {
    //		super.onDraw(canvas);
    //		canvas.drawColor(0xEFeffff);
    ////		Paint p = mPaint;
    //		Paint p2 = mPathPaint;
    ////		p.setTextAlign(Paint.Align.CENTER);
    //		if (index == -1)
    //			return;
    //		p2.setTextAlign(Paint.Align.CENTER);
    //		// 先画当前行，之后再画他的前面和后面，这样就保持当前行在中间的位置
    //        if (list.get (index)!=null&&list.get(index).text!=null)
    //        {
    //            canvas.drawText(list.get(index).text, mX, middleY+pixelSize/2, p2);
    //        }
    //
    ////		float tempY = middleY;
    //		// 画出本句之前的句子
    ////		for (int i = index - 1; i >= 0; i--) {
    ////			tempY = tempY - DY;
    ////			if (tempY < 0) {
    ////				break;
    ////			}
    ////			canvas.drawText(list.get(i).getName(), mX, tempY, p);
    ////		}
    ////		tempY = middleY;
    ////		// 画出本句之后的句子
    ////		for (int i = index + 1; i < list.size(); i++) {
    ////			// 往下推移
    ////			tempY = tempY + DY;
    ////			if (tempY > mY) {
    ////				break;
    ////			}
    ////			canvas.drawText(list.get(i).getName(), mX, tempY, p);
    ////		}
    //	}
    //	protected void onSizeChanged(int w, int h, int ow, int oh) {
    //		super.onSizeChanged(w, h, ow, oh);
    //		mX = w * 0.5f;
    //		mY = h;
    //		middleY = h * 0.5f;
    //	}
    
    public VerticalScrollTextView(Context context, AttributeSet attr) {
        super(context, attr);
        mContext = context;
        //		init();
    }
    
    public VerticalScrollTextView(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        mContext = context;
        //		init();
    }
    
    public long updateIndex(int index) {
        if (index == -1 || list == null)
            return -1;
        if (index > list.size() - 1) {
            index = list.size() - 1;
        }
        if (index < 0) {
            index = 0;
        }
        this.index = index;
        return index;
    }
    
    public List<UMengPush> getList() {
        return list;
    }
    
    public void setList(List<UMengPush> list) {
        if (CollectionUtils.isValid(list)) {
            this.list = list;
        }
    }
    
    public void updateUI() {
        if (t == null) {
            t = new Thread(new updateThread());
            t.start();
        }

        isRun = true;
    }
    
    private void setRawTextSize(float size) {
        pixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, DensityUtil.getDeviceDisplay(mContext));

        if (pixelSize != mPathPaint.getTextSize()) {
            mPathPaint.setTextSize(pixelSize);
        }
    }
    
    class updateThread implements Runnable {
        long time = 3000; // 开始 的时间，不能为零，否则前面几句歌词没有显示出来

        public void run() {
            while (true) {
                long sleeptime = updateIndex(showNumber);
                mHandler.post(mUpdateResults);
                if (sleeptime == -1)
                    return;
                try {
                    Thread.sleep(time);
                    showNumber++;
                    if (showNumber >= getList().size())
                        showNumber = 0;
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}