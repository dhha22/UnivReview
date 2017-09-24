package com.univreview.view;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.univreview.R;
import com.univreview.log.Logger;
import com.univreview.model.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 2. 26..
 */
public class ReviewRatingIndicatorView extends FrameLayout {
    @BindView(R.id.difficulty_rate) AppCompatRatingBar difficultyRate;
    @BindView(R.id.assignment_rate) AppCompatRatingBar assignmentRate;
    @BindView(R.id.attendance_rate) AppCompatRatingBar attendanceRate;
    @BindView(R.id.grade_rate) AppCompatRatingBar gradeRate;
    @BindView(R.id.achievement_rate) AppCompatRatingBar achievementRate;
    @BindView(R.id.difficulty_txt) TextView difficultyTxt;
    @BindView(R.id.assignment_txt) TextView assignmentTxt;
    @BindView(R.id.attendance_txt) TextView attendaceTxt;
    @BindView(R.id.grade_txt) TextView gradeTxt;
    @BindView(R.id.achievement_txt) TextView achievementTxt;

    public ReviewRatingIndicatorView(Context context) {
        this(context, null);
    }

    public ReviewRatingIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReviewRatingIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.review_rating_indicator, this, true);
        ButterKnife.bind(this);
    }

    public void setData(Review review){
        Logger.v("review rating: " + review);
        difficultyRate.setRating(review.getDifficultyRate());
        assignmentRate.setRating(review.getAssignmentRate());
        attendanceRate.setRating(review.getAttendanceRate());
        gradeRate.setRating(review.getGradeRate());
        achievementRate.setRating(review.getAchievementRate());
        difficultyTxt.setText(review.getDifficultyRateMessage());
        assignmentTxt.setText(review.getAssignmentRateMessage());
        attendaceTxt.setText(review.getAttendanceRateMessage());
        gradeTxt.setText(review.getGradeRateMessage());
        achievementTxt.setText(review.getAchievementRateMessage());
    }
}
