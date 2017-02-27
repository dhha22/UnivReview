package com.univreview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.R;
import com.univreview.model.Review;
import com.univreview.util.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 23..
 */
public class ReviewItemView extends FrameLayout {
    @BindView(R.id.first_layout) LinearLayout firstLayout;
    @BindView(R.id.second_layout) LinearLayout secondLayout;
    @BindView(R.id.name_txt) TextView nameTxt;
    @BindView(R.id.auth_mark) TextView authMarkTxt;
    @BindView(R.id.subject_txt) TextView subjectTxt;
    @BindView(R.id.professor_txt) TextView professorTxt;
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
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, App.dp5);
        setLayoutParams(params);
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
            subjectTxt.setText(review.subject.getName());
            professorTxt.setText(review.professor.getName() + " 교수님");
//            timeTxt.setText(new TimeUtil().getPointFormat(review.createdDate));
            difficultyTxt.setText(review.getDifficultyRateMessage());
            assignmentTxt.setText(review.getAssignmentRateMessage());
            attendanceTxt.setText(review.getAttendanceRateMessage());
            gradeTxt.setText(review.getGradeRateMessage());
            achievementTxt.setText(review.getAchievementRateMessage());

        }
    }

    public void setMode(Status status){
        if(status == Status.WRITE_REVIEW || status == Status.MY_REVIEW){
            firstLayout.setVisibility(GONE);
            secondLayout.setVisibility(VISIBLE);
        }else if(status == Status.READ_REVIEW){
            firstLayout.setVisibility(VISIBLE);
            secondLayout.setVisibility(GONE);
        }
    }
    public enum Status {
        WRITE_REVIEW, MY_REVIEW, READ_REVIEW
    }
}
