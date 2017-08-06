package com.univreview.view.contract

import android.content.Context
import com.univreview.model.Review

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

     fun getReview(): Review
     fun setContext(context: Context)
     fun registerReview()
     fun checkReviewExist()
}