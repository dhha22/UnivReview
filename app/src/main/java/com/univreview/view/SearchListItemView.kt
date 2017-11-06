package com.univreview.view

import android.content.Context
import com.dhha22.bindadapter.Item
import com.dhha22.bindadapter.ItemView
import com.univreview.R
import com.univreview.model.SearchResult
import kotlinx.android.synthetic.main.search_list_item.view.*

/**
 * Created by DavidHa on 2017. 11. 6..
 */
class SearchListItemView(context: Context) : ItemView(context) {
    init {
        setContentView(R.layout.search_list_item)
        setFullSpan()
    }

    override fun setData(data: Item?) {
        super.setData(data)
        if(data is SearchResult) {
            nameTxt.text = data.name
        }
    }
}