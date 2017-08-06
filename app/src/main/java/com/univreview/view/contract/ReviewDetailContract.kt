package com.univreview.view.contract

import android.content.DialogInterface
import com.univreview.model.Review
import com.univreview.model.ReviewComment

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface ReviewDetailContract {
    interface View {
        fun setData(data: Review)
        fun getReviewId(): Long
        fun setPage(page: Int)
        fun showCommentDeleteDialog(clickListener: DialogInterface.OnClickListener)
        fun dismissProgress()
        fun setCommentMoreBtn(hasMore: Boolean)
        fun scrollToPosition(position: Int)
    }

    fun loadReviewSingle()
    fun postComment(body: ReviewComment)
    fun loadCommentItem(page: Int)

}