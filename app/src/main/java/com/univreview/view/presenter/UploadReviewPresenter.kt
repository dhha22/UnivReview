package com.univreview.view.presenter

import android.content.Intent
import com.univreview.App
import com.univreview.log.Logger
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.model.Review
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.contract.UploadReviewContract
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 6..
 */
class UploadReviewPresenter(val review: Review = Review()) : UploadReviewContract {
    lateinit var view: UploadReviewContract.View

    override fun registerReview() {
        if (review.getAlertMessage() == null) {
            callPostSimpleReviewApi(review)
            view.showProgress()
        } else {
            view.showSimpleMsgDialog(review.getAlertMessage().toString())
        }
    }

    private fun callPostSimpleReviewApi(review: Review) {
        Logger.v("post review: " + review)
        Logger.v("course id: " + review.courseId)
        Logger.v("subj id: " + review.subjectId)
        Retro.instance.reviewService.callPostReview(App.setHeader(), review)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.dismissProgress() }
                .subscribe({ response(it.data) }, { ErrorUtils.parseError(it) })
    }

    override fun checkReviewExist() {
        Retro.instance.reviewService.callCheckReviewForCourseId(App.setHeader(), review.courseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isExist) {
                        view.showAlertDialog()
                        review.courseId = 0
                    }
                }, { ErrorUtils.parseError(it) })
    }

    private fun response(review: Review) {
        Logger.v("response review: " + review)
        view.showRecommendRvDialog(review)
    }

    override fun onActivityResult(data: Intent) {
        val id = data.getLongExtra("id", 0)
        val name = data.getStringExtra("name")
        val type = data.getSerializableExtra("type") as ReviewSearchType
        if (ReviewSearchType.SUBJECT_WITH_RESULT == type) {
            view.setSubjectTxt(name)
            view.setProfessorTxt(null)
            review.subjectId = id
            review.courseId = 0
        } else if (ReviewSearchType.PROF_FROM_SUBJ == type) {
            view.setProfessorTxt(name)
            review.courseId = id
            checkReviewExist()
        }
    }

}