package com.univreview.view;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.fragment.review.ReviewListFragment;
import com.univreview.listener.OnItemClickListener;
import com.univreview.log.Logger;
import com.univreview.model.Review;
import com.univreview.model.ReviewLike;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 23..
 */
public class ReviewItemView extends FrameLayout {
    @BindView(R.id.layout) LinearLayout layout;
    @BindView(R.id.user_layout) LinearLayout userLayout;
    @BindView(R.id.name_txt) TextView nameTxt;
    @BindView(R.id.auth_mark) TextView authMarkTxt;
    @BindView(R.id.subject_txt) TextView subjectTxt;
    @BindView(R.id.professor_txt) TextView professorTxt;
    @BindView(R.id.subject_professor) TextView subjectProfessorTxt;
    @BindView(R.id.more_btn) ImageButton moreBtn;
    @BindView(R.id.review_txt) TextView reviewTxt;
    @BindView(R.id.like_img) ImageView likeImg;
    @BindView(R.id.difficulty_txt) TextView difficultyTxt;
    @BindView(R.id.assignment_txt) TextView assignmentTxt;
    @BindView(R.id.attendance_txt) TextView attendanceTxt;
    @BindView(R.id.grade_txt) TextView gradeTxt;
    @BindView(R.id.achievement_txt) TextView achievementTxt;
    @BindView(R.id.difficulty_rate) AppCompatRatingBar difficultyRate;
    @BindView(R.id.assignment_rate) AppCompatRatingBar assignmentRate;
    @BindView(R.id.attendance_rate) AppCompatRatingBar attendanceRate;
    @BindView(R.id.grade_rate) AppCompatRatingBar gradeRate;
    @BindView(R.id.achievement_rate) AppCompatRatingBar achievementRate;
    @BindView(R.id.bottom_line) View bottomLine;
    @BindView(R.id.bottom_layout) LinearLayout bottomLayout;
    @BindView(R.id.like_layout) LinearLayout likeLayout;
    @BindView(R.id.like_cnt) TextView likeCntTxt;
    @BindView(R.id.comment_cnt) TextView commentCntTxt;
    @BindView(R.id.comment_layout) LinearLayout commentLayout;
    private Review data;
    private Context context;
    private int position;
    private Status status;

    public ReviewItemView(Context context) {
        this(context, null);
    }

