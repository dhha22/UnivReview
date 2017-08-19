package com.univreview.view.presenter

import android.content.Context
import android.net.Uri
import com.univreview.App
import com.univreview.Navigator
import com.univreview.log.Logger
import com.univreview.model.Register
import com.univreview.model.Token
import com.univreview.model.User
import com.univreview.model.UserModel
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.util.ImageUtil
import com.univreview.view.contract.RegisterUnivInfoContract
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
    // temp token -> register -> image file upload -> update profile
    override fun registerUser() {
        Retro.instance.tokenService().tempToken(App.setAuthHeader(""))
                .subscribeOn(Schedulers.io())
                .subscribe({ result -> callRegisterApi(register, result) }) { this.errorResponse(it) }
    }

    private fun callRegisterApi(register: Register, token: Token) {
        Logger.v("userToken: " + token)
        Logger.v("register : " + register)
        Retro.instance.userService().register(App.setAuthHeader(token.token), register)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.response(it) }, { this.errorResponse(it) })
    }

    private fun response(userModel: UserModel) {
        Logger.v("response: " + userModel)
        App.setUserId(userModel.user.id)
        App.setUserToken(userModel.auth.token)
        App.setUniversityId(App.universityId)

        // 회원 프로필 사진을 앨범에서 선택했을 경우
        register.profileUri?:goMain()   // profile uri 가 없는 경우 main 으로 이동
        callImageFileUploadApi(register.profileUri)

    }

    private fun errorResponse(e: Throwable) {
        view.dismissProgress()
        ErrorUtils.parseError(e)
    }


    // File upload -> User 회원가입

    private fun callImageFileUploadApi(uploadUri: Uri?) {
        Retro.instance.fileService(ImageUtil.getPath(uploadUri), "profile")
                .subscribeOn(Schedulers.io())
                .subscribe({ result -> callUserProfileUpdateApi(result.fileLocation) }) { goMain() }
    }

    private fun callUserProfileUpdateApi(profileUrl: String) {
        Logger.v("file location: " + profileUrl)
        val user = User().apply {
            profileImageUrl = profileUrl
        }
        Retro.instance.userService().postProfile(App.setAuthHeader(App.userToken), user, App.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate({ goMain() })
                .subscribe({ result -> Logger.v("profile update: " + result) }, { ErrorUtils.parseError(it) })
    }

    private fun goMain() {
        view.dismissProgress()
        Navigator.goMain(context)
    }

}