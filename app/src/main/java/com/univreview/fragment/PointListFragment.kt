package com.univreview.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.univreview.App
import com.univreview.R
import com.univreview.adapter.PointAdapter
import com.univreview.log.Logger
import com.univreview.model.model_kotlin.RvPoint
import com.univreview.model.model_kotlin.Ticket
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.util.Util
import com.univreview.view.AbsRecyclerView
import com.univreview.view.PointListHeaderView
import com.univreview.view.UnivReviewRecyclerView
import com.univreview.view.contract.PointListContract
import com.univreview.view.presenter.PointListPresenter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 19..
 */
class PointListFragment : AbsListFragment(), PointListContract.View {
    private lateinit var recyclerView: UnivReviewRecyclerView
    private lateinit var headerView: PointListHeaderView
    private lateinit var adapter: PointAdapter
    private lateinit var presenter: PointListPresenter

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
        headerView = PointListHeaderView(context).apply {
            setPoint(point)
            setBuyTicketListener(buyTicketListener)
        }
        presenter = PointListPresenter().apply {
            view = this@PointListFragment
            callReviewTickets()
        }
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
        presenter.adapterModel = adapter
    }

    override fun loadMore() {

    }

    override fun refresh() {
        setStatus(Status.REFRESHING)
        presenter.callPointHistories(page)
    }

    override fun getRecyclerView(): AbsRecyclerView? {
        return recyclerView
    }

    override fun setUserTicket(tickets: List<Ticket>) {
        headerView.setUserTicket(tickets)
    }


    private val buyTicketListener = View.OnClickListener {
        _ -> AlertDialog.Builder(context)
                .setMessage("티켓을 구매하시겠습니까?")
                .setPositiveButton("예", { _, _ -> presenter.buyReviewTicket() })
                .setNegativeButton("아니오", null)
                .show()

    }
}