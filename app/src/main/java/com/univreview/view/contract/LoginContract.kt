package com.univreview.view.contract

import com.facebook.login.LoginResult

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface LoginContract {
    interface View : BaseView{
    }

    fun facebookOnSuccess(loginResult: LoginResult)
    fun kakaoOnSuccess()
}