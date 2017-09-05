package com.univreview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.univreview.adapter.contract.ReviewListAdapterContract
import com.univreview.listener.OnItemClickListener
import com.univreview.log.Logger
import com.univreview.model.model_kotlin.Review
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.view.ReviewItemView

/**
 * Created by DavidHa on 2017. 8. 7..
 */
class ReviewListAdapter(context: Context, val type: ReviewSearchType, headerView: View? = null)
    : CustomAdapter(context, headerView), ReviewListAdapterContract.Model, ReviewListAdapterContract.View {
    lateinit var moreBtnClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        Logger.v("on Create View Holder")
        if (viewType == CONTENT) {
            return ViewHolder(ReviewItemView(context))
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        Logger.v("on Bind View Holder")
        if (getItemViewType(position) == CONTENT) {
            (holder as ViewHolder).v.setData(list[position] as Review)
        }
    }

    override fun getItemCount(): Int {
        if (type != ReviewSearchType.MY_REVIEW) {
            return list.size + HEADER
        }
        return super.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        // My Review에는 Header가 존재하지 않음
        if (type != ReviewSearchType.MY_REVIEW && position == 0) {
            return HEADER
        }
        return super.getItemViewType(position)
    }

    override fun setMoreItemClickListener(itemClickListener: OnItemClickListener) {
        moreBtnClickListener = itemClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val v: ReviewItemView by lazy { itemView as ReviewItemView }

        init {
            if (type == ReviewSearchType.MY_REVIEW) {
                v.setMode(ReviewItemView.Status.MY_REVIEW)
            } else if (type == ReviewSearchType.SUBJECT || type == ReviewSearchType.PROFESSOR) {
                v.setMode(ReviewItemView.Status.READ_REVIEW)
            }
            v.setOnClickListener { itemClickListener.onItemClick(it, adapterPosition) }
            v.moreBtnSetOnClickListener { moreBtnClickListener.onItemClick(it, adapterPosition) }
        }
    }
}