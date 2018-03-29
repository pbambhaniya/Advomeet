package com.multipz.atmiyalawlab.Util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

/**
 * Created by Admin on 31-01-2018.
 */

public class ProgressLoader implements ObjectAnimator.AnimatorListener {

    private Context context;
    private ProgressBar progressBar;
    private View view;

    public ProgressLoader(Context context, ProgressBar progressBar, View view) {
        this.context = context;
        this.progressBar = progressBar;
        this.view = view;
    }

    @SuppressLint("WrongConstant")
    public static void PLoader(Context context, ProgressBar progressBar) {
        ObjectAnimator anim = ObjectAnimator.ofInt(progressBar, "progress", 10, 80);
        anim.setDuration(2000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.INFINITE);
        anim.start();

    }

    @Override
    public void onAnimationStart(Animator animator) {
        animator.start();
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        view.setVisibility(View.GONE);

    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {
        animator.start();
    }
}
