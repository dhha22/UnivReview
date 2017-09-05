package com.univreview.adapter.contract

import com.univreview.listener.OnItemClickListener

/**
 * Created by DavidHa on 2017. 9. 1..
 */
interface ReviewListAdapterContract {
    interface View {
        fun setOnItemClickListener(itemClickListener: OnItemClickListener)
        fun setMoreItemClickListener(itemClickListener: OnItemClickListener)
    }

    interface Model : BaseModel
}