package com.univreview.view.presenter

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.univreview.App
import com.univreview.Navigator
import com.univreview.adapter.contract.ReviewDetailAdapterContract
import com.univreview.fragment.AbsListFragment
import com.univreview.listener.OnItemClickListener
import com.univreview.listener.OnItemLongClickListener
import com.univreview.listener.RvReportItemClickListener
import com.univreview.log.Logger
import com.univreview.model.DataListModel
import com.univreview.model.Review
import com.univreview.model.RvComment
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.util.Util
import com.univreview.view.UnivReviewRecyclerView
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
        Retro.instance.reviewService.callReviewSingle(App.getHeader(), review.id)
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

    private fun onErrorReview(throwable: Throwable) {
        Logger.e("error")
        if (ErrorUtils.ERROR_401 == ErrorUtils.parseError(throwable)) {
            view.showTicketDialog()
        }
    }


    // 가장 최신에 달린 댓글이 list 하단에 존재
    override fun loadComments(page: Int) {
        Retro.instance.reviewService.callReviewComments(App.getHeader(), review.id, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onSuccessComment(page, it) }, { onErrorComment(page, it) })
    }

    private fun onSuccessComment(page: Int, result: DataListModel<RvComment>) {
        view.setStatus(AbsListFragment.Status.IDLE)
        if (result.data.isNotEmpty()) {
            view.setResult(page)
            if(page == DEFAULT_PAGE) adapterModel.clearItem()
            Observable.from(result.data).subscribe { adapterModel.addItem(it) }
        }
    }

    private fun onErrorComment(page: Int, e: Throwable) {
        view.setStatus(AbsListFragment.Status.ERROR)
        if (page == DEFAULT_PAGE) {
            adapterModel.clearItem()
        }
        ErrorUtils.parseError(e)
    }

    // 리뷰 댓글 쓰기
    override fun postReviewComment(message: String?) {
        if (message != null) {
            view.showProgress()
            (view.getRecyclerView() as UnivReviewRecyclerView).scrollToTop()
            Retro.instance.reviewService.postReviewComment(App.getHeader(), review.id, RvComment(message))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterTerminate { view.dismissProgress() }
                    .subscribe({
                        adapterModel.addFirstItem(it.data)
                        view.increaseCommentCnt(true)
                    }, { ErrorUtils.parseError(it) })
        } else {
            Util.toast("메세지를 입력해주세요")
        }
    }


    // 리뷰 댓글 삭제
    override fun onLongClick(view: View, position: Int): Boolean {
        val comment = adapterModel.getItem(position - 1) as RvComment
        Logger.v("delete comment item: $comment")
        // 본인 댓글일 경우
        if (App.userId == (comment.userId)) {
            this.view.showCommentDeleteDialog(DialogInterface.OnClickListener { _, _ ->
                Retro.instance.reviewService.deleteReviewComment(App.getHeader(), review.id, comment.id)
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
        Logger.v("etc btn click")
        Logger.v("review user id: " + review.user?.id)
        //  본인이 더보기 버튼을 클릭했을 경우
        if (review.user?.id == App.userId) {
            view.setDialog(arrayListOf("수정하기"),
                    OnItemClickListener { _, _ -> Navigator.goUploadReviewDetail(context, review) })
        } else {
            view.setDialog(arrayListOf("신고하기"),
                    OnItemClickListener { _, _ -> view.setDialog(arrayListOf("스팸입니다", "부적절합니다"), RvReportItemClickListener(review.id)) })
        }
    }


}