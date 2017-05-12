package com.univreview.fragment.review;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.activity.BaseActivity;
import com.univreview.fragment.BaseFragment;
import com.univreview.listener.OnBackPressedListener;
import com.univreview.log.Logger;
import com.univreview.model.Review;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.TimeUtil;
import com.univreview.util.Util;
import com.univreview.view.ReviewRatingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 24..
 */
public class ReviewDetailFragment extends BaseFragment {
    private static final int POSITION_NONE = -1;
    @BindView(R.id.subject_professor) TextView subjectProfessorTxt;
    @BindView(R.id.name_txt) TextView nameTxt;
    @BindView(R.id.auth_mark) TextView authMark;
    @BindView(R.id.time_txt) TextView timeTxt;
    @BindView(R.id.more_btn) ImageButton moreBtn;
    @BindView(R.id.review_rating_indicator_view) ReviewRatingIndicatorView reviewRatingIndicatorView;
    @BindView(R.id.review_detail_layout) LinearLayout reviewDetailLayout;
    @BindView(R.id.review_detail_txt) TextView reviewDetailTxt;
    @BindView(R.id.dim_view) View dimView;
    @BindView(R.id.bottom_sheet) LinearLayout bottomSheet;
    @BindView(R.id.update) TextView update;
    @BindView(R.id.report) TextView report;
    private BottomSheetBehavior behavior;
    private Review data;
    public static boolean isRefresh = false;

    public static ReviewDetailFragment newInstance(Review data){
        ReviewDetailFragment fragment = new ReviewDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("review", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (Review)getArguments().getSerializable("review");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.v("review detail fragment refresh");
        if(isRefresh){
            callReviewSingleApi(data.id);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((BaseActivity)activity).setOnBackPressedListener(backPressedListener);
        View view = inflater.inflate(R.layout.fragment_review_detail, container, false);
        ButterKnife.bind(this, view);
        toolbar.setBackBtnVisibility(true);
        rootLayout.setBackgroundColor(Util.getColor(context, R.color.backgroundColor));
        rootLayout.addView(view);
        init();
        return rootLayout;
    }

    private void init() {
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dimView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        if(data != null && data.user.name != null) {
            setData(data);
        }else{
            callReviewSingleApi(data.id);
        }

        dimView.setOnClickListener(moreBtnClickListener);
        moreBtn.setOnClickListener(moreBtnClickListener);
    }

    private void setData(Review data) {
        nameTxt.setText(data.user.name);
        if (data.user.authenticated != null) {
            if (data.user.authenticated) {
                authMark.setVisibility(View.VISIBLE);
            } else {
                authMark.setVisibility(View.GONE);
            }
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        int index = 0;
        if (data.subjectDetail.subject != null) {
            builder.append(data.subjectDetail.subject.getName() + " ");
        }
        Util.addSizeSpan(builder, index, Util.dpToPx(context, 16));
        Util.addColorSpan(context, builder, index, R.color.colorPrimary);
        index = builder.length();

        if (data.subjectDetail.professor != null) {
            builder.append(data.subjectDetail.professor.getName() + " 교수님");
        }
        Util.addSizeSpan(builder, index, Util.dpToPx(context, 14));
        Util.addColorSpan(context, builder, index, R.color.professorTxtColor);

        subjectProfessorTxt.setText(builder);

        if (data.reviewDetail != null) {
            reviewDetailTxt.setText(data.reviewDetail.reviewDetail);
            reviewDetailLayout.setVisibility(View.VISIBLE);
        }

        timeTxt.setText(new TimeUtil().getPointFormat(data.createdDate));
        reviewRatingIndicatorView.setData(data);


        if (App.userId == data.userId) {
            update.setOnClickListener(v -> {
                hiddenBottomSheet();
                Navigator.goUploadReviewDetail(context, data, POSITION_NONE);
            });
            report.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);
            if (data.reviewDetail != null) {
                update.setText("리뷰수정");
            } else {
                update.setText("상세리뷰 쓰기");
            }
        } else {
            report.setVisibility(View.VISIBLE);
            update.setVisibility(View.GONE);
            report.setOnClickListener(v -> {
                hiddenBottomSheet();
                Navigator.goReviewReport(context, data.id);
            });
        }
        isRefresh = false;
    }

    private View.OnClickListener moreBtnClickListener = view -> {
        if(behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            expandBottomSheet();
        }else if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
           hiddenBottomSheet();
        }
    };

    private OnBackPressedListener backPressedListener = () -> {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            hiddenBottomSheet();
        } else {
            ((BaseActivity) activity).setOnBackPressedListener(null);
            activity.onBackPressed();
        }
    };


    private void expandBottomSheet() {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        dimView.setVisibility(View.VISIBLE);
    }

    private void hiddenBottomSheet() {
        dimView.setVisibility(View.GONE);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }


    private void callReviewSingleApi(long reviewId){
        Logger.v("review id: " + reviewId);
        Retro.instance.reviewService().getReview(App.setAuthHeader(App.userToken), reviewId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Review review = result.review;
                    review.subjectDetail.subject.name = review.subjectName;
                    review.subjectDetail.professor.name = review.professorName;
                    review.user.name = review.userName;
                    review.user.authenticated = review.authenticated;
                    setData(review);
                }, ErrorUtils::parseError);
    }

}
