package com.univreview.adapter.contract

import com.univreview.model.model_kotlin.AbstractDataProvider

/**
 * Created by DavidHa on 2017. 9. 1..
 */
interface BaseModel {
    fun addItem(item: AbstractDataProvider)
    fun clearItem()
    fun getItem(position: Int): AbstractDataProvider
}