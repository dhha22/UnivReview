package com.univreview.model

/**
 * Created by DavidHa on 2017. 8. 19..
 */
open class ResultModel(val success: Boolean = false,
                       val error: Error? = null,
                       val pagination: Pagination? = null)