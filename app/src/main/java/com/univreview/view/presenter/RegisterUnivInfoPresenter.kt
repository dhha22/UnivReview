package com.univreview.view.presenter

import android.content.Context
import com.univreview.App
import com.univreview.Navigator
import com.univreview.log.Logger
import com.univreview.model.Register
import com.univreview.model.User
import com.univreview.model.enumeration.ImageUploadType
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.util.ImageUtil
import com.univreview.view.contract.RegisterUnivInfoContract
import okhttp3.MultipartBody
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 7..
 */
class RegisterUnivInfoPresenter : RegisterUnivInfoContract {
    lateinit var view: RegisterUnivInfoContract.View
    lateinit var context: Context
    lateinit var register: Register


    // 회원 등록 과정
    override fun registerUser() {
        Retro.instance.loginService.signUp(register)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.response(it.data) }, { this.errorResponse(it) })
    }

    private fun response(user: User) {
        Logger.v("response: " + user)
        Observable.just(App.setUserInfo(user))
                .observeOn(Schedulers.io())
                .subscribe {
                    if (register.profileImageUri != null) {  // 회원 프로필 사진을 앨범에서 선택했을 경우
                        Retro.instance.makeMultipartBody(ImageUtil.getPath(register.profileImageUri), ImageUploadType.PROFILE_IMAGE)
                                .subscribe({ uploadProfileImage(it) }, { Logger.e(it) })
                    } else {  // profile uri 가 없는 경우 main 으로 이동
                        goMain()
                    }
                }
    }

    private fun errorResponse(e: Throwable) {
        view.dismissProgress()
        ErrorUtils.parseError(e)
    }

    private fun uploadProfileImage(body: MultipartBody.Part) {
        Retro.instance.uploadService.postProfileImage(App.getHeader(), body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Logger.v("profile update: $it") }, { ErrorUtils.parseError(it) })
    }

    private fun goMain() {
        view.dismissProgress()
        Navigator.goMain(context)
    }

}