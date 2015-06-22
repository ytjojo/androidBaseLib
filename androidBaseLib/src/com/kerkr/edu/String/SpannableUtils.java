package com.kerkr.edu.String;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

/**
 * 可以处理图标和文字添加到EditText中
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-4-16 下午7:50:24 $
 */
public abstract class  SpannableUtils {

   

    /**
     * 图片代替文字
     * 
     * @param context
     * @param text
     * @param bitmap
     * @return
     */
    public static SpannableString getSpannableStringByTextReplaceBitmap(Context context, String text, Bitmap bitmap) {
        ImageSpan imageSpan = new ImageSpan(context, bitmap);

        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(imageSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
    
    /** 
    * 超链接 
    */  
    private static void addUrlSpan(TextView tv,String str,int start,int end,String url) {  
        SpannableString spanString = new SpannableString(str);  
        URLSpan span = new URLSpan(url);  
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        tv.append(spanString);  
    }  
      
      
    /** 
    * 文字背景颜色 
    */  
    private static void addBackColorSpan(TextView tv,String str,int start,int end,int color) {  
        SpannableString spanString = new SpannableString(str);  
        BackgroundColorSpan span = new BackgroundColorSpan(color);  
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        tv.append(spanString);  
    }  
      
      
    /** 
    * 文字颜色 
    */  
    private static void addForeColorSpan(TextView tv,String str,int start,int end,int color) {  
        SpannableString spanString = new SpannableString(str);  
        ForegroundColorSpan span = new ForegroundColorSpan(color);  
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        tv.append(spanString);  
    }  
      
      
    /** 
    * 字体大小 
    */  
    private static void addFontSpan(TextView tv,String str,int start,int end) {  
        SpannableString spanString = new SpannableString(str);  
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(36);  
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        tv.append(spanString);  
    }  
      
      
    /** 
    * 粗体，斜体 
    */  
    private static void addStyleSpan(TextView tv,String str,int start,int end) {  
        SpannableString spanString = new SpannableString(str);  
        StyleSpan span = new StyleSpan(Typeface.BOLD_ITALIC);  
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        tv.append(spanString);  
    }  
      
      
    /** 
    * 删除线 
    */  
    private  static void addStrikeSpan(TextView tv,String str,int start,int end) {  
        SpannableString spanString = new SpannableString(str);  
        StrikethroughSpan span = new StrikethroughSpan();  
        spanString.setSpan(span, start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        tv.append(spanString);  
    }  
      
    /** 
    * 下划线 
    */  
    private static void addUnderLineSpan(TextView tv,String str,int start,int end) {  
        SpannableString spanString = new SpannableString(str);  
        UnderlineSpan span = new UnderlineSpan();  
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        tv.append(spanString);  
    }  
      
      
   
}