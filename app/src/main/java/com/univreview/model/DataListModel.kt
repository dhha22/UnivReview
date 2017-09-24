package com.univreview.model

/**
 * Created by DavidHa on 2017. 8. 20..
 */
data class DataListModel<out T>(val data : List<T>) : ResultModel()