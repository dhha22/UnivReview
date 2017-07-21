package com.univreview.view.presenter;

import android.content.Context;

import com.univreview.App;
import com.univreview.fragment.MypageFragment;
import com.univreview.log.Logger;
import com.univreview.model.Review;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.Util;
import com.univreview.view.contract.UploadReviewContract;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 7. 20..
 */

public class UploadReviewPresenter implements UploadReviewContract {
    private UploadReviewContract.View view;
    private Review review;
    private Context context;


    @Override
    public void registerReview() {
        review.userId = App.userId;
        review.subjectDetail.subject.name = view.getSubjectName();
        review.subjectDetail.professor.name = view.getProfessorName();
        if (review.getAlertMessage() == null) {
            callPostSimpleReviewApi(review);
            view.showProgress();
        } else {
            Util.simpleMessageDialog(context, review.getAlertMessage());
        }
    }

    private void callPostSimpleReviewApi(Review review){
        Logger.v("post review: " + review);
        Logger.v("prof id: " + review.professorId);
        Logger.v("subj id: " + review.subjectId);
        Retro.instance.reviewService().postSimpleReview(App.setAuthHeader(App.userToken), review)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> view.dismissProgress())
                .subscribe(result -> response(result.review), ErrorUtils::parseError);
    }

    @Override
    public void checkReviewExist() {
        Retro.instance.reviewService().getReviewExist(App.setAuthHeader(App.userToken), review.subjectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    view.setReviewExist(result.exist);
                    if (result.exist) {
                        view.showAlertDialog();
                    }
                }, ErrorUtils::parseError);
    }

    private void response(Review review) {
        Logger.v("response review: " + review);
        MypageFragment.isRefresh = true;
        this.review.id = review.id;
        view.showRecommendRvDialog();
    }

    @Override
    public Review getReview() {
        return review;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(View view) {
        this.view = view;
        this.review = new Review();
    }

    @Override
    public void detachView() {
        view = null;
        review = null;
    }
}
