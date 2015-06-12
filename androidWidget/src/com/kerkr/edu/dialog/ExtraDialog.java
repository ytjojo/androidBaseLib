package com.kerkr.edu.dialog;


import com.ytjojo.widget.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Create custom Dialog windows for your application Custom dialogs rely on
 * custom layouts wich allow you to create and use your own look & feel.
 * <p/>
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 *
 * @author antoine vianey
 */
public class ExtraDialog extends Dialog {
    
    public ExtraDialog(Context context, int theme) {
        super(context, theme);
    }
    
    public ExtraDialog(Context context) {
        super(context);
    }
    
    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {
        
        private Context context;
        
        private String title;
        
        private String message;
        
        private String positiveButtonText;
        
        private String negativeButtonText;
        
        private View contentView;
        
        private int resouceId;
        
        private int mTitleViewId;
        
        private int mMessageViewId;
        
        private int mPositiveViewId;
        
        private int mNegativeViewId;
        
        private int mContentViewId;
        
        //带图片显示
        private String urlString;
        
        private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener;
        
        public Builder(Context context, int resId) {
            this.context = context;
            this.resouceId = resId;
        }
        
        /**
         * Set the Dialog message from String
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message,int id) {
            this.message = message;
            this.mMessageViewId = id;
            return this;
        }
        
        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message,int id) {
            return setMessage((String) context.getText(message), id);
        }
        
        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title, int id) {
            String content = (String) context.getText(title);
            return setTitle(content, id);
        }
        
        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title ,int id) {
            this.title = title;
            this.mTitleViewId = id;
            return this;
        }
        
        /**
         * Set a custom content view for the Dialog. If a message is set, the
         * contentView is not added to the Dialog...
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v,int replaceId) {
            this.contentView = v;
            this.mContentViewId = replaceId;
            return this;
        }
        
        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener,int id) {
            String content = (String) context.getText(positiveButtonText);
            return setPositiveButton(content,listener,id);
        }
        
        /**
         * Set the positive button text and it's listener
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener ,int id) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            this.mPositiveViewId =id;
            return this;
        }
        
        /**
         * Set the negative button resource and it's listener
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener,int id) {
            String content = (String) context.getText(negativeButtonText);
            return setNegativeButton(content, listener, id);
        }
        
        /**
         * Set the negative button text and it's listener
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener,int id) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            this.mNegativeViewId = id;
            return this;
        }
        
        /**
         * Create the custom dialog
         */
        public ExtraDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final ExtraDialog dialog = new ExtraDialog(context, R.style.Dialog_Translucent_NoTitle);
            View layout = inflater.inflate(resouceId, null);
            dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            if (mTitleViewId != -1) {
                ((TextView) layout.findViewById(mTitleViewId)).setText(title);
            }
            TextView positiveTv = null;
            TextView negativeTv = null;
            // set the confirm button
            if (positiveButtonText != null && mPositiveViewId != -1) {
                positiveTv = (TextView) layout.findViewById(mPositiveViewId);
                positiveTv.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    positiveTv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            }
            else {
                // if no confirm button just set the visibility to GONE
                positiveTv.setVisibility(View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null && mNegativeViewId != -1) {
                negativeTv = (TextView) layout.findViewById(mNegativeViewId);
                negativeTv.setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    negativeTv.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            }
            else {
                // if no confirm button just set the visibility to GONE
                negativeTv.setVisibility(View.GONE);
            }
            // set the content message
            if (message != null && mMessageViewId != -1) {
                TextView mMessageTv = (TextView) layout.findViewById(mMessageViewId);
                mMessageTv.setText(message);
            }
            else if (contentView != null && mContentViewId != -1) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(mContentViewId)).removeAllViews();
                ((LinearLayout) layout.findViewById(mContentViewId)).addView(contentView, new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }
        
    }
    
}
