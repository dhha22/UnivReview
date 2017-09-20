package com.univreview.fragment.review

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.univreview.App
import com.univreview.R
import com.univreview.adapter.ReviewListAdapter
import com.univreview.dialog.ListDialog
import com.univreview.fragment.AbsListFragment
import com.univreview.listener.EndlessRecyclerViewScrollListener
import com.univreview.listener.OnItemClickListener
import com.univreview.log.Logger
import com.univreview.model.RandomImageModel
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.model.enumeration.ReviewType
import com.univreview.util.AnimationUtils
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
class ReviewListFragment : AbsListFragment(), ReviewListContract.View {
    lateinit var adapter: ReviewListAdapter
    lateinit var type: ReviewSearchType
    lateinit var name: String
    lateinit var presenter: ReviewListPresenter
    val randomImageModel = RandomImageModel()

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
                adapter = ReviewListAdapter(context, ReviewType.MY_REVIEW)
                appBarLayout.visibility = View.GONE
                toolbar.setBackBtnVisibility(true)
                toolbar.setTitleTxt(name)
            }

        // Subject List
            ReviewSearchType.SUBJECT -> {
                adapter = ReviewListAdapter(context, ReviewType.READ_REVIEW)
                App.picasso.load(randomImageModel.imageURL).fit().centerCrop().into(toolbar_image)
                toolbar.visibility = View.GONE
                appBarLayout.addOnOffsetChangedListener { appBarLayout, _ ->
                    val height = appBarLayout.height - appBarLayout.bottom
                    val value = appBarLayout.bottom.toFloat() / appBarLayout.height
                    AnimationUtils.setScale(reviewTitleTxt, value)
                    if (height == 0) {
                        AnimationUtils.fadeOut(context, toolbar_title_layout)
                        AnimationUtils.fadeIn(context, reviewTitleTxt)
                        AnimationUtils.fadeIn(context, filterLayout)
                    } else if (height >= innerToolbar.height * 1.02) {
                        AnimationUtils.fadeIn(context, toolbar_title_layout)
                        AnimationUtils.fadeOut(context, reviewTitleTxt)
                    } else {
                        AnimationUtils.fadeOut(context, toolbar_title_layout)
                        AnimationUtils.fadeIn(context, reviewTitleTxt)
                        AnimationUtils.fadeOut(context, filterLayout)
                    }
                }

                toolbar_back_btn.setOnClickListener { activity.onBackPressed() }
                reviewTitleTxt.text = name
                toolbar_title_txt.text = name
                toolbar_subtitle_txt.text = "전체"
                filterLayout.setOnClickListener { presenter.loadFilterList() }
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
        recyclerView.addItemDecoration(SimpleDividerItemDecoration(context))
        presenter.let {
            it.adapterModel = adapter
            it.adapterView = adapter
        }
        recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onScrolled(view: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                if (lastVisibleItemPosition == totalItemCount - 1) {
                    lastItemExposed()
                }
            }
        })
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
        filter_name_txt.text = filterName
        toolbar_subtitle_txt.text = filterName
    }

    override fun setDialog(list: List<String>, itemClickListener: OnItemClickListener) {
        ListDialog(context, list, itemClickListener).show()
    }
}