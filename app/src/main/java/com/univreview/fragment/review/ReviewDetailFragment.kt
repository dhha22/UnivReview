package com.univreview.fragment.review

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dhha22.bindadapter.BindAdapter
import com.dhha22.bindadapter.listener.EndlessScrollListener
import com.dhha22.bindadapter.listener.OnItemClickListener
import com.dhha22.bindadapter.listener.ScrollEndSubscriber
import com.univreview.Navigator
import com.univreview.R
import com.univreview.dialog.ListDialog
import com.univreview.fragment.AbsListFragment
import com.univreview.log.Logger
import com.univreview.model.Review
import com.univreview.util.Util
import com.univreview.view.AbsRecyclerView
import com.univreview.view.CommentItemView
import com.univreview.view.ReviewDetailHeader
import com.univreview.view.contract.ReviewDetailContract
import com.univreview.view.presenter.ReviewDetailPresenter
import kotlinx.android.synthetic.main.fragment_review_detail.*
import kotlinx.android.synthetic.main.review_detail_header.view.*

/**
 * Created by DavidHa on 2017. 8. 8..
 */
class ReviewDetailFragment : AbsListFragment(), ReviewDetailContract.View, ScrollEndSubscriber {

    lateinit var adapter: BindAdapter
    lateinit var presenter: ReviewDetailPresenter
    lateinit var reviewItem: ReviewDetailHeader

    companion object {
        @JvmStatic
        fun getInstance(data: Review): ReviewDetailFragment {
            val fragment = ReviewDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable("review", data)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = BindAdapter(context)
                .addHeaderView(ReviewDetailHeader::class.java)
                .addLayout(CommentItemView::class.java)

        presenter = ReviewDetailPresenter().apply {
            view = this@ReviewDetailFragment
            review = arguments.getParcelable("review")
            review.updateNotificationPublisher.subscribe { reviewItem.data = it }
            adapterModel = adapter
            adapterView = adapter
            adapterView.setOnItemLongClickListener(this)
        }

        reviewItem = (adapter.getHeaderView(0) as ReviewDetailHeader).apply {
            headerView.setEtcBtnClickListener(presenter.etcBtnClickListener)
            data = presenter.review
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_review_detail, container, false)
        toolbar.setWhiteToolbarStyle()
        rootLayout.setBackgroundColor(Util.getColor(context, R.color.backgroundColor))
        rootLayout.addView(view)
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        recyclerView.setAdapter(adapter)
        recyclerView.addOnScrollListener(EndlessScrollListener(this))

        presenter.apply {
            loadReviewSingle()
            commentInput.setSendListener { this.postReviewComment(commentInput.inputMsg) }
        }
    }

    override fun onScrollEnd() {
        lastItemExposed()
    }

    override fun refresh() {
        setStatus(Status.REFRESHING)
        presenter.loadComments(DEFAULT_PAGE)
    }

    override fun loadMore() {
        setStatus(Status.LOADING_MORE)
        presenter.loadComments(page)
    }

    override fun getRecyclerView(): AbsRecyclerView? {
        return recyclerView
    }


    override fun showCommentDeleteDialog(clickListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(context)
                .setMessage("댓글을 삭제하시겠습니까?")
                .setPositiveButton("확인", clickListener)
                .setNegativeButton("취소", null)
                .show()
    }

    override fun setDialog(list: List<String>, itemClickListener: OnItemClickListener) {
        Logger.v("itemClickListener: " + itemClickListener)

        ListDialog(context, list, itemClickListener).show()
    }


    override fun increaseCommentCnt(isIncrease: Boolean) {
        reviewItem.bottomView.increaseCommentCnt(isIncrease)
    }

    override fun goUploadReviewDetail(review: Review) {
        Navigator.goUploadReviewDetail(context, review)
    }


}