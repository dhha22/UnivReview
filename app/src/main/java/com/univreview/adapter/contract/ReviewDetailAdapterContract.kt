package com.univreview.adapter.contract

import com.univreview.model.AbstractDataProvider

/**
 * Created by DavidHa on 2017. 9. 1..
 */
interface ReviewDetailAdapterContract {
    interface View :BaseView

    interface Model : BaseModel {
        fun addFirstItem(item : AbstractDataProvider)
        fun removeItem(position: Int)
    }
}