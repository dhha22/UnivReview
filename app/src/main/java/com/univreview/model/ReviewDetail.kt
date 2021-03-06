package com.univreview.model

/**
 * Created by DavidHa on 2017. 8. 27..
 */
data class ReviewDetail(var content: String) {
    fun getAlertMessage(): String? {
        if (content.length < 10) {
            return "최소 10글자 이상 적어주시길 바랍니다."
        } else {
            return null
        }
    }
}