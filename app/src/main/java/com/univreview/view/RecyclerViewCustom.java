package com.univreview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by DavidHa on 2017. 1. 21..
 */
public class RecyclerViewCustom extends RecyclerView {
    public RecyclerViewCustom(Context context) {
        this(context, null);
    }

    public RecyclerViewCustom(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewCustom(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

}
