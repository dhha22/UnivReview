package com.univreview.view.presenter

import android.content.Context
import android.view.View
import com.dhha22.bindadapter.BindAdapterContract
import com.dhha22.bindadapter.listener.OnItemClickListener
import com.univreview.App
import com.univreview.Navigator
import com.univreview.adapter.contract.ReviewListAdapterContract
import com.univreview.fragment.AbsListFragment

import com.univreview.listener.RvReportItemClickListener
import com.univreview.log.Logger
import com.univreview.model.DataModel
import com.univreview.model.Review
import com.univreview.model.ReviewListModel
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.contract.ReviewListContract
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 6..
 */
class ReviewListPresenter : ReviewListContract, OnItemClickListener {
    companion object {
        val DEFAULT_PAGE = 1
    }

    lateinit var context: Context
    lateinit var view: ReviewListContract.View
    lateinit var adapterModel : BindAdapterContract.Model
    lateinit var innerAdapterModel: ReviewListAdapterContract.Model
    var innerAdapterView: ReviewListAdapterContract.View? = null
        set(value) {
            value?.setMoreItemClickListener(moreBtnItemClickListener)
            value?.setOnItemClickListener(this)
        }


    var sbjId: Long = 0L
    var profId: Long = 0L

    override fun loadReviewItem(type: ReviewSearchType, page: Int) {
        var observable: Observable<DataModel<ReviewListModel>>? = null

        when (type) {
            ReviewSearchType.MY_REVIEW -> observable = Retro.instance.reviewService.callMyReview(App.getHeader(), page)
            ReviewSearchType.SUBJECT -> observable = Retro.instance.reviewService.callReviewListBySubject(App.getHeader(), sbjId, page)
        }

        if (sbjId != 0L && profId != 0L) {
            observable = Retro.instance.reviewService.callReviewListBySubjAndProf(App.getHeader(), sbjId, profId, page)
        }

        observable?.let {
            if (page == DEFAULT_PAGE) innerAdapterModel.clearItem()
            it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response(page, it.data) }) { error -> errorResponse(page, error) }
        }
    }

    private fun response(page: Int, rvListModel: ReviewListModel) {
        view.setStatus(AbsListFragment.Status.IDLE)
        if (rvListModel.reviews.isNotEmpty()) {
            Logger.v("review is not empty $page")
            view.setResult(page)
            Observable.from<Review>(rvListModel.reviews)
                    .doAfterTerminate { adapterModel.notifyData() }
                    .subscribe { innerAdapterModel.addItem(it) }

        }
    }

    private fun errorResponse(page: Int, e: Throwable) {
        view.setStatus(AbsListFragment.Status.ERROR)
        if (page == DEFAULT_PAGE) {
            innerAdapterModel.clearItem()
        }
        if (ErrorUtils.parseError(e) == ErrorUtils.ERROR_401) {
            view.showTicketDialog()
        }
    }

    override fun loadFilterList() {
        Navigator.goSearch(context, ReviewSearchType.PROF_FROM_SUBJ, sbjId)
    }


    // Review List 더보기 이벤트 (1. 수정하기, 2. 신고하기)
    private val moreBtnItemClickListener = OnItemClickListener { _, position ->
        Logger.v("more btn click")
        //  본인의 review item을 클릭했을 경우
        val review = innerAdapterModel.getItem(position) as Review
        if (review.user?.id == App.userId) {
            view.setDialog(arrayListOf("수정하기"),
                    OnItemClickListener { _, _ -> Navigator.goUploadReviewDetail(context, review) })
        } else {
            view.setDialog(arrayListOf("신고하기"),
                    OnItemClickListener { _, _ -> view.setDialog(arrayListOf("스팸입니다", "부적절합니다"), RvReportItemClickListener(review.id)) })
        }
    }

    // Review List Item
    override fun onItemClick(view: View, position: Int) {
        Logger.v("click item: " + (innerAdapterModel.getItem(position) as Review).updateNotificationPublisher)
        Navigator.goReviewDetail(context, innerAdapterModel.getItem(position) as Review)
    }

}