package com.univreview.view

import android.content.Context
import com.dhha22.bindadapter.Item
import com.dhha22.bindadapter.ItemView
import com.univreview.R
import com.univreview.model.RvComment
import com.univreview.util.TimeUtil
import kotlinx.android.synthetic.main.comment_item.view.*

/**
 * Created by DavidHa on 2017. 11. 6..
 */
class CommentItemView(context: Context) : ItemView(context) {
    init {
        setContentView(R.layout.comment_item)
        setFullSpan()
    }

    override fun setData(data: Item?) {
        super.setData(data)
        if (data is RvComment) {
            nameTxt.text = data.name
            messageTxt.text = data.content
            timeTxt.text = TimeUtil().getPointFormat(data.createdAt)
        }
    }
}