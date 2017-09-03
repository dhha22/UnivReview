package com.univreview.adapter.contract

import com.univreview.listener.OnItemLongClickListener
import com.univreview.model.model_kotlin.AbstractDataProvider

/**
 * Created by DavidHa on 2017. 9. 1..
 */
interface ReviewDetailAdapterContract {
    interface View :BaseView

    interface Model : BaseModel {
        fun addLastItem(item : AbstractDataProvider)
        fun removeItem(position: Int)
    }
}