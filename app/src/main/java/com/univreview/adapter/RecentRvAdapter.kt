package com.univreview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.univreview.model.AbstractDataProvider
import com.univreview.model.Review
import com.univreview.view.RecentReviewItemView

/**
 * Created by DavidHa on 2017. 8. 28..
 */
class RecentRvAdapter(context: Context) : CustomAdapter(context) {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(RecentReviewItemView(context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as ViewHolder).v.setData(list[position] as Review)
    }

    override fun addItem(item: AbstractDataProvider?) {
        list.add(item)
        notifyDataSetChanged()
    }

    private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val v: RecentReviewItemView by lazy { view as RecentReviewItemView }

    }
}