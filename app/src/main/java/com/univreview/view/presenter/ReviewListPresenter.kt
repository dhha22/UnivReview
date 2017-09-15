package com.univreview.view.presenter

import android.content.Context
import android.view.View
import com.univreview.App
import com.univreview.Navigator
import com.univreview.adapter.contract.ReviewListAdapterContract
import com.univreview.fragment.AbsListFragment
import com.univreview.listener.OnItemClickListener
import com.univreview.log.Logger
import com.univreview.model.SearchModel
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.model.model_kotlin.DataModel
import com.univreview.model.model_kotlin.Review
import com.univreview.model.model_kotlin.ReviewListModel
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
    lateinit var adapterModel: ReviewListAdapterContract.Model
    var adapterView: ReviewListAdapterContract.View? = null
        set(value) {
            value?.setMoreItemClickListener(moreBtnItemClickListener)
            value?.setOnItemClickListener(this)
        }

    lateinit var searchModel: SearchModel
    var sbjId: Long = 0L
    var profId: Long = 0L

    override fun loadReviewItem(type: ReviewSearchType, page: Int) {
        var observable: Observable<DataModel<ReviewListModel>>? = null

        when (type) {
            ReviewSearchType.MY_REVIEW -> observable = Retro.instance.reviewService().callMyReview(App.setHeader(), page)
            ReviewSearchType.PROFESSOR -> observable = Retro.instance.reviewService().callReviewListByProfessor(App.setHeader(), profId, page)
            ReviewSearchType.SUBJECT -> observable = Retro.instance.reviewService().callReviewListBySubject(App.setHeader(), sbjId, page)
        }

        if (sbjId != 0L && profId != 0L) {
            observable = Retro.instance.reviewService().callReviewListBySubjAndProf(App.setHeader(), sbjId, profId, page)
        }

        observable?.let {
            if (page == DEFAULT_PAGE) adapterModel.clearItem()
            it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response(type, page, it.data) }) { error -> errorResponse(page, error) }
        }
    }

    private fun response(type: ReviewSearchType, page: Int, rvListModel: ReviewListModel) {
        if (rvListModel.reviews.isNotEmpty()) {
            view.setResult(page)
            view.setStatus(AbsListFragment.Status.IDLE)

            if (type != ReviewSearchType.MY_REVIEW) {
                view.setHeaderData(rvListModel.difficultyRateAvg, rvListModel.assignmentRateAvg,
                        rvListModel.attendanceRateAvg, rvListModel.gradeRateAvg, rvListModel.achievementRateAvg)
            }
            Observable.from<Review>(rvListModel.reviews)
                    .subscribe { adapterModel.addItem(it) }

        }
    }

    private fun errorResponse(page: Int, e: Throwable) {
        view.setStatus(AbsListFragment.Status.ERROR)
        if (page == DEFAULT_PAGE) {
            view.setHeaderViewVisibility(false)
            adapterModel.clearItem()
        }
        ErrorUtils.parseError(e)
    }

    override fun loadFilterList() {
        if (sbjId == 0L) {  // subject filter list
            Navigator.goSearch(context, ReviewSearchType.SUBJ_FROM_PROF, profId, false)
        } else if (profId == 0L) {  // professor filter list
            Navigator.goSearch(context, ReviewSearchType.PROF_FROM_SUBJ, sbjId, false)
        }
    }


    // Review List 더보기 이벤트 (1. 수정하기, 2. 신고하기)
    private val moreBtnItemClickListener = OnItemClickListener { _, position ->
        Logger.v("more btn click")
        //  본인의 review item을 클릭했을 경우
        val review = adapterModel.getItem(position) as Review
        if (review.user?.id == App.userId) {
            view.setDialog(arrayListOf("수정하기"),
                    OnItemClickListener { _, _ -> Navigator.goUploadReviewDetail(context, review) })
        } else {
            view.setDialog(arrayListOf("신고하기"),
                    OnItemClickListener { _, _ -> Navigator.goReviewReport(context, review.id) })
        }
    }

    // Review List Item
    override fun onItemClick(view: View, position: Int) {
        Logger.v("click item: " + (adapterModel.getItem(position) as Review).updateNotificationPublisher)
        Navigator.goReviewDetail(context, adapterModel.getItem(position) as Review)
    }

}