package com.univreview.model

/**
 * Created by DavidHa on 2017. 9. 1..
 */
data class Ticket(var id: Long,
                  var name: String,
                  val term: Term? = null)