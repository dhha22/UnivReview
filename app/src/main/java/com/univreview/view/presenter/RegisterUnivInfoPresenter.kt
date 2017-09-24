package com.univreview.view.presenter

import android.content.Context
import com.univreview.App
import com.univreview.Navigator
import com.univreview.log.Logger
import com.univreview.model.User
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.contract.RegisterUnivInfoContract
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 7..
 */
class RegisterUnivInfoPresenter : RegisterUnivInfoContract {
    lateinit var view: RegisterUnivInfoContract.View
    lateinit var context: Context
    lateinit var register: User


    // 회원 등록 과정
    override fun registerUser() {
        Retro.instance.loginService.signUp(register)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.response(it.data) }, { this.errorResponse(it) })
    }

    private fun response(user: User) {
        Logger.v("response: " + user)
        Observable.just(user)
                .observeOn(Schedulers.newThread())
                .subscribe {
                    App.setUid(it.uid)
                    App.setUserToken(it.accessToken)
                    App.setClient(it.client)
                    App.setUniversityId(it.universityId!!)
                    App.setUserId(it.id)
                }
       /* // 회원 프로필 사진을 앨범에서 선택했을 경우
        register.profileUri?:goMain()   // profile uri 가 없는 경우 main 으로 이동
        callImageFileUploadApi(register.profileUri)*/
    }

    private fun errorResponse(e: Throwable) {
        view.dismissProgress()
        ErrorUtils.parseError(e)
    }


    // File upload -> User 회원가입

    private fun callImageFileUploadApi(imagePath: String) {
        Retro.instance.fileService(imagePath, "profile")
                .subscribeOn(Schedulers.io())
                .subscribe({ callUserProfileUpdateApi(it.data.objKey) }) { goMain() }
    }

    private fun callUserProfileUpdateApi(profileUrl: String) {
       /* Logger.v("file location: " + profileUrl)
        val user = User().apply {
            profileImageUrl = profileUrl
        }
        Retro.instance.userService().postProfile(App.setAuthHeader(App.userToken), user, App.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate({ goMain() })
                .subscribe({ result -> Logger.v("profile update: " + result) }, { ErrorUtils.parseError(it) })*/
    }

    private fun goMain() {
        view.dismissProgress()
        Navigator.goMain(context)
    }

}