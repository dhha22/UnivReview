package com.univreview.model.model_kotlin

import com.google.gson.annotations.SerializedName

/**
 * Created by DavidHa on 2017. 9. 1..
 */
data class Term(@SerializedName("started_at") val startedAt: String,
                @SerializedName("ended_at") val endedAt: String)