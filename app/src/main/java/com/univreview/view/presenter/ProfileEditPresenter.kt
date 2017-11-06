package com.univreview.view.presenter

import android.app.Activity
import android.content.Context
import com.dhha22.bindadapter.Item
import com.univreview.App
import com.univreview.log.Logger
import com.univreview.model.DataModel
import com.univreview.model.ImageUploadResponse
import com.univreview.model.UpdateUser
import com.univreview.model.User
import com.univreview.model.enumeration.ImageUploadType
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.contract.ProfileEditContract
import okhttp3.MultipartBody
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject

/**
 * Created by DavidHa on 2017. 9. 25..
 */
class ProfileEditPresenter : ProfileEditContract {
    lateinit var view: ProfileEditContract.View
    var imagePath: String? = null
    lateinit var context: Context
    private val subject: PublishSubject<Boolean> = PublishSubject.create()


    override fun saveUserInfo(userName: String) {
        if (imagePath != null) {
            Retro.instance.makeMultipartBody(imagePath!!, ImageUploadType.PROFILE_IMAGE)
                    .subscribe({
                        uploadProfileImage(it)
                    }, { Logger.e(it) })
        } else {
             subject.onCompleted()
        }


        subject.doAfterTerminate { updateUserInfo(userName) }
                .subscribe{}
    }

    // 프로필 사진 업데이트
    private fun uploadProfileImage(body: MultipartBody.Part) {
        Retro.instance.uploadService.postProfileImage(App.getHeader(), body)
                .subscribeOn(Schedulers.io())
                .doAfterTerminate {
                    subject.onCompleted()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Logger.v("update user profile: $it") }, { ErrorUtils.parseError(it) })
    }

    // 사용자 정보 업데이트 (사용자 이름)
    private fun updateUserInfo(userName: String) {
        Retro.instance.userService.updateUserInfo(App.getHeader(), UpdateUser(userName))
                .subscribeOn(Schedulers.io())
                .doAfterTerminate { (context as Activity).finish() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Logger.v("update user info: $it") }, { ErrorUtils.parseError(it) })
    }
}