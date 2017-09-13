package com.univreview.view.presenter

import com.univreview.App
import com.univreview.adapter.contract.PointAdapterContract
import com.univreview.fragment.AbsListFragment
import com.univreview.log.Logger
import com.univreview.model.model_kotlin.RvPoint
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.contract.PointListContract
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 9. 1..
 */
class PointListPresenter : PointListContract {
    private val DEFAULT_PAGE = 1
    lateinit var view: PointListContract.View
    lateinit var adapterModel: PointAdapterContract.Model


    override fun callPointHistories(page: Int) {
        Retro.instance.userService().callPointHistories(App.setHeader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pointResponse(it.data, page) }, {
                    view.setStatus(AbsListFragment.Status.ERROR)
                    ErrorUtils.parseError(it)
                })
    }

    private fun pointResponse(pointHistories: List<RvPoint>, page: Int) {
        if (pointHistories.isNotEmpty()) {
            if (page == AbsListFragment.DEFAULT_PAGE) adapterModel.clearItem()
            view.setStatus(AbsListFragment.Status.IDLE)
            Observable.from(pointHistories)
                    .subscribe({ adapterModel.addItem(it) }, { Logger.e(it) })
        }
    }

    override fun callReviewTickets() {
        Retro.instance.userService().callTicket(App.setHeader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.setUserTicket(it.data)
                }, { ErrorUtils.parseError(it) })
    }

    override fun buyReviewTicket() {
        Retro.instance.userService().buyReviewTicket(App.setHeader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ callPointHistories(DEFAULT_PAGE) }, { ErrorUtils.parseError(it) })
    }

}