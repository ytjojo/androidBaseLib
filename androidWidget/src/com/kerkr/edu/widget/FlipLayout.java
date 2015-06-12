package com.kerkr.edu.widget;



import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

public class FlipLayout extends InterceptorFrameLayout implements Animation.AnimationListener {
        private static final int DURATION = 650;
    
    private static final Interpolator fDefaultInterpolator = new DecelerateInterpolator();
    
    private OnFlipListener mListener;
    
    private FlipAnimator mAnimation;
    
    private boolean mIsFlipped;
    
    private boolean mIsRotationReversed ;
    
    private View mFrontView, mBackView;
    
    public FlipLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public FlipLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public FlipLayout(Context context) {
        super(context);
    }
    
    private void init() {
        mAnimation = new FlipAnimator();
        mAnimation.setAnimationListener(this);
        mAnimation.setInterpolator(fDefaultInterpolator);
        mAnimation.setDuration(DURATION);
        
        //        mFlipAnimation = new FlipAnimation(0, 30, getWidth()/2, getHeight()/2, 0.7f, ScaleUpDownEnum.SCALE_CYCLE);
        //        mFlipAnimation.setAnimationListener(this);
        //        mFlipAnimation.setInterpolator(fDefaultInterpolator);
        //        mFlipAnimation.setDuration(DURATION);
    }
    
    /**
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.dispatchDraw(canvas);
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        if (getChildCount() > 2) {
            throw new IllegalStateException("FlipLayout can host only two direct children");
        }
        
        mFrontView = getChildAt(1);
        mBackView = getChildAt(0);
    }
    
    private boolean isFrontShow;
    
    public boolean isFrontShow() {
        return isFrontShow;
    }
    
    private void toggleView() {
        if (mFrontView == null || mBackView == null) {
            return;
        }
        
        if (mIsFlipped) {
            mFrontView.setVisibility(View.VISIBLE);
            mBackView.setVisibility(View.GONE);
            isFrontShow = true;
        }
        else {
            mFrontView.setVisibility(View.GONE);
            mBackView.setVisibility(View.VISIBLE);
            isFrontShow = false;
        }
        if (mListener != null) {
            mListener.onShowFrontOrBack(isFrontShow);
        }
        mIsFlipped = !mIsFlipped;
    }
    
    public void setOnFlipListener(OnFlipListener listener) {
        mListener = listener;
    }
    
    public void reset() {
        mIsFlipped = false;
        mIsRotationReversed = false;
        mFrontView.setVisibility(View.VISIBLE);
        mBackView.setVisibility(View.GONE);
    }
    
    public void startFlipAnim() {
        if (mAnimation == null) {
            init();
        }
        mAnimation.setVisibilitySwapped();
        startAnimation(mAnimation);
        //        if(mFlipAnimation == null){
        //            init();
        //        }
        //        startAnimation(mFlipAnimation);
    }
    
    public interface OnFlipListener {
        
        public void onShowFrontOrBack(boolean b);
        
        public void onFlipStart(FlipLayout view);
        
        public void onFlipEnd(FlipLayout view);
    }
    
    public class FlipAnimator extends Animation {
        private Camera camera;
        
        private float centerX;
        
        private float centerY;
        
        private boolean visibilitySwapped;
        
        public static final float TRIGER = 0.3f;
        
        public FlipAnimator() {
            setFillAfter(true);
        }
        
        public void setVisibilitySwapped() {
            visibilitySwapped = false;
        }
        
        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            camera = new Camera();
            this.centerX = width / 2;
            this.centerY = height / 2;
        }
        
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            // Angle around the y-axis of the rotation at the given time. It is
            // calculated both in radians and in the equivalent degrees.
            double radians = Math.PI * interpolatedTime;
            float degrees = (float) (180.0 * radians / Math.PI);
            
            if (mIsRotationReversed) {
                degrees = -degrees;
            }
            
            // Once we reach the midpoint in the animation, we need to hide the
            // source view and show the destination view. We also need to change
            // the angle by 180 degrees so that the destination does not come in
            // flipped around. This is the main problem with SDK sample, it does
            // not
            // do this.
            if (interpolatedTime >= 0.5f) {
                if (mIsRotationReversed) {
                    degrees += 180.f;
                }
                else {
                    degrees -= 180.f;
                }
                
                if (!visibilitySwapped) {
                    toggleView();
                    visibilitySwapped = true;
                }
            }
            
            if (radians <= 5d / 180d * Math.PI || radians >= 175d / 180d * Math.PI) {
                
            }
            else {
                radians = 5d / 180d * Math.PI;
            }
            
            final Matrix matrix = t.getMatrix();
            
            final float curDegrees = getCurDegree(degrees);
            camera.save();
            camera.translate(0.0f, 0.0f, (float) (centerX * Math.sin(radians)));
            camera.rotateX(0);
            camera.rotateY(curDegrees);
            camera.rotateZ(0);
            camera.getMatrix(matrix);
            camera.restore();
            //            
            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
            //            matrix.preScale(1,  getScale( interpolatedTime), centerX, centerY);//
            matrix.postScale(getScale(interpolatedTime), 1, centerX, centerY);//
            //            matrix.setScale(getScale( interpolatedTime), 1, centerX, centerY);//
            t.setAlpha(getScale(interpolatedTime));
            
        }
        
        private float getCurDegree1(float interpolatedTime) {
            
            if (interpolatedTime < 0.5f) {
                if (mIsRotationReversed) {
                    
                    return 5f * interpolatedTime;
                }
                else {
                    return -5f* interpolatedTime;
                }
            }
            else if (interpolatedTime > 0.5) {
                if (mIsRotationReversed) {
                    return 355f + 5f*interpolatedTime; 
                }
                else {
                   return -5f + 5f*interpolatedTime; 
                }
            }
            else {
                return 5;
            }
        }
        
        private float getCurDegree(float degrees) {
            if (degrees >= 0.f && degrees <= 5f) {
                return degrees;
            }
            else if (degrees > 5f && degrees <= 90f) {
                return 5f;
            }
            else if (degrees >= 270f && degrees <= 355f) {
                return 355f;
            }
            else if (degrees >= 355f && degrees <= 360f) {
                return degrees;
            }
            else if (degrees >= -90 && degrees <= -5) {
                return -5;
            }
            else if (degrees >= -5 && degrees <= 0) {
                return degrees;
            }
            return degrees;
        }
        
        private float getScale(float iter) {
            if (iter < 0.5f) {
                return 1 - 2f * iter;
            }
            else if (iter > 0.5) {
                return 2f * iter - 1f;
            }
            else {
                return 0;
            }
            //          if(iter <= TRIGER){
            //              return 1- 2.0f *iter;
            //          }else if (iter > 1- TRIGER){
            //              return 2.0f * iter - 1;
            //          }else{
            //              return 1- 2.0f*TRIGER;
            //          }
            
        }
    }
    
    @Override
    public void onAnimationStart(Animation animation) {
        if (mListener != null) {
            mListener.onFlipStart(this);
        }
    }
    
    @Override
    public void onAnimationEnd(Animation animation) {
        if (mListener != null) {
            mListener.onFlipEnd(this);
        }
//        mIsRotationReversed = !mIsRotationReversed;
    }
    
    @Override
    public void onAnimationRepeat(Animation animation) {
    }
    
    public class NumberRotate3DAnimation extends Animation {
        public static final boolean DEBUG = false;
        
        public static final boolean ROTATE_DECREASE = true;
        
        public static final boolean ROTATE_INCREASE = false;
        
        /** max depth at z axis */
        public static final float DEPTH_Z = 0.0f;
        
