package com.univreview.fragment.upload;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.univreview.App;
import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.fragment.BaseWriteFragment;
import com.univreview.log.Logger;
import com.univreview.model.Review;
import com.univreview.model.ReviewDetail;
import com.univreview.network.Retro;
import com.univreview.util.Util;
import com.univreview.view.ReviewItemView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class UploadReviewDetailFragment extends BaseWriteFragment {
    @BindView(R.id.review_item) ReviewItemView reviewItemView;
    @BindView(R.id.input_review) EditText inputReview;
    private boolean isUpdate = false;
    private Review review;
    private ReviewDetail reviewDetail;
    private ProgressDialog progressDialog;

    public static UploadReviewDetailFragment newInstance(Review review){
        UploadReviewDetailFragment fragment = new UploadReviewDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("review", review);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        review = (Review) getArguments().getSerializable("review");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_upload_review_detail, container, false);
        ButterKnife.bind(this, view);
        setReviewData(review);
        progressDialog = Util.progressDialog(context);
        toolbar.setToolbarBackgroundColor(R.color.colorPrimary);
        toolbar.setBackBtnVisibility(true);
        toolbar.setOnConfirmListener(v -> registerReview(review.id));
        rootLayout.addView(view);
        if (review.reviewDetail != null) {
            isUpdate = true;
            inputReview.setText(review.reviewDetail.reviewDetail);
            inputReview.setSelection(inputReview.getText().toString().length());
        }
        return rootLayout;
    }

    private void setReviewData(Review review) {
        reviewItemView.setData(review);
        reviewItemView.setMode(ReviewItemView.Status.WRITE_REVIEW);
    }

    private void registerReview(long reviewId) {
        reviewDetail = new ReviewDetail();
        reviewDetail.reviewId = reviewId;
        reviewDetail.reviewDetail = inputReview.getText().toString();
        if (reviewDetail.getAlertMessage() == null) {
            Logger.v("review detail: " + reviewDetail);
            progressDialog.show();
            if(isUpdate){
                callPutReviewDetail(review.reviewDetail.id, reviewDetail);
            }else {
                callPostReviewDetail(reviewDetail);
            }
        } else {
            Util.simpleMessageDialog(context, reviewDetail.getAlertMessage());
        }
    }

    private void callPostReviewDetail(ReviewDetail reviewDetail) {
        Retro.instance.reviewService().postDetailReview(App.setAuthHeader(App.userToken), reviewDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> progressDialog.dismiss())
                .subscribe(result -> activity.onBackPressed(), error -> errorResponse(error));
    }

    private void callPutReviewDetail(long reviewDetailId, ReviewDetail reviewDetail){
        Logger.v("review detail id: " + reviewDetailId);
        Retro.instance.reviewService().putReviewDetail(App.setAuthHeader(App.userToken), reviewDetailId, reviewDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> progressDialog.dismiss())
                .subscribe(result -> activity.onBackPressed(), error -> errorResponse(error));
    }


    private void errorResponse(Throwable e){
        Logger.e(e);
    }


}
