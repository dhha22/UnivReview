package com.univreview.fragment.review

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.univreview.R
import com.univreview.adapter.ReviewCommentAdapter
import com.univreview.dialog.ListDialog
import com.univreview.fragment.BaseFragment
import com.univreview.listener.OnItemClickListener
import com.univreview.log.Logger
import com.univreview.model.model_kotlin.Review
import com.univreview.model.model_kotlin.RvComment
import com.univreview.util.Util
import com.univreview.view.ReviewDetailHeader
import com.univreview.view.contract.ReviewDetailContract
import com.univreview.view.presenter.ReviewDetailPresenter
import kotlinx.android.synthetic.main.fragment_review_detail.*

/**
 * Created by DavidHa on 2017. 8. 8..
 */
class ReviewDetailFragment : BaseFragment(), ReviewDetailContract.View {

    lateinit var adapter: ReviewCommentAdapter
    lateinit var presenter: ReviewDetailPresenter
    lateinit var headerView: ReviewDetailHeader

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
        presenter = ReviewDetailPresenter().apply {
            view = this@ReviewDetailFragment
            review = arguments.getParcelable<Review>("review")
            review.updateNotificationPublisher.subscribe { setHeaderData(it) }
            context = getContext()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_review_detail, container, false)
        toolbar.setBackBtnVisibility(true)
        rootLayout.setBackgroundColor(Util.getColor(context, R.color.backgroundColor))
        rootLayout.addView(view)
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        headerView = ReviewDetailHeader(context).apply {
            setCommentMoreBtnListener { presenter.loadComments() }
            setEtcBtnClickListener(presenter.etcBtnClickListener)

        }
        setHeaderData(presenter.review)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ReviewCommentAdapter(context, headerView)
        recyclerView.adapter = adapter
        commentInput.setSendListener { postReviewComment(commentInput.inputMsg) }
        presenter.adapterModel = adapter
        presenter.adapterView = adapter
        presenter.loadReviewSingle()
        presenter.loadComments()

    }

    private fun setHeaderData(review: Review) {
        headerView.setData(review)
    }


    private fun postReviewComment(message: String?) {
        if (message != null) {
            showProgress()
            recyclerView.smoothScrollToPosition(adapter.itemCount)
            presenter.postComment(RvComment(message))
        } else {
            Util.toast("메세지를 입력해주세요")
        }
    }

    override fun showCommentDeleteDialog(clickListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(context)
                .setMessage("댓글을 삭제하시겠습니까?")
                .setPositiveButton("확인", clickListener)
                .setNegativeButton("취소", null)
                .show()
    }

    override fun setDialog(list: List<String>, itemClickListener: OnItemClickListener) {
        ListDialog(context, list, itemClickListener).show()
    }

    override fun hasMoreComment(hasMore: Boolean) {
        headerView.setCommentMoreBtn(hasMore)
    }

    override fun increaseCommentCnt(isIncrease: Boolean) {
        headerView.increaseCommentCnt(isIncrease)
    }


}