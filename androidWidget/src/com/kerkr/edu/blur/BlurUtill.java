package com.kerkr.edu.blur;

import java.io.IOException;
import java.util.Calendar;

import com.kerkr.edu.app.CompatUtil;
import com.kerkr.edu.design.FastBlur;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class BlurUtill {



	public static Bitmap getActivityBackBitmap(Activity activity) {

		View decorView = activity.getWindow().getDecorView();
		decorView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
		decorView.setDrawingCacheEnabled(true);
		decorView.buildDrawingCache();
		Bitmap bitmap = decorView.getDrawingCache();
		decorView.destroyDrawingCache();
		decorView.setDrawingCacheEnabled(false);
		return bitmap;
	}

	public static Bitmap blurView(Bitmap bkg, View view, boolean isDownScale,
			int alpha, int filterColor) {
		long startMs = System.currentTimeMillis();
		float scaleFactor = 1;
		float radius = 20;
		if (isDownScale) {
			scaleFactor = 8;
			radius = 2;
		}

		Bitmap overlay = Bitmap.createBitmap(
				(int) (view.getMeasuredWidth() / scaleFactor),
				(int) (view.getMeasuredHeight() / scaleFactor),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
				/ scaleFactor);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setAlpha(alpha);
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(bkg, 0, 0, paint);

		overlay = FastBlur.doBlurRenderScript(view.getContext(), overlay,
				(int) radius, true);

		Drawable drawable = new BitmapDrawable(
				view.getContext().getResources(), overlay);
		if (filterColor >= 0) {
			drawable.setColorFilter(filterColor, Mode.MULTIPLY);
			// ToDo
			// 当用带alpha的颜色值时候无效，Color.parse("#20000000");
		}
		CompatUtil.setBackground(view, drawable);
		return overlay;
	}

	
	/**
	 * 截图动作
	 */
	public static void runCapture() {
		new Thread() {
			public void run() {
				long capTime = Calendar.getInstance().getTimeInMillis();
				String capturePicPath = Environment.getExternalStoragePublicDirectory(
						Environment.DIRECTORY_PICTURES).getPath()
						+ "/" + capTime + ".png";
				try {
					Runtime.getRuntime().exec(
							new String[] { "su", "-c",
									"screencap "+ capturePicPath });

					// notify
					/*runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(),
									"Save in " + capturePicPath,
									Toast.LENGTH_SHORT).show();
						}
					});*/
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	public static Bitmap drawViewToBitmap(View view, int width, int height,
			float downSampling) {
		return drawViewToBitmap(view, width, height, 0f, 0f, downSampling);
	}

	public static Bitmap drawViewToBitmap(View view, int width, int height,
			float translateX, float translateY, float downSampling) {
		float scale = 1f / downSampling;
		int bmpWidth = (int) (width * scale - translateX / downSampling);
		int bmpHeight = (int) (height * scale - translateY / downSampling);
		Bitmap dest = Bitmap.createBitmap(bmpWidth, bmpHeight,
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(dest);
		c.translate(-translateX / downSampling, -translateY / downSampling);
		if (downSampling > 1) {
			c.scale(scale, scale);
		}
		view.draw(c);
		return dest;
	}

	public static void setAlpha(View view, float alpha) {
		ViewHelper.setAlpha(view, alpha);
	}

	public static void animateAlpha(final View view, float fromAlpha, float toAlpha, int duration, final Runnable endAction) {
   
    	ViewPropertyAnimator animator = ViewPropertyAnimator.animate(view).alpha(toAlpha).setDuration(duration).setListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				view.post(endAction);
				
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    }
}
