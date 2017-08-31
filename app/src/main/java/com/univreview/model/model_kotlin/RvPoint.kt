package com.univreview.model.model_kotlin

/**
 * Created by DavidHa on 2017. 8. 28..
 */
data class RvPoint(override var id: Long,
                   val point: Int,
                   val message: String,
                   val createdAt : String) : AbstractDataProvider() {
    override var name: String
        get() =  ""
        set(value) {}
}