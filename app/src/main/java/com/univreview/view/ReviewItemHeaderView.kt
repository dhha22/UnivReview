package com.univreview.view

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.dhha22.bindadapter.ItemView
import com.univreview.R
import com.univreview.model.User
import com.univreview.util.TimeUtil
import com.univreview.util.Util
import kotlinx.android.synthetic.main.review_item_header.view.*


/**
 * Created by DavidHa on 2017. 9. 23..
 */
class ReviewItemHeaderView(context: Context, attributeSet: AttributeSet? = null) : ItemView(context, attributeSet) {
    init {
        LayoutInflater.from(context).inflate(R.layout.review_item_header, this, true)
    }

    fun setUserData(user: User) {
        nameTxt.text = user.name
        if (user.authenticated) {
            authMark.visibility = View.VISIBLE
        } else {
            authMark.visibility = View.GONE
        }
    }

    fun setTimeTxt(createdAt: String) {
        timeTxt.text = TimeUtil().getPointFormat(createdAt)
    }

    fun isUserLayoutVisible(isVisible: Boolean) {
        if (isVisible) {
            userLayout.visibility = View.VISIBLE
        } else {
            userLayout.visibility = View.GONE
        }
    }

    fun isEtcBtnVisible(isVisible: Boolean) {
        if (isVisible) {
            etcBtn.visibility = View.VISIBLE
        } else {
            etcBtn.visibility = View.GONE
        }
    }

    // subject professor 이름 설정
    fun setSubjectProfessor(subjectName: String, professorName: String) {
        val builder = SpannableStringBuilder()
        var index = 0
        builder.append(subjectName + " ")
        Util.addSizeSpan(builder, index, Util.dpToPx(context, 16))
        Util.addColorSpan(context, builder, index, R.color.colorPrimary)
        index = builder.length
        builder.append(professorName + " 교수님")
        Util.addSizeSpan(builder, index, Util.dpToPx(context, 14))
        Util.addColorSpan(context, builder, index, R.color.professorTxtColor)
        subjectProfessorTxt.text = builder
    }

    fun setEtcBtnClickListener(clickListener: OnClickListener) {
        etcBtn.setOnClickListener(clickListener)
    }

    fun setEtcBtnClickListener(click: () -> Unit) {
        etcBtn.setOnClickListener { click.invoke() }
    }
}