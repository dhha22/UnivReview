package com.univreview.view.presenter

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.univreview.App
import com.univreview.Navigator
import com.univreview.adapter.contract.ReviewDetailAdapterContract
import com.univreview.dialog.ListDialog
import com.univreview.listener.OnItemClickListener
import com.univreview.listener.OnItemLongClickListener
import com.univreview.log.Logger
import com.univreview.model.model_kotlin.Review
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

    var page = DEFAULT_PAGE
    lateinit var context: Context
    lateinit var review: Review
    lateinit var view: ReviewDetailContract.View
    lateinit var adapterModel: ReviewDetailAdapterContract.Model
    lateinit var adapterView: ReviewDetailAdapterContract.View

    override fun loadReviewSingle() {
        // review single api 호출
        Retro.instance.reviewService().callReviewSingle(App.setHeader(), review.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    review = it.data
                    view.setHeaderData(it.data)
                }, { ErrorUtils.parseError(it) })
    }

    override fun loadCommentItem() {
        // 가장 최신에 달린 댓글이 list 하단에 존재
        Retro.instance.reviewService().callReviewComments(App.setHeader(), review.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun postComment(body: ReviewComment) {
        // 리뷰 댓글 쓰기
        Retro.instance.reviewService().postReviewComment(App.setHeader(), review.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

        //.subscribe({ result -> adapterModel.addLastItem(result) }, { ErrorUtils.parseError(it) })

    }

    // comment delete
    override fun onLongClick(view: android.view.View, position: Int): Boolean {
        Logger.v("delete comment item: " + adapterModel.getItem(position - 1).name)
        // 본인 댓글일 경우
        // if()
        this.view.showCommentDeleteDialog(DialogInterface.OnClickListener { _, _ ->
            Retro.instance.reviewService().deleteReviewComment(App.setHeader(), adapterModel.getItem(position - 1).id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ adapterModel.removeItem(position) }, { ErrorUtils.parseError(it) })
        })
        return true
    }


    override val etcBtnClickListener = View.OnClickListener { _ ->
        Logger.v("more btn click")
        //  본인이 더보기 버튼을 클릭했을 경우
        if (review.user?.uid == App.userId) {
            view.setDialog(arrayListOf("수정하기"),
                    OnItemClickListener { _, _ -> Navigator.goReviewDetail(context, review) })
        } else {
            view.setDialog(arrayListOf("신고하기"),
                    OnItemClickListener { _, _ -> Navigator.goReviewReport(context, review.id) })
        }
    }
}