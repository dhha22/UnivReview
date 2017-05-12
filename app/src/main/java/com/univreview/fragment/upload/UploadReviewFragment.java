package com.univreview.fragment.upload;

import android.app.ProgressDialog;
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
import com.univreview.activity.BaseActivity;
import com.univreview.dialog.RecommendRvDialog;
import com.univreview.fragment.BaseFragment;
import com.univreview.fragment.BaseWriteFragment;
import com.univreview.fragment.MypageFragment;
import com.univreview.log.Logger;
import com.univreview.model.ActivityResultEvent;
import com.univreview.model.Review;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class UploadReviewFragment extends BaseWriteFragment {
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
    @BindView(R.id.attendance_txt) TextView attendanceTxt;
    @BindView(R.id.grade_txt) TextView gradeTxt;
    @BindView(R.id.achievement_txt) TextView achievementTxt;
    private ProgressDialog progressDialog;
    private RecommendRvDialog recommendRvDialog;
    private Review review;
    private boolean isReviewExist = false;

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
        progressDialog = Util.progressDialog(context);
        review = new Review();
        backBtn.setOnClickListener(v -> activity.onBackPressed());
        okBtn.setOnClickListener(v -> registerReview());
        subjectTxt.setOnClickListener(v -> Navigator.goSearch(context, "subject", subjectTxt.getText().toString(), false));
        professorTxt.setOnClickListener(v -> {
            if (subjectTxt.getText().length() > 0) {
                if (!isReviewExist) {
                    Navigator.goSimpleSearchResult(context, "searchProfessor", review.subjectId);
                } else {
                    showAlertDialog();
                }
            } else {
                Util.simpleMessageDialog(context, "과목을 입력해주세요");
            }
        });
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
            attendanceTxt.setText(review.getAttendanceRateMessage());
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
                    review.subjectDetailId = 0;
                    review.professorId = 0;
                    professorTxt.setText(null);
                    callGetReviewExist(id);
                } else if ("searchProfessor".equals(type)) {
                    long detailId = data.getLongExtra("detailId", 0);
                    professorTxt.setText(name);
                    review.subjectDetailId = detailId;
                    review.professorId = id;
                }
            }
        }
    }

    private void registerReview() {
        review.userId = App.userId;
        review.difficultyRate = difficultyRate.getRating();
        review.assignmentRate = assignmentRate.getRating();
        review.attendanceRate = attendanceRate.getRating();
        review.gradeRate = gradeRate.getRating();
        review.achievementRate = achievementRate.getRating();
        review.subjectDetail.subject.name = subjectTxt.getText().toString();
        review.subjectDetail.professor.name = professorTxt.getText().toString();
        if (review.getAlertMessage() == null) {
            callPostSimpleReviewApi(review);
            progressDialog.show();
        } else {
            Util.simpleMessageDialog(context, review.getAlertMessage());
        }
    }



    //api

    private void callGetReviewExist(long subjectId){
        Retro.instance.reviewService().getReviewExist(App.setAuthHeader(App.userToken), subjectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    isReviewExist = result.exist;
                    if(isReviewExist){
                        showAlertDialog();
                    }
                }, ErrorUtils::parseError);

    }

    private void showAlertDialog(){
        new AlertDialog.Builder(context, R.style.customDialog)
                .setMessage("이미 해당 과목 리뷰를 쓰셨습니다.\n다른 과목 리뷰를 써주시길 바랍니다.")
                .setPositiveButton("확인", null)
                .show();
    }

    private void callPostSimpleReviewApi(Review review){
        Logger.v("post review: " + review);
        Retro.instance.reviewService().postSimpleReview(App.setAuthHeader(App.userToken), review)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> progressDialog.dismiss())
                .subscribe(result -> response(result.review), ErrorUtils::parseError);
    }

    private void response(Review review) {
        Logger.v("response review: " + review);
        MypageFragment.isRefresh = true;
        this.review.id = review.id;
        recommendRvDialog =new RecommendRvDialog(context, this.review);
        recommendRvDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recommendRvDialog != null) recommendRvDialog.dismiss();
    }
}
