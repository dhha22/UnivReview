package com.univreview.view.presenter

import com.univreview.App
import com.univreview.fragment.MypageFragment
import com.univreview.log.Logger
import com.univreview.model.model_kotlin.Review
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.contract.UploadReviewContract
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 6..
 */
class UploadReviewPresenter(val review : Review = Review()) : UploadReviewContract {
    lateinit var view : UploadReviewContract.View
    var subjectName:String? = null
    var professorName:String? = null

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
        Retro.instance.reviewService().callPostReview(App.setHeader(), review)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.dismissProgress() }
                .subscribe({ result -> response(result.data) },{ ErrorUtils.parseError(it) })
    }

    override fun checkReviewExist() {
        Retro.instance.reviewService().getReviewExist(App.setAuthHeader(App.userToken), review.subjectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.exist) {
                        view.showAlertDialog()
                    }
                },  { ErrorUtils.parseError(it) })
    }

    private fun response(review: Review) {
        Logger.v("response review: " + review)
        MypageFragment.isRefresh = true
        this.review.id = review.id
        view.showRecommendRvDialog()
    }

}