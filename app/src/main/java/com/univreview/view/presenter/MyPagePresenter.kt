package com.univreview.view.presenter

import android.content.Context
import android.view.View
import com.univreview.App
import com.univreview.Navigator
import com.univreview.adapter.contract.MyPageAdapterContract
import com.univreview.listener.OnItemClickListener
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.model.model_kotlin.Setting
import com.univreview.view.contract.MyPageContract

/**
 * Created by DavidHa on 2017. 9. 2..
 */
class MyPagePresenter : MyPageContract, OnItemClickListener {
    private val MY_REVIEW = 0
    private val POINT = 1
    private val USER_IDENTIFY = 2

    lateinit var view: MyPageContract.View
    lateinit var adapterModel: MyPageAdapterContract.Model
    var adapterView: MyPageAdapterContract.View? = null
        set(value) {
            value?.setOnItemClickListener(this)
        }
    lateinit var context: Context

    override fun onItemClick(view: View?, position: Int) {
        when (position) {
            MY_REVIEW -> Navigator.goReviewList(context, ReviewSearchType.MY_REVIEW, App.userId, "내 리뷰")
            POINT -> {
                val name = (adapterModel.getItem(POINT) as Setting).previewStr
                val index = name.indexOf("point") - 1
                Navigator.goPointList(context, name.substring(0, index).toInt())
            }
            USER_IDENTIFY -> Navigator.goRegisterUserIdentity(context)
        }
    }
}