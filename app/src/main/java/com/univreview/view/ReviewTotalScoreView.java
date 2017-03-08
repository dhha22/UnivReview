package com.univreview.view;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.R;
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

    public void setData(){
        averageScoreTxt.setText("3.5");
        averageIndicator.setRating(3.5f);
        Review review = new Review();
        review.difficultyRate =3;
        review.assignmentRate = 2;
        review.attendanceRate = 4;
        review.gradeRate = 1;
        review.achievementRate = 2;
        reviewRatingIndicatorView.setData(review);
    }
}