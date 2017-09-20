package com.univreview.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.univreview.log.Logger
import com.univreview.view.AbsRecyclerView
import com.univreview.view.UnivReviewRecyclerView
import java.util.HashMap

/**
 * Created by DavidHa on 2017. 8. 5..
 */
 abstract class AbsListFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    enum class Status {
        IDLE, LOADING_MORE, REFRESHING, ERROR
    }

    companion object {
        @JvmStatic
        val DEFAULT_PAGE = 1
        private val needRefresh = HashMap<Class<*>, Boolean>()

        @JvmStatic
        fun setNeedRefresh(clazz: Class<*>, refresh: Boolean) {
            needRefresh.put(clazz, refresh)
        }
    }

    protected var page = DEFAULT_PAGE
    private var status = Status.LOADING_MORE

    private fun isNeedRefresh(): Boolean {
        val aClass = (this as Any).javaClass
        if (needRefresh.containsKey(aClass)) {
            return needRefresh[aClass]?:true
        }
        return true
    }

    fun setResult(page: Int) {
        this.page = page + 1
        Logger.v("set result page: " + this.page)
    }

    protected fun lastItemExposed() {
        if (status != Status.LOADING_MORE && status != Status.REFRESHING) {
            loadMore()
        }
    }

    fun setStatus(status: Status) {
        Logger.i("status " + status)
        if (this.status == status) {
            return
        }
        this.status = status
        val recyclerView: AbsRecyclerView? = getRecyclerView()

        when (status) {
            AbsListFragment.Status.IDLE -> {
                recyclerView?.onRefreshComplete()
            }
            AbsListFragment.Status.REFRESHING -> {
                recyclerView?.isRefreshing = true
            }
            AbsListFragment.Status.ERROR -> {
                recyclerView?.onRefreshComplete()
            }
        }
    }

    abstract fun getRecyclerView(): AbsRecyclerView?

    abstract fun loadMore()

    abstract fun refresh()

    override fun onResume() {
        super.onResume()
        if (isNeedRefresh()) {
            setNeedRefresh((this as Any).javaClass, false)
            if (status != Status.REFRESHING) {
                page = DEFAULT_PAGE
                refresh()
            }
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView()?.setOnRefreshListener(this)
        Logger.i("setOnRefreshListener " + (this as Any).javaClass.simpleName)
    }

    override fun onRefresh() {
        if (status != Status.REFRESHING) {
            refresh()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (getRecyclerView() is UnivReviewRecyclerView) {
            (getRecyclerView() as UnivReviewRecyclerView).removeAllViews()
            (getRecyclerView() as UnivReviewRecyclerView).setAdapter(null)
        } else if (getRecyclerView() is RecyclerView) {
            (getRecyclerView() as RecyclerView).removeAllViews()
            (getRecyclerView() as RecyclerView).adapter = null
        }
        setNeedRefresh((this as Any).javaClass, true)
    }

}