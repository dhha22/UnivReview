package com.univreview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.univreview.adapter.contract.SearchAdapterContract
import com.univreview.view.SearchListItemView

/**
 * Created by DavidHa on 2017. 8. 23..
 */
class SearchAdapter(context : Context) : CustomAdapter(context), SearchAdapterContract.Model {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(SearchListItemView(context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as ViewHolder).v.setText(list[position].name)
    }

    private inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val v : SearchListItemView by lazy { itemView as SearchListItemView }
        init {
            v.setOnClickListener { itemClickListener.onItemClick(v, adapterPosition) }
        }
    }
}