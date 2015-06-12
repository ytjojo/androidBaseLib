package com.kerkr.edu.widget;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/**
 * 
 * <瓜瓜乐效果，覆盖在TextView上>
 * <功能详细描述>
 * 
 * @author  lijing
 * @version  [版本号, 2015-5-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */

public class EraseView extends View {
    
    private boolean isMove = false;
    private Bitmap bitmap = null;
    private Bitmap frontBitmap = null;
    private Path path;
    private Canvas mCanvas;
    private Paint paint;
    
    
    public EraseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        
        canvas.save();
        canvas.clipRect(0, 0, getWidth(), getHeight());
        if (mCanvas == null) {
            eraseBitmp();
        } 
        canvas.drawBitmap(bitmap, 0, 0, null);    
        mCanvas.drawPath(path,paint);
        canvas.restore();
        super.onDraw(canvas);
    }
    
    public void eraseBitmp() {
        
        bitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_4444);
        
        frontBitmap = CreateBitmap(Color.GRAY,getWidth(),getHeight());

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(20);
        
        path = new Path();

        mCanvas = new Canvas(bitmap);
        mCanvas.drawBitmap(frontBitmap, 0, 0,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ax = event.getX();
        float ay = event.getY();
        
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isMove = false;
            path.reset();
            path.moveTo(ax, ay);
            invalidate();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            isMove = true;
            path.lineTo(ax,ay);
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }
    
    public  Bitmap CreateBitmap(int color,int width, int height) {
        int[] rgb = new int [width * height];
        
        for (int i=0;i<rgb.length;i++) {
            rgb[i] = color;
        }
        
        return Bitmap.createBitmap(rgb, width, height,Config.ARGB_8888);
    }
    
}