package com.univreview.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dhha22.bindadapter.BindAdapter
import com.univreview.App
import com.univreview.Navigator
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.model.Review
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.RecentReviewItemView
import kotlinx.android.synthetic.main.new_home_fragment.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 9. 15..
 */
class HomeFragment : BaseFragment() {
    private lateinit var cultureAdapter: BindAdapter
    private lateinit var majorAdapter: BindAdapter

    companion object {
        @JvmStatic
        fun getInstance(): HomeFragment {
            val fragment = HomeFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.new_home_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        callRecentReviewApi()
    }

    fun init() {
        subjectSearch.setOnClickListener { Navigator.goSearch(context, ReviewSearchType.SUBJECT) }

        //recycler view
        val cultureLayoutManager = LinearLayoutManager(context)
        cultureLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val majorLayoutManager = LinearLayoutManager(context)
        majorLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        cultureAdapter = BindAdapter(context).addLayout(RecentReviewItemView::class.java)
        majorAdapter = BindAdapter(context).addLayout(RecentReviewItemView::class.java)
        recentCultureRecyclerView.layoutManager = cultureLayoutManager
        recentMajorRecyclerView.layoutManager = majorLayoutManager
        recentCultureRecyclerView.adapter = cultureAdapter
        recentMajorRecyclerView.adapter = majorAdapter
    }

    private fun callRecentReviewApi() {
        Retro.instance.reviewService.callRecentReview(App.getHeader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cultureResponse(it.data.generalReviews, it.data.masterReviews)
                }, { Logger.e(it) })
    }

    private fun cultureResponse(cultures: List<Review>, majors: List<Review>) {
        Observable.from(cultures)
                .doAfterTerminate { cultureAdapter.notifyData() }
                .subscribe({ result -> cultureAdapter.addItem(result) }, { ErrorUtils.parseError(it) })
        Observable.from(majors)
                .doAfterTerminate { majorAdapter.notifyData() }
                .subscribe({ result -> majorAdapter.addItem(result) }, { ErrorUtils.parseError(it) })
    }
}