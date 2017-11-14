package com.univreview.model

import com.dhha22.bindadapter.Item

/**
 * Created by DavidHa on 2017. 11. 6..
 */

data class SearchResult(val id: Long, val name: String, var professor: Professor? = null) : Item
// Major, Subject, Professor, Course