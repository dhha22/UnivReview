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
import com.univreview.model.model_kotlin.AbstractDataProvider
import com.univreview.view.ReviewItemView
import rx.subjects.PublishSubject

/**
 * Created by DavidHa on 2017. 8. 7..
 */
class ReviewListAdapter(context: Context, val type: ReviewSearchType) : CustomAdapter(context),
        ReviewListAdapterContract.Model, ReviewListAdapterContract.View {
    lateinit var moreBtnClickListener: OnItemClickListener


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        Logger.v("on Create View Holder")
        return ViewHolder(ReviewItemView(context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        Logger.v("on Bind View Holder")
        (holder as ViewHolder).v.setData(list[position] as Review)
    }

    override fun addItem(item: AbstractDataProvider) {
        val position = itemCount
        (item as Review).updateNotificationPublisher.subscribe {
            list[position] = it
            Logger.v("subscribe item: "+(list[position] as Review).updateNotificationPublisher)
            notifyItemChanged(position)
        }
        super.addItem(item)
    }

    override fun getPosition(position: Int): Int {
        val pos: Int
        if (headerView == null) {
            pos = position
        } else {
            pos = position - 1
        }
        return pos
    }

    /*override fun getItemCount(): Int {
        if (type != ReviewSearchType.MY_REVIEW) {
            return list.size + HEADER
        }
        return super.getItemCount()
    }*/

    /*override fun getItemViewType(position: Int): Int {
        // My Review에는 Header가 존재하지 않음
        if (type != ReviewSearchType.MY_REVIEW && position == 0) {
            return HEADER
        }
        return super.getItemViewType(position)
    }
*/
    override fun setMoreItemClickListener(itemClickListener: OnItemClickListener) {
        moreBtnClickListener = itemClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val v: ReviewItemView by lazy { itemView as ReviewItemView }

        init {
            if (type == ReviewSearchType.MY_REVIEW) {
                v.setMode(ReviewItemView.Status.MY_REVIEW)
            } else if (type == ReviewSearchType.SUBJECT) {
                v.setMode(ReviewItemView.Status.READ_REVIEW)
            }
            v.setOnClickListener { itemClickListener.onItemClick(it, adapterPosition) }
            v.moreBtnSetOnClickListener { moreBtnClickListener.onItemClick(it, adapterPosition) }
        }
    }
}