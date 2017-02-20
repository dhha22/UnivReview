package com.univreview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * Created by DavidHa on 2017. 2. 20..
 */
public class ReviewTotalScoreView extends FrameLayout {
    public ReviewTotalScoreView(Context context) {
        this(context, null);
    }

    public ReviewTotalScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReviewTotalScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //LayoutInflater.from(context).inflate()
    }
}
