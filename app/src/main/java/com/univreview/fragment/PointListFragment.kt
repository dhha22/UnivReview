package com.univreview.fragment

import android.os.Bundle
import com.univreview.view.AbsRecyclerView
import com.univreview.view.UnivReviewRecyclerView

/**
 * Created by DavidHa on 2017. 8. 19..
 */
class PointListFragment : AbsListFragment() {
    private lateinit var recyclerView : UnivReviewRecyclerView

    companion object {
        @JvmStatic
        fun getInstance(point : Int) : PointListFragment{
            val fragment  = PointListFragment()
            val bundle  =  Bundle()
            bundle.putInt("point", point)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun loadMore() {

    }

    override fun refresh() {

    }

    override fun getRecyclerView(): AbsRecyclerView? {
        return recyclerView
    }
}