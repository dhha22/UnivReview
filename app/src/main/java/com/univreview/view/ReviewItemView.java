package com.univreview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.univreview.R;
import com.univreview.model.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 23..
 */
public class ReviewItemView extends FrameLayout {
    @BindView(R.id.name_txt) TextView nameTxt;
    @BindView(R.id.auth_mark) TextView authMarkTxt;
    @BindView(R.id.time_txt) TextView timeTxt;
    @BindView(R.id.review_txt) TextView reviewTxt;
    @BindView(R.id.difficulty_txt) TextView difficultyTxt;
    @BindView(R.id.assignment_txt) TextView assignmentTxt;
    @BindView(R.id.attendance_txt) TextView attendanceTxt;
    @BindView(R.id.grade_txt) TextView gradeTxt;
    @BindView(R.id.achievement_txt) TextView achievementTxt;

    public ReviewItemView(Context context) {
        this(context, null);
    }

    public ReviewItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReviewItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.review_item, this, true);
        ButterKnife.bind(this);
    }

    public void setData(Review review) {
        if (review != null) {
            if (review.user != null) {
                nameTxt.setText(review.user.name);
                if (review.user.authenticated) {
                    authMarkTxt.setVisibility(VISIBLE);
                } else {
                    authMarkTxt.setVisibility(INVISIBLE);
                }
            }
            if(review.reviewDetail != null) {
                if (review.reviewDetail.length() > 0) {
                    reviewTxt.setVisibility(VISIBLE);
                } else {
                    reviewTxt.setVisibility(GONE);
                }
                reviewTxt.setText(review.reviewDetail);
            }
            timeTxt.setText(review.createdDate);
            difficultyTxt.setText(String.valueOf(review.difficultyRate));
            assignmentTxt.setText(String.valueOf(review.assignmentRate));
            attendanceTxt.setText(String.valueOf(review.attendanceRate));
            gradeTxt.setText(String.valueOf(review.gradeRate));
            achievementTxt.setText(String.valueOf(review.achievementRate));

        }
    }
}
