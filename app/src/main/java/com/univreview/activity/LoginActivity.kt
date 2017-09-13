package com.univreview.activity

import android.content.Intent
import android.os.Bundle
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.util.exception.KakaoException
import com.univreview.App
import com.univreview.Navigator
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.view.contract.LoginContract
import com.univreview.view.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

/**
 * Created by DavidHa on 2017. 8. 4..
 */
class LoginActivity : BaseActivity(), LoginContract.View {
    private val facebookCallbackManager = CallbackManager.Factory.create()
    private val kakaoCallback = SessionCallback()
    private val presenter = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.setCurrentActivity(this)
        setFullScreen()
        if (App.userToken != null) {
            Navigator.goMain(this)
            finish()
            return
        }
        setContentView(R.layout.activity_login)
        presenter.apply {
            view = this@LoginActivity
            context = this@LoginActivity
        }

        // facebook
        LoginManager.getInstance().registerCallback(facebookCallbackManager, facebookCallback)
        facebookLoginBtn.setOnClickListener { facebookLogin() }

        //kakao
        Session.getCurrentSession().addCallback(kakaoCallback)
        kakaoLoginBtn.setOnClickListener { kakaoLogin() }
    }


    // facebook signIn
    private fun facebookLogin() {
        Logger.v("facebook signIn")
        showProgress()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))

    }

    // kakao signIn
    private fun kakaoLogin() {
        Logger.v("kakao signIn")
        showProgress()
        val isOpen = Session.getCurrentSession().checkAndImplicitOpen()
        Logger.v("is kakao session open: " + isOpen)
        if (!isOpen) {
            dismissProgress()
            Session.getCurrentSession().open(AuthType.KAKAO_TALK, this)
        }
    }


    // facebook callback
    private val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(loginResult: LoginResult) {
            presenter.facebookOnSuccess(loginResult)
        }

        override fun onCancel() {
            dismissProgress()
        }

        override fun onError(error: FacebookException) {
            dismissProgress()
            Logger.e(error)
            if (error is FacebookAuthorizationException) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut()
                    facebookLogin()
                }
            }
        }
    }


    // kakao callback
    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            // 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴
            Logger.v("카카오 로그인 세션 오픈")
            presenter.kakaoOnSuccess()
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                dismissProgress()
                Logger.e(exception.message)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        facebookCallbackManager?.onActivityResult(requestCode, resultCode, data)
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        LoginManager.getInstance().unregisterCallback(facebookCallbackManager)
        Session.getCurrentSession().removeCallback(kakaoCallback)
    }
}
