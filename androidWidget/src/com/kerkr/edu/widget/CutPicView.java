package com.kerkr.edu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

@SuppressLint("DrawAllocation") 
public class CutPicView extends View implements OnTouchListener {

	private Context mContext;
	private Paint mPaint;//绘画CutPicView的主要pain
	private Paint mRoundPaint;//绘画和前景蒙版层相交圆的paint
	private Paint mFgroundPaint;//绘画前景蒙版层的paint
	private Paint mClearFgPaint;//清除蒙版层内容的paint
	private Paint mCutPicPaint;//切割圆形bitmap的pain
	
	private Bitmap mBitmap;//传进来的bitmap，用于切割
	private Bitmap mLastBitmap;//用于保存传进来bitmap，即传进来bitmap的副本
	private Bitmap mFgBitmap;//用于绘画前景蒙版层的bitmap
	private Bitmap output;//用于承接切割出来的图片内容，也就是说这个就是返回来的圆形bitmap
	
	
	private Point mCenterPoint;
	private Point mBitmapPoint;
	private PointF mFirstDragPoint;
	private PointF mFirstZoomPoint;
	private PointF mSecondZoomPoint;
	
	private Canvas mFgCanvas;//前景蒙版层画布
	private Canvas canvas;//切割圆形bitmap的画布
	private Rect src;
	private Paint paint;
	private RectF rectF;
	private Rect dst;
	private RectF mRect;
	
	private final int MODE_DRAG = 0;
	private final int MODE_ZOOM = 1;
	
	private int mCurrMode;
	private int mRadius;
	private int mRingWidth;
	private int mBitmapLastWidth;
	private int mBitmapLastHeight;
	private int mAvatorWidth;
	private float scale = 0;
	private float oriDis;
	private boolean isFinishFirstZoomed;
	private boolean isSecondePointerUp;
	
	public CutPicView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	private void initView() {
		//设置半径为屏幕的1/3
		mRadius = getScreenWidth()*1/3;
		mRingWidth = 4;
		mAvatorWidth = 2*mRadius;
		
		mPaint = new Paint();
		mRoundPaint = new Paint();
		mFgroundPaint = new Paint();
		mClearFgPaint = new Paint();
		mCutPicPaint = new Paint();
		
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setAntiAlias(true); 
		mPaint.setARGB(255, 255 ,225, 255);
		mPaint.setStrokeWidth(mRingWidth);
		
		mRoundPaint.setStrokeWidth(mRadius);  
        mRoundPaint.setAntiAlias(true);
		mRoundPaint.setARGB(255, 0, 0, 0);
		
		mFgroundPaint.setAntiAlias(true);
		mFgroundPaint.setARGB(185, 0 ,0, 0); 
		//设置前景蒙版层的画笔Xfermode模式为XOR，也就是这个模式可以将两个图形相交的部分清除掉，剩下一个有着空洞的蒙版层
		mFgroundPaint.setXfermode(new PorterDuffXfermode(Mode.XOR));
		//设置切割圆形头像的画笔Xfermode，这个模式可以将两个图形相交的部分切割出来，类似抠图
		mCutPicPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		
		mCenterPoint = new Point();
		mBitmapPoint = new Point();
		mFirstDragPoint = new PointF();
		mFirstZoomPoint = new PointF();
		mSecondZoomPoint = new PointF();
		
		 //清掉mFgCanvas中的内容
		mClearFgPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
       // mClearFgPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
        
		initCutPicParams();
		
		setOnTouchListener(this);
		
	}

	private void initCutPicParams() {
		float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        float offsetX = mCenterPoint.x - mBitmapPoint.x-mRadius;
        float offsetY = mCenterPoint.y - mBitmapPoint.y-mRadius;
        
        top = offsetY;
        bottom = offsetY+mAvatorWidth;
        left = offsetX;
        right = offsetX+mAvatorWidth;
                
        dst_left = 0;
        dst_top = 0;
        dst_right = mAvatorWidth;
        dst_bottom = mAvatorWidth;
        
        output = Bitmap.createBitmap(mAvatorWidth,
        		mAvatorWidth, Config.ARGB_8888);
        canvas = new Canvas(output);
         
        paint = new Paint();
        src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        rectF = new RectF(dst);

        paint.setAntiAlias(true);
         
        paint.setColor(0xFFFFFFFF);
        
	}

	/**
	 * 设置切割图片的位置，这里的位置随着bitmap的坐标而改变
	 */
	private void setCutPicPosition() {
        src.set(mCenterPoint.x - mBitmapPoint.x-mRadius,
        		mCenterPoint.y - mBitmapPoint.y-mRadius,
        		mCenterPoint.x - mBitmapPoint.x-mRadius+mAvatorWidth,
        		mCenterPoint.y - mBitmapPoint.y-mRadius+mAvatorWidth);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//保证里面的变量只进行一次初始化，因为onDraw方法里不适合进行初始化操作，这样会开销很大的资源。
		if(mFgBitmap == null) {
			
			mLastBitmap = mBitmap;
			mBitmapLastWidth = mBitmap.getWidth();
			mBitmapLastHeight = mBitmap.getHeight();
			mFgBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);  
			mFgCanvas = new  Canvas(mFgBitmap);
			mRect = new RectF(0, 0, getWidth(), getHeight());
			mCenterPoint.set(getWidth()/2, getHeight()/2);
			
		}
		setCutPicPosition();
		//清除前景蒙版层
		mFgCanvas.drawPaint(mClearFgPaint);
        
