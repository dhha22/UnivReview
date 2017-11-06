package com.univreview.view.contract

/**
 * Created by DavidHa on 2017. 9. 25..
 */
interface ProfileEditContract {
    interface View : BaseView
    fun saveUserInfo(userName:String)
}