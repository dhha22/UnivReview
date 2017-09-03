package com.univreview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.univreview.adapter.contract.MyPageAdapterContract
import com.univreview.model.model_kotlin.Setting
import com.univreview.view.SettingItemView

/**
 * Created by DavidHa on 2017. 9. 2..
 */
class MyPageAdapter(context: Context) : CustomAdapter(context),
        MyPageAdapterContract.View, MyPageAdapterContract.Model {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(SettingItemView(context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as ViewHolder).v.setTitle(list[position].name)
        holder.v.setPreviewTxt((list[position] as Setting).previewStr)
    }


    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val v: SettingItemView by lazy { view as SettingItemView }

        init {
            v.setOnClickListener { v -> itemClickListener.onItemClick(v, adapterPosition) }
        }
    }
}