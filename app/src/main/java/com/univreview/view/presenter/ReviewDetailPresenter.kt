package com.univreview.view.presenter

import android.content.DialogInterface
import com.univreview.App
import com.univreview.adapter.contract.ReviewDetailAdapterContract
import com.univreview.listener.OnItemLongClickListener
import com.univreview.log.Logger
import com.univreview.model.ReviewComment
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.contract.ReviewDetailContract
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 6..
 */
class ReviewDetailPresenter : ReviewDetailContract, OnItemLongClickListener {
    companion object {
        val DEFAULT_PAGE = 1
    }

    lateinit var view: ReviewDetailContract.View
    lateinit var adapterModel: ReviewDetailAdapterContract.Model
    lateinit var adapterView: ReviewDetailAdapterContract.View

    override fun loadReviewSingle() {
        // review single api 호출
        Retro.instance.reviewService().getReview(App.setAuthHeader(App.userToken), view.getReviewId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    val review = result.review.apply {
                        subjectDetail.subject.name = subjectName
                        subjectDetail.professor.name = professorName
                        user.name = userName
                        user.authenticated = authenticated
                    }
                    view.setData(review)    // review 동기화
                }, { ErrorUtils.parseError(it) })
    }

    override fun loadCommentItem(page: Int) {
        // 가장 최신에 달린 댓글이 list 하단에 존재
        Retro.instance.reviewService().getReviewComment(App.setAuthHeader(App.userToken), view.getReviewId(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (page == DEFAULT_PAGE) {
                        adapterModel.clearItem()
                    }
                    view.setCommentMoreBtn(result.comments.size == 5)
                    view.setPage(page + 1)

                    Logger.v("comment result: " + result.toString())
                    Observable.from(result.comments)
                            .subscribe({ data -> adapterModel.addItem(data) }, { Logger.e(it) })
                }) { error ->
                    view.setPage(DEFAULT_PAGE)
                    ErrorUtils.parseError(error)
                }
    }

    override fun postComment(body: ReviewComment) {
        Retro.instance.reviewService().postReviewComment(App.setAuthHeader(App.userToken), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.dismissProgress() }
                .subscribe({ result -> adapterModel.addLastItem(result) }, { ErrorUtils.parseError(it) })

    }

    // comment delete
    override fun onLongClick(view: android.view.View, position: Int): Boolean {
        Logger.v("delete comment item: " + adapterModel.getItem(position - 1).name)
        this.view.showCommentDeleteDialog(DialogInterface.OnClickListener { _, _ ->
            Retro.instance.reviewService().deleteComment(App.setAuthHeader(App.userToken), adapterModel.getItem(position - 1).id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ adapterModel.removeItem(position) }, { ErrorUtils.parseError(it) })
        })
        return true
    }
}