package com.univreview.view.presenter

import android.app.Activity
import android.content.Context
import com.univreview.App
import com.univreview.log.Logger
import com.univreview.model.UpdateUser
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.contract.ProfileEditContract
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 9. 25..
 */
class ProfileEditPresenter : ProfileEditContract {
    lateinit var view: ProfileEditContract.View
    var imagePath: String? = null
    lateinit var userName: String
    lateinit var context: Context


    override fun saveUserInfo() {
        if (imagePath != null) {    // 프로필 사진이 바뀌었을 경우
            Retro.instance.fileService(imagePath!!, "profile")  // 이미지 파일 업로드
                    .subscribeOn(Schedulers.io())
                    .subscribe({ updateUserInfo(it.data.objKey) }, { ErrorUtils.parseError(it) })
        } else {
            updateUserInfo()
        }
    }

    // 사용자 정보 업데이트
    private fun updateUserInfo(imageUrl: String? = null) {
        Retro.instance.userService.updateUserInfo(App.setHeader(), UpdateUser(userName, imageUrl))
                .subscribeOn(Schedulers.io())
                .doAfterTerminate { (context as Activity).finish() }
                .subscribe({ Logger.v("update user: $it") }, { ErrorUtils.parseError(it) })
    }
}