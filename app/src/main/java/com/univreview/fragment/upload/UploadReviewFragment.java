package com.univreview.fragment.upload;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.ActivityResultEvent;
import com.univreview.model.Review;
import com.univreview.network.Retro;
import com.univreview.util.Util;

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
    @BindView(R.id.subject_txt) TextView subjectTxt;
    @BindView(R.id.professor_txt) TextView professorTxt;
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
    private Review review;

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

    private void init() {
        review = new Review();
        backBtn.setOnClickListener(v -> activity.onBackPressed());
        okBtn.setOnClickListener(v -> registerReview());
        subjectTxt.setOnClickListener(v -> Navigator.goSearch(context, "subject", subjectTxt.getText().toString(), false));
        professorTxt.setOnClickListener(v -> Navigator.goSearch(context, "professor", professorTxt.getText().toString(), false));
        difficultyRate.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            review.difficultyRate = v;
            difficultyTxt.setText(review.getDifficultyRateMessage());
        });
        assignmentRate.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            review.assignmentRate = v;
            assignmentTxt.setText(review.getAssignmentRateMessage());
        });
        attendanceRate.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            review.attendanceRate = v;
            attendaceTxt.setText(review.getAttendanceRateMessage());
        });
        gradeRate.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            review.gradeRate = v;
            gradeTxt.setText(review.getGradeRateMessage());
        });
        achievementRate.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            review.achievementRate = v;
            achievementTxt.setText(review.getAchievementRateMessage());
        });
    }



    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        if (activityResultEvent.getResultCode() == getActivity().RESULT_OK) {
            if (activityResultEvent.getRequestCode() == Navigator.SEARCH) {
                Intent data = activityResultEvent.getIntent();
                long id = data.getLongExtra("id", 0);
                String name = data.getStringExtra("name");
                String type = data.getStringExtra("type");
                Logger.v("on activity result: " + type);
                if ("subject".equals(type)) {
                    subjectTxt.setText(name);
                    review.subjectId = id;
                } else if ("professor".equals(type)) {
                    professorTxt.setText(name);
                    review.professorId = id;
                }

            }
        }
    }

    private void registerReview(){
        review.difficultyRate = difficultyRate.getRating();
        review.assignmentRate = assignmentRate.getRating();
        review.attendanceRate = attendanceRate.getRating();
        review.gradeRate = gradeRate.getRating();
        review.achievementRate = achievementRate.getRating();

        //test
        review.professor.name = professorTxt.getText().toString();
        review.subject.name = subjectTxt.getText().toString();
        if (review.getAlertMessage() == null) {
            Navigator.goUploadReviewDetail(context, review);
           // callPostSimpleReviewApi(review);
        } else {
            Util.simpleMessageDialog(context, review.getAlertMessage());
        }
    }

    //api

    private void callPostSimpleReviewApi(Review review){
        Logger.v("post review: " + review);
        Retro.instance.reviewService().postSimpleReview(review)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response(result.review), error -> Logger.e(error));
    }

    private void response(Review review) {
        Logger.v("response review: " + review);
        new AlertDialog.Builder(context)
                .setMessage("좀 더 자세한 리뷰를 쓰면\n" +
                        "많은 학우들에게 도움이 됩니다.\n" +
                        "리뷰를 써볼까요?")
                .setPositiveButton("리뷰쓰기", (dialog, which) -> {
                    Navigator.goUploadReviewDetail(context, review);
                    activity.finish();
                })
                .setNegativeButton("다음에", (dialog, which) -> activity.onBackPressed())
                .setCancelable(false)
                .show();
    }


}
