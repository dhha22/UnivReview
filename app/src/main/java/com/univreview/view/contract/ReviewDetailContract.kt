package com.univreview.view.contract

import android.content.DialogInterface
import com.univreview.dialog.ListDialog
import com.univreview.model.Review
import com.univreview.model.ReviewComment

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface ReviewDetailContract {
    interface View {
        fun setHeaderData(data: Review)
        fun showCommentDeleteDialog(clickListener: DialogInterface.OnClickListener)
        fun dismissProgress()
        fun setCommentMoreBtn(hasMore: Boolean)
    }

    fun loadReviewSingle()
    fun postComment(body: ReviewComment)
    fun loadCommentItem()

}