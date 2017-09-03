package com.univreview.model.model_kotlin

/**
 * Created by DavidHa on 2017. 8. 20..
 */
data class Setting(override var id: Long,
                   override var name: String,
                   var previewStr: String = "") : AbstractDataProvider() {
    constructor(id: Long, name: String) : this(id, name, "")
}