    public ReviewItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReviewItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.review_item, this, true);
        this.context = context;
        ButterKnife.bind(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, Util.dpToPx(context, 8));
        setLayoutParams(params);
    }

    public void setData(Review review) {
        if (review != null) {
            this.data = review;
            Logger.v("review: " + review);
            setVisibility(VISIBLE);
            if (review.user != null) {
                nameTxt.setText(review.user.name);
                if (review.user.authenticated != null) {
                    if (review.user.authenticated) {
                        authMarkTxt.setVisibility(VISIBLE);
                    } else {
                        authMarkTxt.setVisibility(GONE);
                    }
                }else{
                    authMarkTxt.setVisibility(GONE);
                }
            }

            likeImg.setSelected(review.likes);
            likeCntTxt.setText(review.likeCount+"명");
            commentCntTxt.setText(review.commentCount+"명");
            likeLayout.setOnClickListener(v -> {
                callReviewLike(review.rid);
                if (likeImg.isSelected()) {
                    likeImg.setSelected(false);
                } else {
                    likeImg.setSelected(true);
                }
            });

            if (review.reviewDetail != null) {
                reviewTxt.setVisibility(VISIBLE);
                reviewTxt.setText(review.reviewDetail.reviewDetail);
            }else{
                reviewTxt.setVisibility(GONE);
            }


            SpannableStringBuilder builder = new SpannableStringBuilder();
            int index = 0;
            if (review.subjectDetail.subject != null) {
                builder.append(review.subjectDetail.subject.getName() + " ");
            }
            Util.addColorSpan(context, builder, index, R.color.colorPrimary);
            index = builder.length();

            if (review.subjectDetail.professor != null) {
                builder.append(review.subjectDetail.professor.getName() + " 교수님");
            }
            Util.addSizeSpan(builder, index, App.dp12);
            Util.addColorSpan(context, builder, index, R.color.professorTxtColor);

            subjectProfessorTxt.setText(builder);

            if (review.subjectDetail.subject != null) {
                subjectTxt.setText(review.subjectDetail.subject.getName() + "");
            }
            if (review.subjectDetail.professor != null) {
                professorTxt.setText(review.subjectDetail.professor.getName() + " 교수님");

            }

            difficultyTxt.setText(review.getDifficultyRateMessage());
            assignmentTxt.setText(review.getAssignmentRateMessage());
            attendanceTxt.setText(review.getAttendanceRateMessage());
            gradeTxt.setText(review.getGradeRateMessage());
            achievementTxt.setText(review.getAchievementRateMessage());
            difficultyRate.setRating(review.difficultyRate);
            assignmentRate.setRating(review.assignmentRate);
            attendanceRate.setRating(review.attendanceRate);
            gradeRate.setRating(review.gradeRate);
            achievementRate.setRating(review.achievementRate);
            /*setOnClickListener(v -> {
                if (Status.MY_REVIEW.equals(status) || Status.READ_REVIEW.equals(status)) {
                    *//*ReviewListFragment.reviewSingleId = review.id;
                    ReviewListFragment.reviewItemRefreshPosition = position;*//*
                    Navigator.goReviewDetail(context, review, layout);
                }
            });*/
            likeLayout.setOnClickListener(v -> callReviewLike(review.rid));
        } else {
            setVisibility(INVISIBLE);
        }
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void setMoreBtnClickListener(OnItemClickListener itemClickListener){
        moreBtn.setOnClickListener(view -> itemClickListener.onItemClick(view, position));
    }

    public void setMode(Status status) {
        this.status = status;
        switch (status){
            case WRITE_REVIEW:
                subjectTxt.setVisibility(GONE);
                professorTxt.setVisibility(GONE);
                subjectProfessorTxt.setVisibility(VISIBLE);
                userLayout.setVisibility(GONE);
                reviewTxt.setVisibility(GONE);
                moreBtn.setVisibility(GONE);
                bottomLine.setVisibility(GONE);
                bottomLayout.setVisibility(GONE);
                break;
            case MY_REVIEW:
                subjectTxt.setVisibility(VISIBLE);
                professorTxt.setVisibility(VISIBLE);
                subjectProfessorTxt.setVisibility(GONE);
                userLayout.setVisibility(GONE);
                reviewTxt.setVisibility(VISIBLE);
                moreBtn.setVisibility(VISIBLE);
                bottomLine.setVisibility(VISIBLE);
                bottomLayout.setVisibility(VISIBLE);
                break;
            case READ_REVIEW:
                subjectTxt.setVisibility(VISIBLE);
                professorTxt.setVisibility(VISIBLE);
                subjectProfessorTxt.setVisibility(GONE);
                userLayout.setVisibility(VISIBLE);
                reviewTxt.setVisibility(VISIBLE);
                moreBtn.setVisibility(VISIBLE);
                bottomLine.setVisibility(VISIBLE);
                bottomLayout.setVisibility(VISIBLE);
                break;
        }
    }

    public enum Status {
        WRITE_REVIEW, MY_REVIEW, READ_REVIEW
    }

    // api
    private void callReviewLike(long id){
        Retro.instance.reviewService().likeReview(App.setAuthHeader(App.userToken), new ReviewLike(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (data.likes) {
                        data.likes = false;
                        data.likeCount--;
                    } else {
                        data.likes = true;
                        data.likeCount++;
                    }
                    setData(data);
                }, ErrorUtils::parseError);
    }
}
