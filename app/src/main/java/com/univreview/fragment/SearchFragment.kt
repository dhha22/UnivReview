package com.univreview.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dhha22.bindadapter.BindAdapter
import com.dhha22.bindadapter.listener.EndlessScrollListener
import com.dhha22.bindadapter.listener.ScrollEndSubscriber
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.util.Util
import com.univreview.view.SearchListItemView
import com.univreview.view.UnivReviewRecyclerView
import com.univreview.view.contract.SearchContract
import com.univreview.view.presenter.SearchPresenter
import com.univreview.widget.PreCachingLayoutManager
import kotlinx.android.synthetic.main.fragment_search.*
import rx.Observable
import rx.Subscription
import java.util.concurrent.TimeUnit

/**
 * Created by DavidHa on 2017. 9. 18..
 */
class SearchFragment : AbsListFragment(), SearchContract.View, ScrollEndSubscriber {
    private lateinit var adapter: BindAdapter
    private lateinit var presenter: SearchPresenter
    private var timer: Subscription? = null

    companion object {
        @JvmStatic
        fun getInstance(type: ReviewSearchType, id: Long): SearchFragment {
            val fragment = SearchFragment()
            val bundle = Bundle()
            bundle.putSerializable("type", type)
            bundle.putLong("id", id)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = arguments.getSerializable("type") as ReviewSearchType
        val id = arguments.getLong("id")
        Logger.v("id: $id, type: $type")
        presenter = SearchPresenter().apply {
            view = this@SearchFragment
            context = getContext()
            this.type = type
            this.id = id
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_search, container, false)
        toolbar.setSearchToolbarStyle()
        rootLayout.addView(view)
        rootLayout.setBackgroundColor(Util.getColor(context, R.color.searchBgColor))
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        input.hint = getHintStr()
        deleteBtn.setOnClickListener { input.text = null }
        input.addTextChangedListener(textWatcher)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        //recycler view
        adapter = BindAdapter(context).addLayout(SearchListItemView::class.java)
        val layoutManager = PreCachingLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.setAdapter(adapter)
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        recyclerView.addOnScrollListener(EndlessScrollListener(this))

        presenter.searchAdapterModel = adapter
        presenter.searchAdapterView = adapter
    }

    override fun onScrollEnd() {
        lastItemExposed()
    }

    // search input textWatcher

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (timer != null && timer!!.isUnsubscribed) {
                timer!!.unsubscribe()
            }
        }

        override fun afterTextChanged(s: Editable) {
            Logger.v("search str: " + s.toString())
            if (s.isNotEmpty()) {
                deleteBtn.visibility = View.VISIBLE
            } else {
                deleteBtn.visibility = View.INVISIBLE
            }
            page = DEFAULT_PAGE
            timer = Observable.timer(300, TimeUnit.MILLISECONDS)
                    .subscribe { presenter.callSearchApi(s.toString(), page) }
        }
    }


    private fun getHintStr(): String {
        when (presenter.type) {
            ReviewSearchType.UNIVERSITY -> return "대학교를 입력해주세요"
            ReviewSearchType.MAJOR -> return "학과를 입력해주세요"
            ReviewSearchType.SUBJECT, ReviewSearchType.SUBJECT_WITH_RESULT -> return "과목을 입력해주세요"
            ReviewSearchType.PROF_FROM_SUBJ -> return "교수명을 입력해주세요"
            else -> return ""
        }
    }

    override fun setInputStr(searchStr: String) {
        input.setText(searchStr)
        input.setSelection(searchStr.length)
    }

    override fun getRecyclerView(): UnivReviewRecyclerView? {
        return recyclerView
    }

    override fun refresh() {
        setStatus(Status.REFRESHING)
        presenter.callSearchApi(input.text.toString(), DEFAULT_PAGE)
    }

    override fun loadMore() {
        Logger.v("load more")
        Logger.v("page: " + page)
        setStatus(Status.LOADING_MORE)
        presenter.callSearchApi(input.text.toString(), page)
    }


    override fun onPause() {
        super.onPause()
        Util.hideKeyboard(context, input)
    }


    override fun onDestroy() {
        presenter.stopSearch()
        super.onDestroy()
    }
}