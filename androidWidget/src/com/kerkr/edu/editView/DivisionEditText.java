package com.kerkr.edu.editView;
 


import com.ytjojo.widget.R;

import android.content.Context;  
import android.content.res.TypedArray;  
import android.text.Editable;  
import android.text.TextWatcher;  
import android.util.AttributeSet;  
import android.view.View;  
import android.widget.EditText;  
  
/** 
 * �ָ������ 
 *  
 * @author Administrator 
 *  
 */  
public class DivisionEditText extends EditText {  
  
    /* �������� */  
    private String[] text;  
    /* ����ʵ�ʳ��� (����+�ָ���) */  
    private Integer length;  
    /* ��������ĳ��� */  
    private Integer totalLength;  
    /* ÿ��ĳ��� */  
    private Integer eachLength;  
    /* �ָ��� */  
    private String delimiter;  
    /* ռλ�� */  
    private String placeHolder;  
  
    public DivisionEditText(Context context) {  
        super(context);  
    }  
  
    public DivisionEditText(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        try {  
            // ��ʼ������  
            TypedArray typedArray = context.obtainStyledAttributes(attrs,  
                    R.styleable.EditText);  
            this.totalLength = typedArray.getInteger(  
                    R.styleable.EditText_totalLength, 0);  
            this.eachLength = typedArray.getInteger(  
                    R.styleable.EditText_eachLength, 0);  
            this.delimiter = typedArray  
                    .getString(R.styleable.EditText_delimiter);  
            if (this.delimiter == null || this.delimiter.length() == 0) {  
                this.delimiter = "-";  
            }  
            this.placeHolder = typedArray  
                    .getString(R.styleable.EditText_placeHolder);  
            if (this.placeHolder == null || this.placeHolder.length() == 0) {  
                this.placeHolder = " ";  
            }  
            typedArray.recycle();  
  
            // ��ʼ��  
            init();  
  
            // ���ݱ仯����  
            this.addTextChangedListener(new DivisionTextWatcher());  
            // ��ȡ�������  
            this.setOnFocusChangeListener(new DivisionFocusChangeListener());  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public DivisionEditText(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    /** 
     * ��ʼ�� 
     */  
    public void init() {  
        // �ܹ��ּ���  
        int groupNum = 0;  
        // ���ÿ�鳤��(����)��Ϊ0,����  
        if (this.eachLength != 0) {  
            groupNum = this.totalLength / this.eachLength;  
        }  
        // ʵ�ʳ���  
        length = this.totalLength + this.eachLength != 0 ? this.totalLength  
                + groupNum - 1 : 0;  
        // ��ʼ������  
        text = new String[this.length];  
        // ��������С����0,��ʼ����������  
        // �ո�ռλ,�ָ���ռλ  
        if (length > 0) {  
            for (int i = 0; i < length; i++) {  
                if (i != 0 && (i + 1) % (this.eachLength + 1) == 0) {  
                    text[i] = this.delimiter;  
                } else {  
                    text[i] = placeHolder;  
                }  
            }  
            // �����ı�  
            mySetText();  
            // ���ý���  
            mySetSelection();  
        }  
    }  
  
    /** 
     * ��ȡ��� 
     *  
     * @return 
     */  
    public String getResult() {  
        StringBuffer buffer = new StringBuffer();  
        for (String item : text) {  
            if (!placeHolder.equals(item) && !delimiter.equals(item)) {  
                buffer.append(item);  
            }  
        }  
        return buffer.toString();  
    }  
  
    /** 
     * �ı����� 
     *  
     * @author Administrator 
     *  
     */  
    private class DivisionTextWatcher implements TextWatcher {  
  
        @Override  
        public void afterTextChanged(Editable s) {  
  
        }  
  
        @Override  
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  
        }  
  
        @Override  
        public void onTextChanged(CharSequence s, int start, int before,  
                int count) {  
            // ���ǰ����С�����鳤��,��Ϊʹ���˸�  
            if (s.length() < length) {  
                // �������λ��  
                int index = DivisionEditText.this.getSelectionStart();  
                // ɾ����ַ�  
                String deleteStr = text[index];  
                // ����Ƿָ���,ɾ��ָ���ǰһ��  
                if (delimiter.equals(deleteStr)) {  
                    index--;  
                }  
                // �ÿ�  
                text[index] = placeHolder;  
                // �鿴ǰһ���Ƿ�Ϊ�ָ���  
                if (index - 1 >= 0) {  
                    if (delimiter.equals(text[index - 1])) {  
                        index--;  
                    }  
                }  
                // �����ı�  
                mySetText();  
                // ���ý���  
                mySetSelection(index);  
            }  
            // ֻ��һ��һ���ַ�����  
            if (count == 1) {  
                // �ӹ����ʼ,�Ƿ��пյ�λ��  
                int index = isBlank(DivisionEditText.this.getSelectionStart());  
                // �����  
                if (index != -1) {  
                    // ������ڵ��ַ�  
                    String allStr = s.toString();  
                    // ������ַ�  
                    String inputStr = allStr.substring(start, start + count);  
                    // �滻ռλ��  
                    text[index] = inputStr;  
                }  
                // �����ı�  
                mySetText();  
                // ���ý���  
                mySetSelection();  
            }  
        }  
    }  
  
    /** 
     * ��ȡ������� 
     *  
     * @author Administrator 
     *  
     */  
    private class DivisionFocusChangeListener implements OnFocusChangeListener {  
  
        @Override  
        public void onFocusChange(View v, boolean hasFocus) {  
            if (hasFocus) {  
                // ���ý���  
                mySetSelection(0);  
            }  
        }  
    }  
  
    /** 
     * �����ı� 
     *  
     * @param text 
     */  
    private void mySetText() {  
        StringBuffer buffer = new StringBuffer();  
        for (String item : text) {  
            buffer.append(item);  
        }  
        // �����ı�  
        setText(buffer);  
    }  
  
    /** 
     * ���ý��� 
     *  
     * @param text 
     */  
    private void mySetSelection() {  
        mySetSelection(fullSelection());  
    }  
  
    /** 
     * ���ý��� 
     *  
     * @param text 
     */  
    private void mySetSelection(int index) {  
        DivisionEditText.this.setSelection(index);  
    }  
  
    /** 
     * �ӹ��λ����ʼ,�������Ƿ��пյ�ռλ�� 
     *  
     * @param text 
     * @param selection 
     * @return 
     */  
    private int isBlank(int selection) {  
        int index = -1;  
        for (int i = selection - 1; i < length; i++) {  
            if (placeHolder.equals(text[i])) {  
                index = i;  
                break;  
            }  
        }  
        return index;  
    }  
  
    /** 
     * ���һ�����յ��ַ��Ĺ��λ�� 
     *  
     * @param text 
     * @return 
     */  
    private int fullSelection() {  
        int index = 0;  
        for (int i = 0; i < length; i++) {  
            if (!placeHolder.equals(text[i]) && !delimiter.equals(text[i])) {  
                index = i + 1;  
            }  
        }  
        return index;  
    }  
  
    public Integer getTotalLength() {  
        return totalLength;  
    }  
  
    public void setTotalLength(Integer totalLength) {  
        this.totalLength = totalLength;  
    }  
  
    public Integer getEachLength() {  
        return eachLength;  
    }  
  
    public void setEachLength(Integer eachLength) {  
        this.eachLength = eachLength;  
    }  
  
    public String getDelimiter() {  
        return delimiter;  
    }  
  
    public void setDelimiter(String delimiter) {  
        this.delimiter = delimiter;  
    }  
  
    public String getPlaceHolder() {  
        return placeHolder;  
    }  
  
    public void setPlaceHolder(String placeHolder) {  
        this.placeHolder = placeHolder;  
    }  
  
}  