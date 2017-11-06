package com.univreview.model

import com.dhha22.bindadapter.Item

/**
 * Created by DavidHa on 2017. 8. 28..
 */
data class RvPoint(var id: Long,
                   val point: Int,
                   val message: String,
                   val createdAt : String) : Item {

}