package com.univreview.view.presenter

import android.content.Context
import android.os.Bundle
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.univreview.App
import com.univreview.Navigator
import com.univreview.log.Logger
import com.univreview.model.Login
import com.univreview.model.Register
import com.univreview.model.UserModel
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.util.Util
import com.univreview.view.contract.LoginContract
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 6..
 */
class LoginPresenter : LoginContract {
    lateinit var view: LoginContract.View
    lateinit var context: Context

    override fun facebookOnSuccess(loginResult: LoginResult) {
        val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
            Logger.v("facebook responseMajor" + response.toString())
            val userId = `object`.getString("id")   //facebook user id
            val accessToken = loginResult.accessToken.token    //facebookAccessToken
            val nickName = `object`.getString("name") // name
            val profileUrl = `object`.getJSONObject("picture").getJSONObject("data").getString("url") //facebook profile image
            val email: String? = `object`.getString("email") // facebook email
            callLoginApi("F", userId, accessToken, nickName, profileUrl, email)
        }
        val parameters = Bundle()
        parameters.putString("fields", "id, name, picture.type(large)")
        request.parameters = parameters
        request.executeAsync()
    }

    override fun kakaoOnSuccess() {
        UserManagement.requestMe(object : MeResponseCallback() {
            override fun onFailure(errorResult: ErrorResult?) {
                view.dismissProgress()
                val ErrorCode = errorResult!!.errorCode
                val ClientErrorCode = -777
                if (ErrorCode == ClientErrorCode) {
                    Util.toast("카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.")
                } else {
                    Logger.e("오류로 카카오로그인 실패 ")
                }
            }

            override fun onSessionClosed(errorResult: ErrorResult) {
                view.dismissProgress()
                Logger.e("카카오 로그인 에러")
            }

            override fun onSuccess(userProfile: UserProfile) {
                Logger.v("카카오 로그인 성공")
                val userId = userProfile.id.toString()
                val accessToken = Session.getCurrentSession().tokenInfo.accessToken
                val nickName = userProfile.nickname  //kakao nickname
                val profileURL = userProfile.profileImagePath  //kakao profile image
                val email: String? = userProfile.email // kakao email

                callLoginApi("K", userId, accessToken, nickName, profileURL, email)
            }

            override fun onNotSignedUp() {
                Logger.v("자동로그인 아님")
                // 자동가입이 아닐경우 동의창
            }
        })
    }

    //api
    private fun callLoginApi(userType: String, userId: String, accessToken: String, nickName: String, profileURL: String, email: String? = null) {
        Logger.v("userType: $userType, userId: $userId, accessToken: $accessToken, nickName: $nickName, profileUrl: $profileURL, email: $email")
        Retro.instance.loginService().login(App.setAuthHeader(""), Login(userType, userId, accessToken))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.dismissProgress() }
                .subscribe({ this.response(it) })
                { error -> loginErrorResponse(error, Register(userType, userId, accessToken, nickName, profileURL, email)) }
    }

    private fun response(userModel: UserModel) {
        Logger.v("response: " + userModel)
        Observable.just(userModel)
                .observeOn(Schedulers.newThread())
                .subscribe {
                    App.setUserId(it.user.id)
                    App.setUserToken(it.auth.token)
                    App.setUniversityId(it.user.universityId)

                }
        Navigator.goMain(context)
    }

    private fun loginErrorResponse(error: Throwable, register: Register) {
        if (ErrorUtils.parseError(error) == ErrorUtils.ERROR_404) {   //신규회원
            Navigator.goRegisterUserInfo(context, register)
        } else {
            Util.toast("서버 오류입니다.")
        }
    }
}