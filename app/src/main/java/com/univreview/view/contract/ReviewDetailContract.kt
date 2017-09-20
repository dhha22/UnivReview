package com.univreview.view.contract

import android.content.DialogInterface
import com.univreview.fragment.AbsListFragment
import com.univreview.listener.OnItemClickListener
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
    }

    fun loadReviewSingle()
    fun postReviewComment(message:String?)
    fun loadComments(page : Int)
    val etcBtnClickListener: android.view.View.OnClickListener

}