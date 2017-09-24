package com.univreview.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.univreview.App
import com.univreview.Navigator
import com.univreview.R
import com.univreview.adapter.RecentRvAdapter
import com.univreview.log.Logger
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.model.Review
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import kotlinx.android.synthetic.main.new_home_fragment.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 9. 15..
 */
class HomeFragment : BaseFragment() {
    private lateinit var cultureAdapter: RecentRvAdapter
    private lateinit var majorAdapter: RecentRvAdapter

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
        cultureAdapter = RecentRvAdapter(context)
        majorAdapter = RecentRvAdapter(context)
        recentCultureRecyclerView.layoutManager = cultureLayoutManager
        recentMajorRecyclerView.layoutManager = majorLayoutManager
        recentCultureRecyclerView.adapter = cultureAdapter
        recentMajorRecyclerView.adapter = majorAdapter
    }

    private fun callRecentReviewApi() {
        Retro.instance.reviewService().callRecentReview(App.setHeader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cultureResponse(it.data.generalReviews, it.data.masterReviews)
                }, { Logger.e(it) })
    }

    private fun cultureResponse(cultures: List<Review>, majors: List<Review>) {
        Observable.from(cultures)
                .subscribe({ result -> cultureAdapter.addItem(result) }, { ErrorUtils.parseError(it) })
        Observable.from(majors)
                .subscribe({ result -> majorAdapter.addItem(result) }, { ErrorUtils.parseError(it) })
    }
}