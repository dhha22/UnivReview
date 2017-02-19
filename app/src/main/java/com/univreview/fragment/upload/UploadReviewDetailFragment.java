package com.univreview.fragment.upload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.Review;
import com.univreview.model.ReviewDetail;
import com.univreview.network.Retro;
import com.univreview.view.ReviewItemView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class UploadReviewDetailFragment extends BaseFragment {
    @BindView(R.id.review_item) ReviewItemView reviewItemView;
    @BindView(R.id.input_review) EditText inputReview;
    private Review review;

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
        toolbar.setToolbarBackgroundColor(R.color.colorPrimary);
        toolbar.setOnConfirmListener(v -> registerReview(review.id));
        rootLayout.addView(view);
        return rootLayout;
    }

    private void setReviewData(Review review){
        reviewItemView.setData(review);
    }

    private void registerReview(long reviewId){
        Logger.v("review Detail");
        ReviewDetail reviewDetail = new ReviewDetail();
        reviewDetail.reviewId = reviewId;
        reviewDetail.reviewDetail = inputReview.getText().toString();
        if(reviewDetail.checkReviewDetail()){
            callPostReviewDetail(reviewDetail);
        }else{
            //error msg
        }
    }

    private void callPostReviewDetail(ReviewDetail reviewDetail){
        Retro.instance.reviewService().postDetailReview(reviewDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response(),error -> errorResponse());
    }

    private void response(){

    }

    private void errorResponse(){

    }


}
