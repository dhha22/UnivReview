package com.univreview.view.contract

import android.content.Intent
import com.univreview.model.Review

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface UploadReviewContract {
    interface View : BaseView {
        fun showAlertDialog()
        fun showSimpleMsgDialog(msg: String)
        fun showRecommendRvDialog(review: Review)
        fun setSubjectTxt(str: String?)
        fun setProfessorTxt(str: String?)
    }

    fun onActivityResult(data: Intent)
    fun registerReview()
    fun checkReviewExist()
}