package com.univreview.view.contract

import com.univreview.fragment.AbsListFragment
import com.univreview.model.model_kotlin.Ticket

/**
 * Created by DavidHa on 2017. 9. 1..
 */
interface PointListContract {
    interface View : BaseView {
        fun setResult(page: Int)
        fun setStatus(status: AbsListFragment.Status)
        fun setUserTicket(ticket: Ticket)
    }

    fun callPointHistories(page: Int)
    fun callReviewTicket()
    fun buyReviewTicket()
}