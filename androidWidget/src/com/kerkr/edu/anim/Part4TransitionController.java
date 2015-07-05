package com.kerkr.edu.anim;

import android.app.Activity;
import android.view.ViewGroup;


import java.lang.ref.WeakReference;

import com.ytjojo.widget.R;

public class Part4TransitionController extends TransitionController {
    private static int[] VIEW_IDS ;

    Part4TransitionController(WeakReference<Activity> activityWeakReference, AnimatorBuilder animatorBuilder) {
        super(activityWeakReference, animatorBuilder);
    }

    public static TransitionController newInstance(Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        AnimatorBuilder animatorBuilder = AnimatorBuilder.newInstance(activity);
        return new Part4TransitionController(activityWeakReference, animatorBuilder);
    }

    @Override
    protected void enterInputMode(Activity activity) {
        createTransitionAnimator(activity);
        activity.setContentView(0);
    }

    @Override
    protected void exitInputMode(Activity activity) {
        createTransitionAnimator(activity);
        activity.setContentView(1);
    }

    private void createTransitionAnimator(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);

        TransitionAnimator.begin(parent, VIEW_IDS);
    }
}
