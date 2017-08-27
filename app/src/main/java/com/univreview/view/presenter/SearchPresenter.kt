package com.univreview.view.presenter

import com.univreview.App
import com.univreview.adapter.contract.SearchAdapterContract
import com.univreview.fragment.AbsListFragment
import com.univreview.log.Logger
import com.univreview.model.SearchModel
import com.univreview.model.model_kotlin.AbstractDataProvider
import com.univreview.model.model_kotlin.DataListModel
import com.univreview.model.model_kotlin.Department
import com.univreview.model.model_kotlin.ResultModel
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.contract.SearchContract
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.io.UncheckedIOException

/**
 * Created by DavidHa on 2017. 8. 6..
 */
class SearchPresenter : SearchContract {
    companion object {
        val DEFAULT_PAGE = 1
    }

    private var subscription : CompositeSubscription = CompositeSubscription()
    var subjectType: String? = null
    lateinit var view: SearchContract.View
    lateinit var searchAdapterModel: SearchAdapterContract.Model

    override fun searchUniversity(name: String, page: Int) {
        setObservable(Retro.instance.searchService().callUniversityList(name), page)
    }

    override fun searchDepartment(id: Long, name: String, page: Int) {
        setObservable(Retro.instance.searchService().callDepartmentList(id, name), page)
    }

    override fun searchMajor(id: Long, name: String, page: Int) {
        setObservable(Retro.instance.searchService().callMajorList(App.universityId.toLong(), id, name), page)
    }

    override fun searchProfessor(departmentId: Long?, name: String, page: Int) {
        setObservable(Retro.instance.searchService().callProfessors(App.setHeader(), name), page)
    }

    override fun searchSubject(majorId: Long?, name: String, page: Int) {
        Logger.v("university id: " + App.universityId)
        Logger.v("major id: " + majorId)
        setObservable(Retro.instance.searchService().callSubjects(App.setHeader(), majorId, name), page)
    }

    override fun searchProfFromSubj(subjectId: Long, name: String, page: Int) {
        Logger.v("subjectId: " + subjectId)
        setObservable(Retro.instance.searchService().callCourse(App.setHeader(), subjectId), page)
    }

    private fun <T> setObservable(observable: Observable<DataListModel<T>>, page: Int) {
        subscription.add( observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { if (page == DEFAULT_PAGE) searchAdapterModel.clear() }
                .subscribe({
                    @Suppress("UNCHECKED_CAST")
                    response(it.data as List<AbstractDataProvider>)
                }, { this.errorResponse(it) }))
    }


    private fun response(result: List<AbstractDataProvider>) {
        view.setStatus(AbsListFragment.Status.IDLE)
        Observable.from(result)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data -> searchAdapterModel.addItem(data) }, { Logger.e(it) })
    }


    private fun errorResponse(e: Throwable) {
        view.setStatus(AbsListFragment.Status.ERROR)
        ErrorUtils.parseError(e)
    }

    override fun stopSearch() {
        subscription.unsubscribe()
    }
}