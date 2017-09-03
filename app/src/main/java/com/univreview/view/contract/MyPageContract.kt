package com.univreview.view.contract

import com.univreview.model.model_kotlin.User

/**
 * Created by DavidHa on 2017. 9. 2..
 */
interface MyPageContract {
    interface View : BaseView {
        fun setUserData(data : User)
    }

}