package com.univreview.adapter.contract

import com.dhha22.bindadapter.BindAdapterContract
import com.univreview.listener.OnItemClickListener
import com.univreview.model.Review

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