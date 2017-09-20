package com.univreview.view

import android.content.Context
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.univreview.R
import com.univreview.model.model_kotlin.Ticket
import com.univreview.util.TimeUtil
import kotlinx.android.synthetic.main.point_list_header.view.*
import java.lang.StringBuilder

/**
 * Created by DavidHa on 2017. 9. 1..
 */
class PointListHeaderView(context: Context) : CardView(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.point_list_header, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setPoint(totalPoint : Int){
        totalPointTxt.text = String.format(resources.getString(R.string.point, totalPoint))
    }

    fun setBuyTicketListener(clickListener: OnClickListener){
        buyTicketBtn.setOnClickListener(clickListener)
    }

    fun setUserTicket(ticket: Ticket) {
            ticket.let {
                val timeUtil = TimeUtil()
                buyTicketBtn.visibility = View.GONE
                ticketLayout.visibility = View.VISIBLE
                ticketNameTxt.text = it.name
                ticketTimeTxt.text = StringBuilder(timeUtil.getPointFormat(it.term.startedAt)
                        + "~" + timeUtil.getPointFormat(it.term.endedAt))
            }

    }

}