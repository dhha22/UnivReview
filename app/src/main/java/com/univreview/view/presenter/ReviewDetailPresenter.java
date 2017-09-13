/*
package com.univreview.view.presenter;

import android.util.Log;

import com.univreview.App;
import com.univreview.adapter.contract.ReviewDetailAdapterContract;
import com.univreview.listener.OnItemLongClickListener;
import com.univreview.log.Logger;
import com.univreview.model.Review;
import com.univreview.model.ReviewComment;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.view.contract.ReviewDetailContract;

import org.w3c.dom.Comment;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

*/
/**
 * Created by DavidHa on 2017. 7. 13..
 *//*


public class ReviewDetailPresenter implements ReviewDetailContract, OnItemLongClickListener {
    private static final int DEFAULT_PAGE = 1;
    private ReviewDetailContract.View view;
    private ReviewDetailAdapterContract.Model adapterModel;
    private ReviewDetailAdapterContract.View adapterView;

    @Override
    public void loadReviewSingle() {
        Retro.instance.reviewService().getReview(App.setAuthHeader(App.userToken), view.getReviewId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Review review = result.review;
                    review.subjectDetail.subject.name = review.subjectName;
                    review.subjectDetail.professor.name = review.professorName;
                    review.user.name = review.userName;
                    review.user.authenticated = review.authenticated;
                    view.setData(review);
                }, ErrorUtils::parseError);
    }

    @Override
    public void loadComments(int page) {
        // 가장 최신에 달린 댓글이 list 하단에 존재
        Retro.instance.reviewService().getReviewComment(App.setAuthHeader(App.userToken), view.getReviewId(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (page == DEFAULT_PAGE) {
                        adapterModel.clearItem();
                    }
                    view.hasMoreComment(result.comments.size() == 5);
                    view.setPage(page + 1);

                    Logger.v("comment result: " + result.toString());
                    Observable.from(result.comments)
                            .subscribe(data -> adapterModel.addItem(data), Logger::e);
                }, error -> {
                    view.setPage(DEFAULT_PAGE);
                    ErrorUtils.parseError(error);
                });
    }

    @Override
    public void postComment(ReviewComment body) {
        Retro.instance.reviewService().postReviewComment(App.setAuthHeader(App.userToken), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> view.dismissProgress())
                .subscribe(result -> adapterModel.addLastItem(result), ErrorUtils::parseError);
    }

    public void setAdapterModel(ReviewDetailAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    public void setAdapterView(ReviewDetailAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        adapterView.setOnItemLongClickListener(this);
    }

    // comment delete
    @Override
    public boolean onLongClick(android.view.View view, int position) {
        Logger.v("delete comment item: " + adapterModel.getItem(position - 1).getName());
        this.view.showCommentDeleteDialog((dialog, which) ->
                Retro.instance.reviewService().deleteComment(App.setAuthHeader(App.userToken), adapterModel.getItem(position - 1).getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> adapterModel.removeItem(position), ErrorUtils::parseError));
        return true;
    }
}
*/
