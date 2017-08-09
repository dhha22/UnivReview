package com.univreview.view.presenter

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.univreview.App
import com.univreview.Navigator
import com.univreview.adapter.contract.ReviewDetailAdapterContract
import com.univreview.dialog.ListDialog
import com.univreview.listener.OnItemLongClickListener
import com.univreview.log.Logger
import com.univreview.model.Review
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
        val POSITION_NONE = -1
    }


    var page = DEFAULT_PAGE
    lateinit var context:Context
    lateinit var review : Review
    lateinit var view: ReviewDetailContract.View
    lateinit var adapterModel: ReviewDetailAdapterContract.Model
    lateinit var adapterView: ReviewDetailAdapterContract.View
    val dialog: ListDialog by lazy {
        val dialogList = ArrayList<String>()
        if (review.reviewDetail != null) {
            dialogList.add("리뷰수정")
        } else {
            dialogList.add("상세리뷰 쓰기")
        }

        if (App.userId as Long != review.userId) {
            dialogList.add(0, "리뷰신고")
        }
        ListDialog(context, dialogList, dialogItemClickListener)
    }

    override fun loadReviewSingle() {
        // review single api 호출
        Retro.instance.reviewService().getReview(App.setAuthHeader(App.userToken), review.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    val review = result.review.apply {
                        subjectDetail.subject.name = subjectName
                        subjectDetail.professor.name = professorName
                        user.name = userName
                        user.authenticated = authenticated
                    }
                    view.setHeaderData(review)    // review 동기화
                }, { ErrorUtils.parseError(it) })
    }

    override fun loadCommentItem() {
        // 가장 최신에 달린 댓글이 list 하단에 존재
        Retro.instance.reviewService().getReviewComment(App.setAuthHeader(App.userToken), review.id, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (page == DEFAULT_PAGE) {
                        adapterModel.clearItem()
                    }
                    view.setCommentMoreBtn(result.comments.size == 5)
                    page.inc()

                    Logger.v("comment result: " + result.toString())
                    Observable.from(result.comments)
                            .subscribe({ data -> adapterModel.addItem(data) }, { Logger.e(it) })
                }) { error ->
                    this.page = DEFAULT_PAGE
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



    private val dialogItemClickListener = { _: View, position: Int ->
        when (position) {
            0 -> Navigator.goUploadReviewDetail(context, review, POSITION_NONE)    // 리뷰 수정 or 상세리뷰 쓰기
            1 -> Navigator.goReviewReport(context, review.id)   //리뷰 신고
        }
    }
}