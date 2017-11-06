package com.univreview.model

import com.dhha22.bindadapter.Item

/**
 * Created by DavidHa on 2017. 9. 13..
 */
data class RvComment(var id: Long,
                     var name: String,
                     var userId: Long,
                     var content: String,
                     var createdAt: String) : Item {
    constructor(content: String) : this(0, "", 0, content, "")
}