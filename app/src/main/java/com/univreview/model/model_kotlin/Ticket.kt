package com.univreview.model.model_kotlin

/**
 * Created by DavidHa on 2017. 9. 1..
 */
data class Ticket (override var id : Long,
                   override var name: String,
                   val term : Term) : AbstractDataProvider()