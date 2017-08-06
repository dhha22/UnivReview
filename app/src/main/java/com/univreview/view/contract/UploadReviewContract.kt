package com.univreview.view.contract

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface UploadReviewContract {
    interface View {
        fun showProgress()
        fun dismissProgress()
        fun showRecommendRvDialog()
        val subjectName: String
        val professorName: String
        fun showAlertDialog()
        fun setReviewExist(isExist: Boolean)
    }

     fun registerReview()
     fun checkReviewExist()
}