package com.univreview.view

import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import com.dhha22.bindadapter.ItemView
import com.univreview.R
import com.univreview.util.Util
import kotlinx.android.synthetic.main.toolbar.view.*

/**
 * Created by DavidHa on 2017. 11. 14..
 */
class Toolbar(context: Context, attributeSet: AttributeSet? = null) : ItemView(context, attributeSet) {
    init {
        setContentView(R.layout.toolbar)
    }

    fun setToolbarBackgroundColor(colorId: Int) {
        toolbar.setBackgroundColor(resources.getColor(colorId))
    }


    fun setOnRightMenuListener(click: () -> Unit) {
        rightMenu.setOnClickListener { click.invoke() }
        rightMenu.visibility = VISIBLE
    }

    fun setRightMenuText(menuStr: String) {
        rightMenu.text = menuStr
    }


    fun setWhiteToolbarStyle() {
        setBackBtnVisibility(true)
        toolbar.setBackgroundColor(Util.getColor(context, R.color.white))
        setTitleColor(R.color.black)
        ViewCompat.setBackgroundTintList(backBtn, ContextCompat.getColorStateList(context, R.color.black))
        backBtn.setOnClickListener { (context as Activity).finish() }
    }

    fun setSearchToolbarStyle() {
        setBackBtnVisibility(true)
        line.visibility = GONE
    }

    fun setCancelToolbarStyle() {
        setCancelBtnVisibility(true)
        toolbar.setBackgroundColor(Util.getColor(context, R.color.white))
        setTitleColor(R.color.black)
        ViewCompat.setBackgroundTintList(cancelBtn, ContextCompat.getColorStateList(context, R.color.black))
        cancelBtn.setOnClickListener { (context as Activity).onBackPressed() }
        setTitleColor(R.color.black)
    }

    fun setTitleTxt(title: String) {
        if (title.length > 10) titleTxt.textSize = 14f
        titleTxt.text = title
    }

    fun setTitleColor(id: Int) {
        titleTxt.setTextColor(Util.getColor(context, id))
    }

    fun setBackBtnVisibility(isVisible: Boolean) {
        if (isVisible) {
            backBtn.visibility = View.VISIBLE
            backBtn.setOnClickListener { (context as Activity).finish() }
        } else {
            backBtn.visibility = View.INVISIBLE
        }
    }

    fun setCancelBtnVisibility(isVisible: Boolean) {
        if (isVisible) {
            cancelBtn.visibility = View.VISIBLE
            cancelBtn.setOnClickListener { (context as Activity).onBackPressed() }
        } else {
            cancelBtn.visibility = View.INVISIBLE
        }
    }
}