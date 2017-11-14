package com.univreview.view

import android.content.Context
import android.util.AttributeSet
import com.dhha22.bindadapter.ItemView
import com.univreview.R
import com.univreview.util.Util
import kotlinx.android.synthetic.main.comment_input.view.*

/**
 * Created by DavidHa on 2017. 11. 14..
 */
class CommentInput(context: Context, attrs: AttributeSet? = null) : ItemView(context, attrs) {
    init {
        setContentView(R.layout.comment_input)
        setFullSpan()
    }

    val inputMsg: String?
        get() {
            val inputStr = input.text.toString()
            input.text = null
            Util.hideKeyboard(context, input)
            return if (inputStr.isNotEmpty()) inputStr else null
        }


    fun setSendListener(click: () -> Unit) {
        sendBtn.setOnClickListener { click.invoke() }
    }
}