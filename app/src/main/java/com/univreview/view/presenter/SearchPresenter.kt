package com.univreview.view.presenter

import com.univreview.App
import com.univreview.adapter.contract.SearchAdapterContract
import com.univreview.fragment.AbsListFragment
import com.univreview.log.Logger
import com.univreview.model.*
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.contract.SearchContract
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 6..
 */
class SearchPresenter : SearchContract {
    companion object {
        val DEFAULT_PAGE = 1
    }
    var subjectType : String? = null
    lateinit var view : SearchContract.View
    lateinit var searchAdapterModel : SearchAdapterContract.Model

    override fun searchUniversity(name: String, page: Int) {
        setObservable(Retro.instance.searchService().getUniversities(name, page), page)
    }

    override fun searchDepartment(id: Long, name: String, page: Int) {
        setObservable(Retro.instance.searchService().getDepartments(id, name, subjectType, page), page)
    }

    override fun searchMajor(id: Long, name: String, page: Int) {
        setObservable(Retro.instance.searchService().getMajors(App.universityId.toLong(), id, name, subjectType, page), page)
    }

    override fun searchProfessor(departmentId: Long?, name: String, page: Int) {
        setObservable(Retro.instance.searchService().getProfessors(App.universityId.toLong(), departmentId, name, page), page)
    }

    override fun searchSubject(majorId: Long?, name: String, page: Int) {
        Logger.v("university id: " + App.universityId)
        Logger.v("major id: " + majorId)
        setObservable(Retro.instance.searchService().getSubjects(App.universityId.toLong(), majorId, name, page), page)
    }

    override fun searchProfFromSubj(subjectId: Long, name: String, page: Int) {
        Logger.v("subjectId: " + subjectId)
        setObservable(Retro.instance.searchService().getProfessorFromSubject(subjectId, page), page)
    }

    private fun setObservable(observable: Observable<SearchModel>, page: Int) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { if (page == DEFAULT_PAGE) searchAdapterModel.clear() }
                .subscribe({ result -> response(result, page) },  { this.errorResponse(it) })
    }

    private fun response(result: SearchModel, page: Int) {
        view.setResult(page)
        view.setStatus(AbsListFragment.Status.IDLE)
        val list: List<AbstractDataProvider> =
                if (result.universities != null) {
                    result.universities
                } else if (result.departments != null) {
                    result.departments
                } else if (result.majors != null) {
                    result.majors
                } else if (result.professors != null) {
                    result.professors
                } else if (result.subjects != null) {
                    result.subjects
                } else {
                    ArrayList<AbstractDataProvider>()
                }

        Observable.from(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data -> searchAdapterModel.addItem(data) }, { Logger.e(it) })

    }


    private fun errorResponse(e: Throwable) {
        view.setStatus(AbsListFragment.Status.ERROR)
        ErrorUtils.parseError(e)
    }
}