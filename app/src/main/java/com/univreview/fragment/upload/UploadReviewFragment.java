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
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.dialog.RecommendRvDialog;
import com.univreview.fragment.BaseWriteFragment;
import com.univreview.log.Logger;
import com.univreview.model.ActivityResultEvent;
import com.univreview.model.enumeration.ReviewSearchType;
import com.univreview.util.Util;
import com.univreview.view.contract.UploadReviewContract;
import com.univreview.view.presenter.UploadReviewPresenter;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class UploadReviewFragment extends BaseWriteFragment implements UploadReviewContract.View{
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
    private boolean isReviewExist = false;
    private RecommendRvDialog recommendRvDialog;

    private UploadReviewPresenter presenter;

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
        presenter = new UploadReviewPresenter();
        presenter.view = this;

        backBtn.setOnClickListener(v -> getActivity().onBackPressed());
        okBtn.setOnClickListener(v -> presenter.registerReview());
        subjectTxt.setOnClickListener(v -> Navigator.goSearch(getContext(), ReviewSearchType.SUBJECT, false));
        professorTxt.setOnClickListener(v -> {
            if (subjectTxt.getText().length() > 0) {
                if (!isReviewExist) { // review 를 처음 남기는 학생
                    Navigator.goSearch(getContext(), ReviewSearchType.PROF_FROM_SUBJ, presenter.getReview().subjectId, false);
                } else {
                    showAlertDialog();
                }
            } else {
                Util.simpleMessageDialog(getContext(), "과목을 입력해주세요");
            }
        });

        // rating (difficulty, assignment, attendance, grade, achievement)

        difficultyRate.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            presenter.getReview().difficultyRate = v;
            difficultyTxt.setText(presenter.getReview().getDifficultyRateMessage());
        });
        assignmentRate.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            presenter.getReview().assignmentRate = v;
            assignmentTxt.setText(presenter.getReview().getAssignmentRateMessage());
        });
        attendanceRate.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            presenter.getReview().attendanceRate = v;
            attendanceTxt.setText(presenter.getReview().getAttendanceRateMessage());
        });
        gradeRate.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            presenter.getReview().gradeRate = v;
            gradeTxt.setText(presenter.getReview().getGradeRateMessage());
        });
        achievementRate.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            presenter.getReview().achievementRate = v;
            achievementTxt.setText(presenter.getReview().getAchievementRateMessage());
        });
    }


    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        if (activityResultEvent.getResultCode() == getActivity().RESULT_OK) {
            if (activityResultEvent.getRequestCode() == Navigator.SEARCH) {
                Intent data = activityResultEvent.getIntent();
                long id = data.getLongExtra("id", 0);
                String name = data.getStringExtra("name");
                ReviewSearchType type = (ReviewSearchType)data.getSerializableExtra("type");
                Logger.v("on activity result: " + type);
                if (ReviewSearchType.SUBJECT.equals(type)) {
                    subjectTxt.setText(name);
                    presenter.getReview().subjectId = id;
                    presenter.getReview().subjectDetailId = 0;
                    presenter.getReview().professorId = 0;
                    professorTxt.setText(null);
                    presenter.checkReviewExist();
                } else if (ReviewSearchType.PROF_FROM_SUBJ.equals(type)) {
                    long detailId = data.getLongExtra("detailId", 0);
                    professorTxt.setText(name);
                    presenter.getReview().subjectDetailId = detailId;
                    presenter.getReview().professorId = id;
                }
            }
        }
    }


    @Override
    public void showRecommendRvDialog() {
        recommendRvDialog =new RecommendRvDialog(getContext(), presenter.getReview());
        recommendRvDialog.show();
    }

    @Override
    public void showAlertDialog(){
        new AlertDialog.Builder(getContext(), R.style.customDialog)
                .setMessage("이미 해당 과목 리뷰를 쓰셨습니다.\n다른 과목 리뷰를 써주시길 바랍니다.")
                .setPositiveButton("확인", null)
                .show();
    }

    @Override
    public void setReviewExist(boolean reviewExist) {
        isReviewExist = reviewExist;
    }

    @NotNull
    @Override
    public String getSubjectName() {
        return subjectTxt.getText().toString();
    }

    @NotNull
    @Override
    public String getProfessorName() {
        return professorTxt.getText().toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recommendRvDialog != null) recommendRvDialog.dismiss();
    }
}
