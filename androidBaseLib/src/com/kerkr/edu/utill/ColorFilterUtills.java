package com.kerkr.edu.utill;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class ColorFilterUtills {
	
	/**   
	   * 按下这个按钮进行的颜色过滤   
	   */    
	  public final static float[] BT_SELECTED=new float[] {      
	      2, 0, 0, 0, 2,      
	      0, 2, 0, 0, 2,      
	      0, 0, 2, 0, 2,      
	      0, 0, 0, 1, 0 };     
	       
	  /**   
	   * 按钮恢复原状的颜色过滤   
	   */    
	  public final static float[] BT_NOT_SELECTED=new float[] {      
	      1, 0, 0, 0, 0,      
	      0, 1, 0, 0, 0,      
	      0, 0, 1, 0, 0,      
	      0, 0, 0, 1, 0 };     
	public static void doBrightness(Paint paint){
	     int brightness = 60 - 127;    
	        ColorMatrix cMatrix = new ColorMatrix();    
	        cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1,    
	                0, 0, brightness,// 改变亮度     
	                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });    
	         
	        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));    
	}
	
	public static void doGray(Drawable d){
		//Make this drawable mutable.     
		//A mutable drawable is guaranteed to not share its state with any other drawable.     
		d.mutate();     
		ColorMatrix cm = new ColorMatrix();     
		cm.setSaturation(0);     
		ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);     
		d.setColorFilter(cf);
		
	}
	public static void doChange(Drawable d){
		d.setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
//		d.setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
	}
}
