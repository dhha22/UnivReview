package com.univreview.view.presenter

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.univreview.App
import com.univreview.Navigator
import com.univreview.adapter.contract.ReviewDetailAdapterContract
import com.univreview.listener.OnItemClickListener
import com.univreview.listener.OnItemLongClickListener
import com.univreview.log.Logger
import com.univreview.model.model_kotlin.DataListModel
import com.univreview.model.model_kotlin.Review
import com.univreview.model.model_kotlin.RvComment
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
    var adapterView: ReviewDetailAdapterContract.View? = null
        set(value) {
            field = value
            value?.setOnItemLongClickListener(this)
        }

    // review single api 호출
    override fun loadReviewSingle() {
        Retro.instance.reviewService().callReviewSingle(App.setHeader(), review.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.data?.let {
                        review.content = it.content
                        review.likeCount = it.likeCount
                        review.commentCount = it.commentCount
                        review.notifyUpdate()
                    }
                }, { onErrorReview(it) })
    }

    fun onErrorReview(throwable: Throwable) {
        Logger.e("error")
        if (ErrorUtils.ERROR_401 == ErrorUtils.parseError(throwable)) {
            view.showTicketDialog()
        }
    }


    // 가장 최신에 달린 댓글이 list 하단에 존재
    override fun loadComments() {
        Retro.instance.reviewService().callReviewComments(App.setHeader(), review.id, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onSuccessComment(it) }, { ErrorUtils.parseError(it) })
    }

    // 리뷰 댓글 추가
    fun onSuccessComment(result: DataListModel<RvComment>) {
        view.hasMoreComment(result.pagination?.nextPage != 0)
        if (result.data.isNotEmpty()) {
            Observable.from(result.data).subscribe { adapterModel.addLastItem(it) }
        }
    }

    // 리뷰 댓글 쓰기
    override fun postComment(body: RvComment) {
        Retro.instance.reviewService().postReviewComment(App.setHeader(), review.id, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.dismissProgress() }
                .subscribe({
                    adapterModel.addLastItem(it.data)
                    view.increaseCommentCnt(true)
                }, { ErrorUtils.parseError(it) })
    }


    // 리뷰 댓글 삭제
    override fun onLongClick(view: View, position: Int): Boolean {
        val comment = adapterModel.getItem(position - 1) as RvComment
        Logger.v("delete comment item: $comment")
        // 본인 댓글일 경우
        if (App.userId == (comment.userId)) {
            this.view.showCommentDeleteDialog(DialogInterface.OnClickListener { _, _ ->
                Retro.instance.reviewService().deleteReviewComment(App.setHeader(), comment.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            adapterModel.removeItem(position)
                            this.view.increaseCommentCnt(false)
                        }, { ErrorUtils.parseError(it) })
            })
        }
        return true
    }

    override val etcBtnClickListener = View.OnClickListener { _ ->
        Logger.v("more btn click")
        Logger.v("review user id: " + review.user?.id)
        //  본인이 더보기 버튼을 클릭했을 경우
        if (review.user?.id == App.userId) {
            view.setDialog(arrayListOf("수정하기"),
                    OnItemClickListener { _, _ -> Navigator.goUploadReviewDetail(context, review) })
        } else {
            view.setDialog(arrayListOf("신고하기"),
                    OnItemClickListener { _, _ -> Navigator.goReviewReport(context, review.id) })
        }
    }
}