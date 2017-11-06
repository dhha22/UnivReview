package com.univreview.adapter.contract

import com.dhha22.bindadapter.BindAdapterContract
import com.dhha22.bindadapter.listener.OnItemClickListener

/**
 * Created by DavidHa on 2017. 9. 1..
 */
interface ReviewListAdapterContract {
    interface View {
        fun setOnItemClickListener(itemClickListener: OnItemClickListener)
        fun setMoreItemClickListener(itemClickListener: OnItemClickListener)
    }

    interface Model : BindAdapterContract.Model
}