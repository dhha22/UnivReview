package com.univreview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.univreview.model.model_kotlin.RvPoint
import com.univreview.view.PointItemView

/**
 * Created by DavidHa on 2017. 8. 30..
 */
class PointAdapter(context: Context, headerView: View) : CustomAdapter(context, headerView) {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == CONTENT) {
            return ViewHolder(PointItemView(context))
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == CONTENT) {
            (holder as ViewHolder).v.setData(list[position - 1] as RvPoint)
        }
    }

    private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val v: PointItemView by lazy { view as PointItemView }
    }
}