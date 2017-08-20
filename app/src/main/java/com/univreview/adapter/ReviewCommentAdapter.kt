package com.univreview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.univreview.adapter.contract.ReviewDetailAdapterContract
import com.univreview.model.model_kotlin.AbstractDataProvider
import com.univreview.model.ReviewComment
import com.univreview.view.CommentItemView

/**
 * Created by DavidHa on 2017. 8. 8..
 */
class ReviewCommentAdapter(context: Context, headerView: View)
    : CustomAdapter(context, headerView), ReviewDetailAdapterContract.Model, ReviewDetailAdapterContract.View {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == CONTENT){
            return ViewHolder(CommentItemView(context))
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == CONTENT) {
            (holder as ViewHolder).v.setData(list[position - 1] as ReviewComment)
        }
    }

    override fun addItem(item: AbstractDataProvider?) {
        list.add(0, item)
        notifyDataSetChanged()
    }

    override fun removeItem(position: Int) {
        list.removeAt(position - 1)
        notifyDataSetChanged()
    }

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val v: CommentItemView by lazy { itemView as CommentItemView }

        init {
            v.setOnLongClickListener { itemLongClickListener.onLongClick(it, adapterPosition) }
        }

    }
}