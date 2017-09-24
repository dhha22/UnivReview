package com.univreview.view.presenter

import android.content.Context
import android.view.View
import com.univreview.App
import com.univreview.Navigator
import com.univreview.adapter.contract.MyPageAdapterContract
import com.univreview.listener.OnItemClickListener
import com.univreview.log.Logger
import com.univreview.model.UpdateUser
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.model.Setting
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.contract.MyPageContract
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 9. 2..
 */
class MyPagePresenter : MyPageContract, OnItemClickListener {
    private val MY_REVIEW = 0
    private val POINT = 1
    private val USER_IDENTIFY = 2
    private val SETTING = 3

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
            SETTING -> Navigator.goSetting(context)
        }
    }

    override fun callUserProfile() {
        Retro.instance.userService.callUserProfile(App.setHeader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view.setUserData(it.data) }, { ErrorUtils.parseError(it) })
    }

    override fun userProfileUpdate(imagePath: String) {
        Retro.instance.fileService(imagePath, "profile")
                .subscribeOn(Schedulers.io())
                .subscribe({ updateUserInfo(it.data.objKey) }) { Logger.e(it) }
    }

    private fun updateUserInfo(imageUrl: String) {
        Retro.instance.userService.userInfoUpdate(App.setHeader(), UpdateUser(imageUrl))
                .subscribe({ result -> Logger.v(result) }) { Logger.e(it) }
    }
}