package com.univreview.view.contract

import android.content.DialogInterface
import com.univreview.listener.OnItemClickListener
import com.univreview.model.model_kotlin.Review
import com.univreview.model.model_kotlin.RvComment

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface ReviewDetailContract {
    interface View : BaseView {
        fun setHeaderData(data: Review)
        fun showCommentDeleteDialog(clickListener: DialogInterface.OnClickListener)
        fun hasMoreComment(hasMore: Boolean)
        fun setDialog(list: List<String>, itemClickListener: OnItemClickListener)
    }

    fun loadReviewSingle()
    fun postComment(body: RvComment)
    fun loadComments()
    val etcBtnClickListener: android.view.View.OnClickListener

}