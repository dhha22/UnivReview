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
import com.univreview.fragment.AbsListFragment
import com.univreview.listener.EndlessRecyclerViewScrollListener
import com.univreview.log.Logger
import com.univreview.model.RandomImageModel
import com.univreview.model.Review
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.util.AnimationUtils
import com.univreview.util.Util
import com.univreview.view.AbsRecyclerView
import com.univreview.view.ReviewTotalScoreView
import com.univreview.view.UnivReviewRecyclerView
import com.univreview.view.contract.ReviewListContract
import com.univreview.view.presenter.ReviewListPresenter
import com.univreview.widget.PreCachingLayoutManager
import kotlinx.android.synthetic.main.fragment_review_list.*

/**
 * Created by DavidHa on 2017. 8. 7..
 */
class ReviewListFragment : AbsListFragment(), ReviewListContract.View {
    lateinit var headerView: ReviewTotalScoreView
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
        val id = arguments.getLong("id")
        name = arguments.getString("name")
        presenter = ReviewListPresenter()
        presenter.apply {
            view = this@ReviewListFragment
            when (type) {
                ReviewSearchType.SUBJECT -> subjectId = id
                ReviewSearchType.PROFESSOR -> professorId = id
            }
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
        App.picasso.load(randomImageModel.imageURL).fit().centerCrop().into(toolbar_image)
        when (type) {
            ReviewSearchType.MY_REVIEW -> {
                adapter = ReviewListAdapter(context, type)
                smooth_app_bar_layout.visibility = View.GONE
                toolbar.setBackBtnVisibility(true)
                toolbar.setTitleTxt(name)
            }

            ReviewSearchType.SUBJECT, ReviewSearchType.PROFESSOR -> {
                headerView = ReviewTotalScoreView(context)
                adapter = ReviewListAdapter(context, type, headerView)
                toolbar.visibility = View.GONE
                list.setPadding(0, Util.dpToPx(context, 38), 0, 0)
                smooth_app_bar_layout.addOnOffsetChangedListener { appBarLayout, _ ->
                    val height = appBarLayout.height - appBarLayout.bottom
                    val value = appBarLayout.bottom.toFloat() / appBarLayout.height
                    AnimationUtils.setScale(title_txt, value)
                    if (height == 0) {
                        AnimationUtils.fadeOut(context, toolbar_title_layout)
                        AnimationUtils.fadeIn(context, title_txt)
                        AnimationUtils.fadeIn(context, filter_layout)
                    } else if (height >= innerToolbar.height * 1.02) {
                        AnimationUtils.fadeIn(context, toolbar_title_layout)
                        AnimationUtils.fadeOut(context, title_txt)
                    } else {
                        AnimationUtils.fadeOut(context, toolbar_title_layout)
                        AnimationUtils.fadeIn(context, title_txt)
                        AnimationUtils.fadeOut(context, filter_layout)
                    }
                }
                title_txt.text = name
                toolbar_back_btn.setOnClickListener { activity.onBackPressed() }
                toolbar_title_txt.text = name
                toolbar_subtitle_txt.text = "전체"
            }
        }
        setRecyclerView()
    }

    private fun setRecyclerView() {
        list.setMode(UnivReviewRecyclerView.Mode.DISABLED)
        list.setBackgroundColor(Util.getColor(context, R.color.backgroundColor))
        val layoutManager = PreCachingLayoutManager(context)
        layoutManager.setExtraLayoutSpace(App.SCREEN_HEIGHT)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        list.setLayoutManager(layoutManager)
        list.setAdapter(adapter)
        presenter.adapterModel = adapter
        presenter.adapterView = adapter
        list.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onScrolled(view: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                if (lastVisibleItemPosition == totalItemCount - 1) {
                    lastItemExposed()
                }
            }
        })
    }

    override fun getRecyclerView(): AbsRecyclerView? {
        return list
    }

    override fun refresh() {
        Logger.v("refresh")
        setStatus(AbsListFragment.Status.REFRESHING)
        presenter.loadReviewItem(type, DEFAULT_PAGE)
    }

    override fun loadMore() {
        setStatus(AbsListFragment.Status.LOADING_MORE)
        Logger.v("page: " + page)
        presenter.loadReviewItem(type, page)
    }

    override fun setFilterName(filterName: String) {
        filter_name_txt.text = filterName
        toolbar_subtitle_txt.text = filterName
    }

    override fun setDialog(list: List<String>) {

    }

    override fun setHeaderData(rate: Float, review: Review) {
        headerView.setData(rate, review)
    }

    override fun setHeaderViewVisibility(isVisibility: Boolean) {
        if (isVisibility) {
            headerView.visibility = View.VISIBLE
        } else {
            headerView.visibility = View.GONE
        }
    }
}