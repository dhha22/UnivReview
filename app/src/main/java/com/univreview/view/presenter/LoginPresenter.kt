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
import com.univreview.BuildConfig
import com.univreview.Navigator
import com.univreview.log.Logger
import com.univreview.model.SignIn
import com.univreview.model.model_kotlin.User
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
        val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, _ ->
            Logger.v("facebook response: " + `object`.toString())
            val userId = `object`.getString("id")   //facebook user id
            val accessToken = loginResult.accessToken.token    //facebookAccessToken
            val profileUrl = `object`.getJSONObject("picture").getJSONObject("data").getString("url") //facebook profile image
            val email: String? = `object`.has("email").let { `object`.getString("email") } // facebook email
            callLoginApi("facebook", userId, accessToken, profileUrl, email)
        }
        val parameters = Bundle()
        parameters.putString("fields", "id, name, picture.type(large), email")
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
                val profileURL = userProfile.profileImagePath  //kakao profile image
                val email: String? = userProfile.email // kakao email

                callLoginApi("kakao", userId, accessToken, profileURL, email)
            }

            override fun onNotSignedUp() {
                Logger.v("자동로그인 아님")
                // 자동가입이 아닐경우 동의창
            }
        })
    }

    //api
    private fun callLoginApi(provider: String, userId: String, accessToken: String, profileURL: String, email: String? = null) {
        Logger.v("provider: $provider, userId: $userId, accessToken: $accessToken, profileUrl: $profileURL, email: $email")
        Retro.instance.loginService.signIn(SignIn(accessToken, provider))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.dismissProgress() }
                .subscribe({ this.response(it.data, User(provider, userId.toLong(), email, profileURL, accessToken)) })
                { error -> loginErrorResponse(error, User(provider, userId.toLong(),  email, profileURL, accessToken)) }
    }

    private fun response(userModel: User?, register: User) {
        Logger.v("response: " + userModel)
        userModel?.let {
            Observable.just(userModel)
                    .observeOn(Schedulers.newThread())
                    .subscribe {
                        App.setUniversityId(it.universityId!!)
                        App.setUserId(it.id)
                        App.setUid(it.uid)
                        App.setUserToken(it.accessToken)
                        App.setClient(it.client)
                    }
            Navigator.goMain(context)
        }
        userModel ?: Navigator.goRegisterUserInfo(context, register)
    }

    private fun loginErrorResponse(error: Throwable, register: User) {
        if (ErrorUtils.parseError(error) == ErrorUtils.ERROR_401) {   //신규회원
            Navigator.goRegisterUserInfo(context, register)
        } else {
            if (BuildConfig.DEBUG) {
                Navigator.goMain(context)
            }
            Util.toast("서버 오류입니다.")
        }
    }
}