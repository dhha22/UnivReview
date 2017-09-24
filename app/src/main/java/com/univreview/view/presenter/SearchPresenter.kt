package com.univreview.view.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.univreview.App
import com.univreview.Navigator
import com.univreview.adapter.contract.SearchAdapterContract
import com.univreview.fragment.AbsListFragment
import com.univreview.listener.OnItemClickListener
import com.univreview.log.Logger
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.model.AbstractDataProvider
import com.univreview.model.DataListModel
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.contract.SearchContract
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by DavidHa on 2017. 8. 6..
 */
class SearchPresenter : SearchContract, OnItemClickListener {
    companion object {
        val DEFAULT_PAGE = 1
    }

    private var subscription: CompositeSubscription = CompositeSubscription()
    lateinit var view: SearchContract.View
    lateinit var context: Context
    lateinit var type: ReviewSearchType
    lateinit var searchAdapterModel: SearchAdapterContract.Model
    var searchAdapterView: SearchAdapterContract.View? = null
        set(value) {
            field = value
            value?.setOnItemClickListener(this)
        }
    var id: Long? = null
        set(value) {
            if (value != 0L) {
                field = value
            }
        }

    override fun callSearchApi(name: String, page: Int) {
        when (type) {
            ReviewSearchType.UNIVERSITY -> searchUniversity(name, page)
            ReviewSearchType.MAJOR -> searchMajor(name, page)
            ReviewSearchType.SUBJECT, ReviewSearchType.SUBJECT_WITH_RESULT -> searchSubject(name, page)
            ReviewSearchType.PROF_FROM_SUBJ -> searchProfFromSubj(name, page)
        }
    }

    // 대학교 검색 (로그인)
    private fun searchUniversity(name: String, page: Int) {
        setObservable(Retro.instance.searchService.callUniversityList(name, page), page)
    }

    // 전공 검색 (로그인)
    private fun searchMajor(name: String, page: Int) {
        setObservable(Retro.instance.searchService.callMajorList("M", id, name, page), page)
    }

    // 과목 검색 (리뷰 검색)
    private fun searchSubject(name: String, page: Int) {
        setObservable(Retro.instance.searchService.callSubjects(App.setHeader(), id, name, page), page)
    }

    // 과목에 속한 교수 검색 (리뷰 쓰기)
    private fun searchProfFromSubj(name: String, page: Int) {
        setObservable(Retro.instance.searchService.callCourse(App.setHeader(), id, page), page)
    }


    private fun <T> setObservable(observable: Observable<DataListModel<T>>, page: Int) {
        subscription.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { if (page == DEFAULT_PAGE) searchAdapterModel.clearItem() }
                .subscribe({
                    @Suppress("UNCHECKED_CAST")
                    response(page, it.data as List<AbstractDataProvider>)
                }, { this.errorResponse(it) }))
    }


    private fun response(page : Int, result: List<AbstractDataProvider>) {
        view.setStatus(AbsListFragment.Status.IDLE)
        if (result.isNotEmpty()) {
            Logger.v("load more $page")
            view.setResult(page)
            Observable.from(result)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ data -> searchAdapterModel.addItem(data) }, { Logger.e(it) })
        }
    }


    private fun errorResponse(e: Throwable) {
        view.setStatus(AbsListFragment.Status.ERROR)
        ErrorUtils.parseError(e)
    }

    // search list item click
    override fun onItemClick(view: View?, position: Int) {
        // review list 로 이동 (과목 검색 결과)
        if (type == ReviewSearchType.SUBJECT) {
            val searchStr = searchAdapterModel.getItem(position).name
            this.view.setInputStr(searchStr)
            Navigator.goReviewList(context, type, searchAdapterModel.getItem(position).id, searchStr)
        } else {    // 보여주는 search
            val intent = Intent()
            searchAdapterModel.getItem(position).let {
                intent.putExtra("id", it.id)
                intent.putExtra("name", it.name)
                intent.putExtra("type", type)
                (context as Activity).setResult(Activity.RESULT_OK, intent)
                (context as Activity).onBackPressed()
            }
        }
    }

    override fun stopSearch() {
        subscription.unsubscribe()
    }
}