package com.univreview.adapter.contract

import com.univreview.listener.OnItemClickListener
import com.univreview.listener.OnItemLongClickListener

/**
 * Created by DavidHa on 2017. 9. 2..
 */
interface BaseView {
    fun setOnItemLongClickListener(clickListener: OnItemLongClickListener)
    fun setOnItemClickListener(clickListener: OnItemClickListener)
}