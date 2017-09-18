package com.univreview.view.contract

import com.univreview.fragment.AbsListFragment

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface SearchContract {
    interface View : BaseView {
        fun setResult(page: Int)
        fun setStatus(status: AbsListFragment.Status)
        fun setInputStr(searchStr : String)
    }

    fun callSearchApi(name: String, page: Int)
    fun stopSearch()
}