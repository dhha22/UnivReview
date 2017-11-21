package com.univreview.fragment.review

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dhha22.bindadapter.BindAdapter
import com.dhha22.bindadapter.listener.EndlessScrollListener
import com.dhha22.bindadapter.listener.OnItemClickListener
import com.dhha22.bindadapter.listener.ScrollEndSubscriber
import com.univreview.R
import com.univreview.adapter.ReviewListAdapter
import com.univreview.dialog.ListDialog
import com.univreview.fragment.AbsListFragment
import com.univreview.log.Logger
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.model.enumeration.ReviewType
import com.univreview.util.SimpleDividerItemDecoration
import com.univreview.util.Util
import com.univreview.view.AbsRecyclerView
import com.univreview.view.UnivReviewRecyclerView
import com.univreview.view.contract.ReviewListContract
import com.univreview.view.presenter.ReviewListPresenter
import kotlinx.android.synthetic.main.fragment_review_list.*

/**
 * Created by DavidHa on 2017. 8. 7..
 */
class ReviewListFragment : AbsListFragment(), ReviewListContract.View, ScrollEndSubscriber {
    lateinit var adapter: BindAdapter
    lateinit var innerAdapter : ReviewListAdapter
    lateinit var type: ReviewSearchType
    lateinit var name: String
    lateinit var presenter: ReviewListPresenter

    companion object {
        @JvmStatic
        fun getInstance(type: ReviewSearchType, id: Long, name: String): ReviewListFragment {
            val fragment = ReviewListFragment()
            val bundle = Bundle()
            bundle.putSerializable("type", type)
            bundle.putLong("id", id)
            bundle.putString("name", name)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments.getSerializable("type") as ReviewSearchType
        name = arguments.getString("name")
        val id = arguments.getLong("id")
        Logger.v("type: $type , id: $id, name: $name")

        adapter = BindAdapter(context)

        presenter = ReviewListPresenter()
        presenter.apply {
            view = this@ReviewListFragment
            context = getContext()
            if (type == ReviewSearchType.SUBJECT) sbjId = id
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_review_list, container, false)
        rootLayout.addView(view)
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        when (type) {
        // My Review
            ReviewSearchType.MY_REVIEW -> {
                innerAdapter = ReviewListAdapter(context, ReviewType.MY_REVIEW)
                appBarLayout.visibility = View.GONE
                toolbar.setBackBtnVisibility(true)
                toolbar.setTitleTxt(name)
            }

        // Subject List
            ReviewSearchType.SUBJECT -> {
                innerAdapter = ReviewListAdapter(context, ReviewType.READ_REVIEW)
                toolbar.visibility = View.GONE
                innerToolbar.setBackBtnVisibility(true)
                innerToolbar.setTitleTxt(name)
            }

        }
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        recyclerView.setMode(UnivReviewRecyclerView.Mode.DISABLED)
        recyclerView.setBackgroundColor(Util.getColor(context, R.color.backgroundColor))
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setAdapter(adapter)

        adapter.setInnerAdapter(innerAdapter)
        recyclerView.addItemDecoration(SimpleDividerItemDecoration(context))
        presenter.let {
            it.adapterModel = adapter
            it.innerAdapterModel = innerAdapter
            it.innerAdapterView = innerAdapter
        }

        recyclerView.addOnScrollListener(EndlessScrollListener(this))
    }

    override fun onScrollEnd() {
        lastItemExposed()
    }

    override fun getRecyclerView(): AbsRecyclerView? {
        return recyclerView
    }

    override fun refresh() {
        Logger.v("refresh")
        setStatus(Status.REFRESHING)
        presenter.loadReviewItem(type, DEFAULT_PAGE)
    }

    override fun loadMore() {
        setStatus(Status.LOADING_MORE)
        Logger.v("page: " + page)
        presenter.loadReviewItem(type, page)
    }

    override fun setFilterName(filterName: String) {

    }

    override fun setDialog(list: List<String>, itemClickListener: OnItemClickListener) {
        ListDialog(context, list, itemClickListener).show()
    }
}