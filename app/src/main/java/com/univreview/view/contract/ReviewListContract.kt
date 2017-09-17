package com.univreview.view.contract

import com.univreview.fragment.AbsListFragment
import com.univreview.listener.OnItemClickListener
import com.univreview.model.enumeration.ReviewSearchType

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface ReviewListContract {
    interface View : BaseView {
        fun setDialog(list: List<String>, itemClickListener: OnItemClickListener)
        fun setResult(page: Int)
        fun setStatus(status: AbsListFragment.Status)
        fun setFilterName(filterName: String)
    }

    fun loadReviewItem(type: ReviewSearchType, page: Int)
    fun loadFilterList()
}