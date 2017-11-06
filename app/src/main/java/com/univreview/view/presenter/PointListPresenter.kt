package com.univreview.view.presenter

import android.content.Context
import com.dhha22.bindadapter.BindAdapterContract
import com.univreview.App
import com.univreview.fragment.AbsListFragment
import com.univreview.log.Logger
import com.univreview.model.RvPoint
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.util.Util
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
    lateinit var context: Context
    lateinit var adapterModel: BindAdapterContract.Model


    override fun callPointHistories(page: Int) {
        Retro.instance.userService.callPointHistories(App.getHeader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pointResponse(it.data.point, it.data.pointHistories, page) }, {
                    view.setStatus(AbsListFragment.Status.ERROR)
                    ErrorUtils.parseError(it)
                })
    }

    private fun pointResponse(point: Int, pointHistories: List<RvPoint>, page: Int) {
        view.setStatus(AbsListFragment.Status.IDLE)
        view.setPoint(point)
        if (pointHistories.isNotEmpty()) {
            if (page == AbsListFragment.DEFAULT_PAGE) adapterModel.clearItem()
            Observable.from(pointHistories)
                    .subscribe({ adapterModel.addItem(it) }, { Logger.e(it) })
        }
    }

    override fun callReviewTicket() {
        Retro.instance.userService.callTicket(App.getHeader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.setUserTicket(it.data)
                }, { ErrorUtils.parseError(it) })
    }

    override fun buyReviewTicket() {
        Retro.instance.userService.buyReviewTicket(App.getHeader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.setUserTicket(it.data)
                    callPointHistories(DEFAULT_PAGE)
                }, {
                    Util.simpleMessageDialog(context, ErrorUtils.getErrorMessage(it))
                })
    }

}