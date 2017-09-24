package com.univreview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.univreview.model.Setting
import com.univreview.view.SettingItemView

/**
 * Created by DavidHa on 2017. 9. 20..
 */
class SettingAdapter(context: Context) : CustomAdapter(context) {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(SettingItemView(context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position == 0) {
            (holder as ViewHolder).v.setNextImgVisibility(false)
        }
        (holder as ViewHolder).v.setTitle(list[position].name)
        holder.v.setPreviewTxt((list[position] as Setting).previewStr)
    }

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val v: SettingItemView by lazy { itemView as SettingItemView }

        init {
            v.setOnClickListener { itemClickListener.onItemClick(it, adapterPosition) }
        }
    }
}