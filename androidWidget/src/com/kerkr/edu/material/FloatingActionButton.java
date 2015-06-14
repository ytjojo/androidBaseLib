package com.kerkr.edu.material;

import com.kerkr.edu.design.ScreenUtils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageButton;

public class FloatingActionButton extends ImageButton {

    private Interpolator mInterpolator;

    private Bitmap mBitmap;

    private Paint mDrawablePaint;

    private float currentY;

    private boolean mHidden;

    private boolean isAttached;

    private int mScreenHeight;

    public FloatingActionButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        mInterpolator = new AccelerateDecelerateInterpolator();

        mScreenHeight = getScreenHeight(context);

        // final float elevationPx = ViewHelper.getPxFromDP(getResources(), 1);
        // setElevation(elevationPx);

        // final int size = (int) ResourceHelper.getDimensionPixelSize(R.dimen.fab_size);
        // Outline outline = new Outline();
        // outline.setOval(0, 0, size - (int) elevationPx, size - (int) elevationPx);
        // setOutline(outline);
        // setClipToOutline(true);

        setScaleType(ScaleType.FIT_CENTER);
        // setStateListAnimator(AnimatorInflater.loadStateListAnimator(context,
        // R.drawable.fab_button_states));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        }

        if (mBitmap == null) {
            return;
        }

        canvas.drawBitmap(mBitmap, (getWidth() - mBitmap.getWidth()) / 2, (getHeight() - mBitmap.getHeight()) / 2, mDrawablePaint);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        mBitmap = ((BitmapDrawable) drawable).getBitmap();
    }

    public void setImageDrawableColor(int color) {
        if (mDrawablePaint == null) {
            mDrawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        mDrawablePaint.setColor(color);
    }

    private int getScreenHeight(Context context) {
        return ScreenUtils.getScreenHeight(context);
    }

    public void hide() {
        if (mHidden) {
            return;
        }

        currentY =ViewHelper.getTranslationY(this);

        ObjectAnimator  animator = ObjectAnimator.ofFloat(this, "translationY", mScreenHeight);
        animator.setInterpolator(mInterpolator);
        animator.start();

        mHidden = true;
    }

    public void show() {
        if (mHidden == false) {
            return;
        }

        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", currentY);
        animator.setInterpolator(mInterpolator);
        animator.start();

        mHidden = false;
    }

    public boolean isAttached() {
        return isAttached;
    }

    public void setAttached(boolean isAttached) {
        this.isAttached = isAttached;
    }
}