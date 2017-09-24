package com.univreview.model

/**
 * Created by DavidHa on 2017. 8. 28..
 */
data class DataModel<out T>(val data : T) : ResultModel()