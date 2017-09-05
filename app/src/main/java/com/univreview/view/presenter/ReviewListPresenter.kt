package com.univreview.view.presenter

import android.content.Context
import android.view.View
import com.univreview.App
import com.univreview.Navigator
import com.univreview.adapter.contract.ReviewListAdapterContract
import com.univreview.dialog.ListDialog
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
            ReviewSearchType.MY_REVIEW -> observable = Retro.instance.reviewService().callMyReview(App.setHeader())
            ReviewSearchType.PROFESSOR -> observable = Retro.instance.reviewService().callReviewListByProfessor(App.setHeader(), profId)
            ReviewSearchType.SUBJECT -> observable = Retro.instance.reviewService().callReviewListBySubject(App.setHeader(), sbjId)
        }

        if (sbjId != 0L && profId != 0L) {
            observable = Retro.instance.reviewService().callReviewListBySubjAndProf(App.setHeader(), sbjId, profId)
        }

        observable?.let {
            if (page == DEFAULT_PAGE) adapterModel.clearItem()
            it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response(type, page, it.data) }) { error -> errorResponse(page, error) }
        }
    }

    private fun response(type: ReviewSearchType, page: Int, reviewListModel: ReviewListModel) {
        view.setResult(page)
        view.setStatus(AbsListFragment.Status.IDLE)
        reviewListModel.let {
            if (type != ReviewSearchType.MY_REVIEW) {
                //view.setHeaderData(reviewListModel.totalAverageRates, reviewListModel.reviewAverage)
            }
            Observable.from<Review>(reviewListModel.reviews)
                    .subscribe({
                        Logger.v("add item: $it")
                        adapterModel.addItem(it)
                    }, { Logger.e(it) })

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

    override fun searchProfessor(subjectId: Long) {
        Logger.v("subject id: " + subjectId)


    }

    override fun searchSubject(professorId: Long) {
        Logger.v("professor id: " + professorId);

    }

    private fun searchResponse(result: SearchModel) {
        this.searchModel = result
        var observable: Observable<String>? = null
        if (result.subjects != null) {
            observable = Observable.from(result.subjects).map { it.getName() }
        } else if (result.professors != null) {
            observable = Observable.from(result.professors).map { it.getName() }
        }
        //observable?.toList()?.subscribe({ name -> view.setDialog(name) }, { Logger.e(it) })
    }


    // Review List 더보기 이벤트 (1. 수정, 2. 신고...)
    private val moreBtnItemClickListener = OnItemClickListener { _, position ->
        Logger.v("more btn click")
        //  본인의 review item을 클릭했을 경우
        val review = adapterModel.getItem(position) as Review
        if (review.user?.uid == App.userId) {
            view.setDialog(arrayListOf("수정하기"),
                    OnItemClickListener { _, _ -> Navigator.goReviewDetail(context, review) })
        } else {
            view.setDialog(arrayListOf("신고하기"),
                    OnItemClickListener { _, _ -> Navigator.goReviewReport(context, review.id) })
        }
    }

    // Review List Item
    override fun onItemClick(view: View, position: Int) {
        Logger.v("review list item click position: " + position)
        Navigator.goReviewDetail(context, adapterModel.getItem(position) as Review)
    }

}