		if(mBitmap != null) {
			if(!isFinishFirstZoomed) {
				scale = getHeight()/mBitmap.getHeight();
				mBitmap = zoomImg(mLastBitmap, (int) (scale*mBitmap.getWidth()), getHeight());
				mBitmapPoint.set((getWidth()-mBitmap.getWidth())/2, 0);
				isFinishFirstZoomed = true;
			}
			//绘画传进来的bitmap
			canvas.drawBitmap(mBitmap, mBitmapPoint.x, mBitmapPoint.y, mPaint);
		}
		
        //绘制圆环  
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mPaint);
        //绘制相交圆  
        mFgCanvas.drawCircle(mCenterPoint.x,mCenterPoint.y, mRadius, mRoundPaint);  
        //绘制半透明矩形
        mFgCanvas.drawRect(mRect, mFgroundPaint) ;
        //将前景蒙版层画在本CutPicView的canvas里
        canvas.drawBitmap(mFgBitmap, null, mRect, mPaint);
	}

	//以下是图片拖动、缩放的逻辑，由于逻辑不易解释，在此也不做多余的注释了，感兴趣的可以研究一下。
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if(mBitmap != null) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			//单指触屏
			case MotionEvent.ACTION_DOWN:
				//设为单指拖动模式
				mCurrMode = MODE_DRAG;
				mFirstDragPoint.set(event.getX(), event.getY());
				mFirstZoomPoint.set(event.getX(), event.getY());
				break;
			//双指触屏
			case MotionEvent.ACTION_POINTER_DOWN:
				//设为双指拖动模式
				mCurrMode = MODE_ZOOM;
				mSecondZoomPoint.set(event.getX(), event.getY());
				oriDis = distance(event); 
				break;
			case MotionEvent.ACTION_MOVE:
				//判断当前模式
				if(mCurrMode == MODE_DRAG) {
					if(isSecondePointerUp) {
						mFirstDragPoint.set(event.getX(), event.getY());
						isSecondePointerUp = false;
					}
					mBitmapPoint.set(mBitmapPoint.x+(int) (event.getX()-mFirstDragPoint.x),
							mBitmapPoint.y+(int) (event.getY()-mFirstDragPoint.y));
					mFirstDragPoint.set(event.getX(), event.getY());
				} else if(mCurrMode == MODE_ZOOM) {
					 float newDist = distance(event);  
		                if (newDist > 10f) {  
		                    float scale = newDist / oriDis;
		                    int x = (int) (mBitmapPoint.x+(mBitmap.getWidth()-scale*mBitmapLastWidth)/2);
		                    int y = (int) (mBitmapPoint.y+(mBitmap.getHeight()-scale*mBitmapLastHeight)/2);
		                    mBitmap = zoomImg(mLastBitmap, (int)(scale*mBitmapLastWidth), (int)(scale*mBitmapLastHeight));
		                    mBitmapPoint.set(x, y);
		                }  
				}
				
				break;
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_POINTER_UP:				
				mBitmapLastWidth = mBitmap.getWidth();
				mBitmapLastHeight = mBitmap.getHeight();
				mCurrMode = MODE_DRAG;
				isSecondePointerUp = true;
				break;
			default:
				break;
			}
			requestLayout();
			invalidate();
		}
		
		return true;
	} 
	/**
	 * 设置你要切割的bitmap
	 * @param bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		mBitmap = bitmap;
	}
	
	/**
     * 转换图片成圆形
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public Bitmap toRoundBitmap() {
       
        canvas.drawRoundRect(rectF, mRadius, mRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(mBitmap, src, dst, paint);
        return output;
    }
	/**
	 * 按比例缩放bitmap
	 * @param bm 原bitmap
	 * @param newWidth 新的宽度
	 * @param newHeight 新的高度
	 * @return 缩放后的bitmap
	 */
	private Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
		if(newHeight==0 || newWidth == 0) {
			return bm;
		}
	   float scale = 0;
	   //获得图片的宽高
	   int width = bm.getWidth();
	   int height = bm.getHeight();
	   //计算缩放比例，优先判断高度
	   if(newHeight > 0) {
		   scale = ((float) newHeight) / height;
		   newWidth = (int) (scale*width);
		   
	   } else {
		   scale = ((float) newWidth) / width;
		   newHeight = (int) (scale*height);
	   }
	   //取得想要缩放的matrix参数
	  
	   Matrix matrix = new Matrix();
	   matrix.postScale(scale, scale);
	   // 得到新的图片
	   Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
	   return newbm;
	}
	
    /**
     *  计算两个触摸点之间的距离   
     * @param event
     * @return
     */
    private float distance(MotionEvent event) {  
        float x = event.getX(0) - event.getX(1);  
        float y = event.getY(0) - event.getY(1);  
        return (float) Math.sqrt(x * x + y * y);  
    }  

    /**
     * 获得屏幕宽度
     * @return
     */
    private int getScreenWidth() {
    	WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    	DisplayMetrics m = new DisplayMetrics();
    	wm.getDefaultDisplay().getMetrics(m);
    	return m.widthPixels;
    }
	
}
