package com.univreview.fragment.upload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.Review;
import com.univreview.network.Retro;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class UploadReviewFragment extends BaseFragment {
    @BindView(R.id.back_btn) ImageButton backBtn;
    @BindView(R.id.ok_btn) TextView okBtn;
    @BindView(R.id.difficulty_rate) AppCompatRatingBar difficultyRate;
    @BindView(R.id.assignment_rate) AppCompatRatingBar assignmentRate;
    @BindView(R.id.attendance_rate) AppCompatRatingBar attendanceRate;
    @BindView(R.id.grade_rate) AppCompatRatingBar gradeRate;
    @BindView(R.id.achievement_rate) AppCompatRatingBar achievementRate;
    @BindView(R.id.subject_txt) TextView subjectTxt;
    @BindView(R.id.professor_txt) TextView professorTxt;


    public static UploadReviewFragment newInstance(){
        UploadReviewFragment fragment = new UploadReviewFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_upload_review, container, false);
        ButterKnife.bind(this, view);
        toolbar.setVisibility(View.GONE);
        init();
        rootLayout.addView(view);
        return rootLayout;
    }
    private void init(){
        backBtn.setOnClickListener(v -> activity.onBackPressed());
        okBtn.setOnClickListener(v ->registerReview());
        subjectTxt.setOnClickListener(v -> Navigator.goSearch(context, "subject", App.UNIVERSITY_ID));
    }

    private void registerReview(){
        Review review = new Review();
        review.difficultyRate = difficultyRate.getRating();
        review.assignmentRate = assignmentRate.getRating();
        review.attendanceRate = attendanceRate.getRating();
        review.gradeRate = gradeRate.getRating();
        review.achievementRate = achievementRate.getRating();
        if(review.checkReviewRating()){
            callRegisterReviewApi(review);
        }else{
            //error msg
        }
    }

    private void callRegisterReviewApi(Review review){
        Retro.instance.reviewService().postSimpleReview(review)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response(result.reviews), error -> Logger.v(error));
    }

    private void response(List<Review> review){

    }


}
