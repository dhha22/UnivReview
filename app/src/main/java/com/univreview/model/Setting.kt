package com.univreview.model

import com.dhha22.bindadapter.Item

/**
 * Created by DavidHa on 2017. 8. 20..
 */
data class Setting(val id: Long,
                   val name: String,
                   var previewStr: String = "") : Item {
    constructor(id: Long, name: String) : this(id, name, "")
}