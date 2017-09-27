package com.univreview.model

import com.univreview.BuildConfig
import com.univreview.network.Retro

/**
 * Created by DavidHa on 2017. 9. 24..
 */
data class UpdateUser(val name: String? = null,
                      var profileImageUrl: String? = null) {
    init {
        if (profileImageUrl != null) {
            profileImageUrl = if (BuildConfig.DEBUG) {
                Retro.IMAGE_URL_DEV + profileImageUrl
            } else {
                Retro.IMAGE_URL_PROD + profileImageUrl
            }
        }
    }
}