package com.univreview.view.contract

import android.content.DialogInterface
import com.dhha22.bindadapter.listener.OnItemClickListener
import com.univreview.fragment.AbsListFragment
import com.univreview.model.Review
import com.univreview.view.AbsRecyclerView

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface ReviewDetailContract {
    interface View : BaseView {
        fun showCommentDeleteDialog(clickListener: DialogInterface.OnClickListener)
        fun setDialog(list: List<String>, itemClickListener: OnItemClickListener)
        fun increaseCommentCnt(isIncrease : Boolean)
        fun getRecyclerView(): AbsRecyclerView?
        fun setResult(page: Int)
        fun setStatus(status: AbsListFragment.Status)
        fun goUploadReviewDetail(review : Review)
    }

    fun loadReviewSingle()
    fun postReviewComment(message:String?)
    fun loadComments(page : Int)
    val etcBtnClickListener: android.view.View.OnClickListener

}