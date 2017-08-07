package com.univreview.view.presenter

import com.univreview.App
import com.univreview.adapter.contract.ReviewListAdapterContract
import com.univreview.fragment.AbsListFragment
import com.univreview.log.Logger
import com.univreview.model.*
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.contract.ReviewListContract
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import kotlin.properties.Delegates

/**
 * Created by DavidHa on 2017. 8. 6..
 */
class ReviewListPresenter : ReviewListContract {
    companion object {
        val DEFAULT_PAGE = 1
    }

    lateinit var view: ReviewListContract.View
    lateinit var adapterModel: ReviewListAdapterContract.Model
    lateinit var adapterView: ReviewListAdapterContract.View
    lateinit var searchModel: SearchModel
    var subjectId: Long? = null
    var professorId: Long? = null
    var userId: Long? = null

    override fun loadReviewItem(type: ReviewSearchType, page: Int) {
        val observable: Observable<ReviewListModel>
        if (type == ReviewSearchType.MY_REVIEW) {
            observable = Retro.instance.reviewService().getMyReviews(App.setAuthHeader(App.userToken), page)

        } else {
            observable = Retro.instance.reviewService().getReviews(App.setAuthHeader(App.userToken),
                    subjectId, professorId, userId, page)
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { if (page == DEFAULT_PAGE) adapterModel.clearItem() }
                .subscribe({ result -> response(type, page, result) }) { error -> errorResponse(page, error) }
    }

    private fun response(type: ReviewSearchType, page: Int, reviewListModel: ReviewListModel) {
        view.setResult(page)
        view.setStatus(AbsListFragment.Status.IDLE)
        reviewListModel.let {
            Logger.v("result size: " + it.reviews.size)
            Logger.v("review: " + it.totalAverageRates + " " + it.reviewAverage)
            if (type != ReviewSearchType.MY_REVIEW) {
                view.setHeaderData(reviewListModel.totalAverageRates, reviewListModel.reviewAverage)
            }
            Observable.from<Review>(reviewListModel.reviews)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result -> adapterModel.addItem(result) }, { Logger.e(it) })

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
        Retro.instance.searchService().getProfessorFromSubject(subjectId, DEFAULT_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.searchResponse(it) },  { ErrorUtils.parseError(it) })

    }

    override fun searchSubject(professorId: Long) {
        Logger.v("professor id: " + professorId);
        Retro.instance.searchService().getSubjectFromProfessor(professorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.searchResponse(it) }, { ErrorUtils.parseError(it) })
    }

    private fun searchResponse(result: SearchModel) {
        this.searchModel = result
        var observable: Observable<String>? = null
        if(result.subjects != null){
            observable = Observable.from(result.subjects).map { it.getName() }
        }else if(result.professors != null){
            observable = Observable.from(result.professors).map { it.getName() }
        }
        observable?.toList()?.subscribe({ name -> view.setDialog(name) }, { Logger.e(it) })
    }
}