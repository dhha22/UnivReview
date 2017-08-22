package com.univreview.view.contract

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface UploadReviewContract {
    interface View : BaseView{
        var isReviewExist : Boolean
        fun showAlertDialog()
        fun showSimpleMsgDialog(msg : String)
        fun showRecommendRvDialog()
    }

     fun registerReview()
     fun checkReviewExist()
}