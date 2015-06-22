package com.kerkr.edu.viewUtill;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class EditeTextUtil {
    /**
     * 设置EditText光标的位置
     *
     * @param editText
     * @param position
     */
    public static void setEditTextCursorPosition(EditText editText, int position) {
        Selection.setSelection(editText.getText(), position);
    }

    /**
     * edittext不显示软键盘,要显示光标
     */
    public void initPhoneEditText(EditText numEditText) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            numEditText.setInputType(InputType.TYPE_NULL);
        } else {
            ((Activity) numEditText.getContext()).getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(numEditText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void setSectionLast(EditText et) {
		CharSequence textStr = et.getText();
		if (textStr == null) {
			return;
		}
		if (textStr.length() == 0) {
			return;
		}
		if (textStr instanceof Spannable) {
			Spannable spannableText = (Spannable) textStr;
			Selection.setSelection(spannableText, textStr.length());
		}
	}

	public static void showSoft(EditText edit) {
		edit.requestFocus();
		InputMethodManager imm = (InputMethodManager) edit.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		// imm.showSoftInput(edit, 0);
	}



	public static void hiddenSoft(View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	public static void showKeyboard(final EditText editText){
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				InputMethodManager inputManager =
				(InputMethodManager) editText.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(editText, 0);
			}
		},
		500);
	}
	/*
	 * 输入框的长度限制
	 */
	public static void lengthConfig(EditText et, int length) {
		et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(length) });
	}
	/**
	 * 
	 * 始终 不弹出软键盘
	 * 
	 */
	public static void setAlwaysHidden(EditText edit){
		edit.setInputType(InputType.TYPE_NULL);
	}
	public static void setEditTextEnable(EditText mInputEditText){
		mInputEditText.setFocusable(true);
		mInputEditText.setFocusableInTouchMode(true);
		mInputEditText.requestFocus();
//		mInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
	}
	public static void setEditTextTouchable(final EditText mEditText){
		  mEditText.setOnTouchListener(new OnTouchListener(){
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	                int inType = mEditText.getInputType(); // backup the input type  
	                mEditText.setInputType(InputType.TYPE_NULL); // disable soft input      
	                mEditText.onTouchEvent(event); // call native handler      
	                mEditText.setInputType(inType); // restore input type     
//	                mEditText.setSelection(mEditText.getText().length());  
	                return true;  
	            }

	        });
	}
}
