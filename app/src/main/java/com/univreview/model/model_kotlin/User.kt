package com.univreview.model.model_kotlin

/**
 * Created by DavidHa on 2017. 8. 19..
 */
data class User(val provider: String,
                val uid: Long,
                val name: String,
                val email: String,
                val accessToken: String,
                val client: String,
                val universityId: Long)