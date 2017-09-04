package com.univreview.view;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.R;
import com.univreview.log.Logger;
import com.univreview.model.Review;
import com.univreview.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 2. 20..
 */
public class ReviewTotalScoreView extends CardView {
    @BindView(R.id.average_score_txt) TextView averageScoreTxt;
    @BindView(R.id.average_indicator) AppCompatRatingBar averageIndicator;
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
        CardView.LayoutParams params = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, (int)Util.dpToPx(context, 154), 0, App.dp12);
        setCardElevation(App.dp1);
        setLayoutParams(params);
    }

    public void setData(float rate, Review review) {
        float averageScore = Math.round(rate * 100f) / 100f;
        Logger.v("rate: " + rate);
        if (rate != 0) {
            setVisibility(VISIBLE);
            averageScoreTxt.setText(String.valueOf(averageScore));
            averageIndicator.setRating(averageScore);
           // reviewRatingIndicatorView.setData(review);
        } else {
            setVisibility(GONE);
        }
    }
}
