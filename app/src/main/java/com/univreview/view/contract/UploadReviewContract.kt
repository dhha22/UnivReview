package com.univreview.view.contract

import com.univreview.model.model_kotlin.Review

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface UploadReviewContract {
    interface View : BaseView{
        fun showAlertDialog()
        fun showSimpleMsgDialog(msg : String)
        fun showRecommendRvDialog(review : Review)
    }

     fun registerReview()
     fun checkReviewExist()
}