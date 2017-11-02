package com.univreview.view.presenter

import android.app.Activity
import android.content.Context
import com.univreview.App
import com.univreview.log.Logger
import com.univreview.model.UpdateUser
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.util.ImageUtil
import com.univreview.view.contract.ProfileEditContract
import okhttp3.MultipartBody
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 9. 25..
 */
class ProfileEditPresenter : ProfileEditContract {
    lateinit var view: ProfileEditContract.View
    var imagePath: String? = null
    lateinit var context: Context


    override fun saveUserInfo(userName: String?) {
        if (imagePath != null) {
            Retro.instance.makeMultipartBody(imagePath!!)
                    .subscribe({ uploadProfileImage(it) }, { Logger.e(it) })
        }

        if(userName != null){
            updateUserInfo(userName)
        }
    }

    // 프로필 사진 업데이트
    private fun uploadProfileImage(body: MultipartBody.Part) {
        Retro.instance.uploadService.postProfileImage(body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Logger.v("profile update: $it") }, { ErrorUtils.parseError(it) })
    }

    // 사용자 정보 업데이트 (사용자 이름)
    private fun updateUserInfo(userName : String) {
        Retro.instance.userService.updateUserInfo(App.setHeader(), UpdateUser(userName))
                .subscribeOn(Schedulers.io())
                .doAfterTerminate { (context as Activity).finish() }
                .subscribe({ Logger.v("update user: $it") }, { ErrorUtils.parseError(it) })
    }
}