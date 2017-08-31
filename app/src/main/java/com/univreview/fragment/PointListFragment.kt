package com.univreview.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.univreview.App
import com.univreview.R
import com.univreview.adapter.PointAdapter
import com.univreview.log.Logger
import com.univreview.model.model_kotlin.DataListModel
import com.univreview.model.model_kotlin.RvPoint
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.util.Util
import com.univreview.view.AbsRecyclerView
import com.univreview.view.PointListHeaderView
import com.univreview.view.UnivReviewRecyclerView
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 19..
 */
class PointListFragment : AbsListFragment() {
    private lateinit var recyclerView: UnivReviewRecyclerView
    private lateinit var headerView: PointListHeaderView
    private lateinit var adapter: PointAdapter

    companion object {
        @JvmStatic
        fun getInstance(point: Int): PointListFragment {
            val fragment = PointListFragment()
            val bundle = Bundle()
            bundle.putInt("point", point)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val point = arguments.getInt("point")
        headerView = PointListHeaderView(context)
        headerView.setPoint(point)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        recyclerView = UnivReviewRecyclerView(context)
        recyclerView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        toolbar.setBackgroundColor(Util.getColor(context, R.color.colorPrimary))
        toolbar.setBackBtnVisibility(true)
        toolbar.setTitleTxt("포인트")
        init()
        rootLayout.addView(recyclerView)
        return rootLayout
    }

    fun init() {
        adapter = PointAdapter(context, headerView)
        recyclerView.setBackgroundColor(Util.getColor(context, R.color.backgroundColor))
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        recyclerView.setAdapter(adapter)
    }

    override fun loadMore() {

    }

    override fun refresh() {
        setStatus(Status.REFRESHING)
        callPointHistories()
    }

    override fun getRecyclerView(): AbsRecyclerView? {
        return recyclerView
    }

    fun callPointHistories() {
        Retro.instance.userService().callPointHistroies(App.setHeader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pointResponse(it.data) }, {
                    setStatus(Status.ERROR)
                    ErrorUtils.parseError(it)
                })
    }

    fun pointResponse(pointHistories: List<RvPoint>) {
        if(page == DEFAULT_PAGE) adapter.clear()
        setStatus(Status.IDLE)
        Observable.from(pointHistories)
                .subscribe({ adapter.addItem(it) }, { Logger.e(it) })
    }
}