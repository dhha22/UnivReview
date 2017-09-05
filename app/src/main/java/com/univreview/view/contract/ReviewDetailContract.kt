package com.univreview.view.contract

import android.content.DialogInterface
import android.view.View
import com.univreview.listener.OnItemClickListener
import com.univreview.model.ReviewComment
import com.univreview.model.model_kotlin.Review

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface ReviewDetailContract {
    interface View : BaseView {
        fun setHeaderData(data: Review)
        fun showCommentDeleteDialog(clickListener: DialogInterface.OnClickListener)
        fun setCommentMoreBtn(hasMore: Boolean)
        fun setDialog(list: List<String>, itemClickListener: OnItemClickListener)
    }

    fun loadReviewSingle()
    fun postComment(body: ReviewComment)
    fun loadCommentItem()
    val etcBtnClickListener: android.view.View.OnClickListener

}