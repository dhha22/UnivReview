package com.univreview.view.contract

import com.univreview.model.User

/**
 * Created by DavidHa on 2017. 9. 2..
 */
interface MyPageContract {
    interface View : BaseView {
        fun setUserData(data : User)
    }

    fun callUserProfile()
}