        /** duration of the animation。 */
        public static final long DURATION = 500;
        
        private float centerX;
        
        private float centerY;
        
        private Camera camera;
        
        private boolean visibilitySwapped;
        
        public NumberRotate3DAnimation() {
            setDuration(DURATION);
            setFillAfter(true);
        }
        
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            camera = new Camera();
            centerX = width / 2;
            centerY = height / 2;
        }
        
        protected void applyTransformation(float interpolatedTime, Transformation transformation) {
            float from = 0.0f, to = 0.0f;
            if (mIsRotationReversed) {
                from = 0.0f;
                to = 180.0f;
            }
            else {
                from = 360.0f;
                to = 180.0f;
            }
            float degree = from + (to - from) * interpolatedTime;
            boolean overHalf = (interpolatedTime > 0.5f);
            if (overHalf) {
                // this is important ,the number should be readable instead of mirror effect when rotate half point  
                degree = degree - 180;
                if (!visibilitySwapped) {
                    toggleView();
                    visibilitySwapped = true;
                }
            }
            float depth = (0.5f - Math.abs(interpolatedTime - 0.5f)) * DEPTH_Z;
            final Matrix matrix = transformation.getMatrix();
            camera.save();
            camera.translate(0.0f, 0.0f, depth);
            camera.rotateY(degree);
            camera.getMatrix(matrix);
            camera.restore();
            if (DEBUG) {
                if (overHalf) {
                    matrix.preTranslate(-centerX * 2, -centerY);
                    matrix.postTranslate(centerX * 2, centerY);
                }
            }
            else {
                //make sure the animate view is at the center of the layout during animation
                matrix.preTranslate(-centerX, -centerY);
                matrix.postTranslate(centerX, centerY);
            }
        }
        
        public void setVisibilitySwapped() {
            visibilitySwapped = false;
        }
        
    }
    
}