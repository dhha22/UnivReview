package com.univreview.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.univreview.R
import com.univreview.model.model_kotlin.RvPoint
import com.univreview.util.TimeUtil
import com.univreview.util.Util
import kotlinx.android.synthetic.main.point_item.view.*

/**
 * Created by DavidHa on 2017. 8. 30..
 */
class PointItemView(context: Context?) : FrameLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.point_item, this, true)
    }

    fun setData(data : RvPoint){
        val builder = StringBuilder(String.format(resources.getString(R.string.point), data.point))
        if(data.point > 0){     // point 증가
            builder.insert(0, "+")
            pointTxt.setTextColor(Util.getColor(context, R.color.colorPrimary))
        }else{      // point 감소
            pointTxt.setTextColor(Util.getColor(context, R.color.pointSubtractColor))
        }
        pointTxt.text = builder.toString()
        messageTxt.text = data.message
        timeTxt.text = TimeUtil().getPointFormat(data.createdAt)
    }
}