package com.univreview.model.model_kotlin

import kotlin.properties.Delegates

/**
 * Created by DavidHa on 2017. 8. 20..
 */
data class Setting(override var id: Long, override var name: String) : AbstractDataProvider() {
     lateinit var previewStr : String
}