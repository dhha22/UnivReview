package com.univreview.listener

import android.view.View
import com.dhha22.bindadapter.listener.OnItemClickListener
import com.univreview.App
import com.univreview.log.Logger
import com.univreview.model.RvReport
import com.univreview.network.Retro
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 9. 20..
 */
class RvReportItemClickListener(val reviewId: Long) : OnItemClickListener {

    private val reportMsg1 = "스팸입니다"
    private val reportMsg2 = "부적절합니다"
    override fun onItemClick(view: View?, position: Int) {
        if (position == 0) {
            postReviewReport(reportMsg1)
        } else if (position == 1) {
            postReviewReport(reportMsg2)
        }
    }

    fun postReviewReport(message: String) {
        Retro.instance.reviewService.reviewReport(App.getHeader(), reviewId, RvReport(message))
                .subscribeOn(Schedulers.io())
                .subscribe({ Logger.v(it) }, { Logger.e(it) })
    }
}