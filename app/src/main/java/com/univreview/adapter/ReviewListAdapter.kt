package com.univreview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.dhha22.bindadapter.AbsAdapter
import com.dhha22.bindadapter.Item
import com.dhha22.bindadapter.ItemView
import com.dhha22.bindadapter.listener.OnItemClickListener
import com.univreview.adapter.contract.ReviewListAdapterContract
import com.univreview.log.Logger
import com.univreview.model.Review
import com.univreview.model.enumeration.ReviewType
import com.univreview.view.ReviewItemView
import kotlinx.android.synthetic.main.review_detail_header.view.*

/**
 * Created by DavidHa on 2017. 8. 7..
 */
class ReviewListAdapter(val context: Context, val type: ReviewType) : AbsAdapter(),
        ReviewListAdapterContract.Model, ReviewListAdapterContract.View {
    lateinit var moreBtnClickListener: OnItemClickListener
    lateinit var itemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder
            = ReviewHolder(ReviewItemView(context))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as ReviewHolder).v.setData(items[position], type)
    }

    override fun getItemCount(): Int = items.size


    override fun addItem(item: Item) {
        val position = itemCount
        (item as Review).updateNotificationPublisher.subscribe {
            setItem(position, it)
            Logger.v("subscribe item: " + (getItem(position) as Review).updateNotificationPublisher)
            notifyItemChanged(position)
        }
        items.add(item)
    }

    override fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun setMoreItemClickListener(itemClickListener: OnItemClickListener) {
        moreBtnClickListener = itemClickListener
    }


    inner class ReviewHolder(itemView: ItemView) : RecyclerView.ViewHolder(itemView) {
        val v: ReviewItemView by lazy { itemView as ReviewItemView }

        init {
            v.setOnClickListener { itemClickListener.onItemClick(it, adapterPosition) }
            v.headerView.setEtcBtnClickListener { moreBtnClickListener.onItemClick(v.headerView, adapterPosition) }
        }
    }

}