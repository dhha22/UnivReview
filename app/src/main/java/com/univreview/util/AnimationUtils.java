package com.univreview.util;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;

import com.univreview.R;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 1. 19..
 */
public class AnimationUtils {
    public static void registerCircularRevealAnimation(final Context context, final View view, final RevealAnimationSetting revealSettings, final int startColor, final int endColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    int cx = revealSettings.getCenterX();
                    int cy = revealSettings.getCenterY();
                    int width = revealSettings.getWidth();
                    int height = revealSettings.getHeight();
                    int duration = context.getResources().getInteger(android.R.integer.config_mediumAnimTime);

                    //Simply use the diagonal of the view
                    float finalRadius = (float) Math.sqrt(width * width + height * height);
                    Animator anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius).setDuration(duration);
                    anim.setInterpolator(new FastOutSlowInInterpolator());
                    anim.start();
                    startColorAnimation(view, startColor, endColor, duration);
                }
            });
        }
    }

    static void startColorAnimation(final View view, final int startColor, final int endColor, int duration) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(startColor, endColor);
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        anim.setDuration(duration);
        anim.start();
    }

    public static void fadeIn(Context context, View view) {
        if (view.getVisibility() == View.INVISIBLE || view.getVisibility() == View.GONE) {
            Animation fadeInAnimation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_in);
            view.startAnimation(fadeInAnimation);
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void fadeOut(Context context, View view){
        if(view.getVisibility() == View.VISIBLE) {
            Animation fadeInAnimation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_out);
            view.startAnimation(fadeInAnimation);
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void setScale(View view, float value){
        view.setScaleX(value);
        view.setScaleY(value);
    }
}





