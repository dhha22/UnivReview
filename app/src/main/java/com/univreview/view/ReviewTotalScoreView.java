package com.univreview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.univreview.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 2. 20..
 */
public class ReviewTotalScoreView extends FrameLayout {
    @BindView(R.id.review_rating_indicator_view) ReviewRatingIndicatorView reviewRatingIndicatorView;
    public ReviewTotalScoreView(Context context) {
        this(context, null);
    }

    public ReviewTotalScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReviewTotalScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.review_total_score, this, true);
        ButterKnife.bind(this);
        setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setData(){

    }
}
