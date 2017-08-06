package com.univreview.view.presenter

import android.content.Context
import com.univreview.App
import com.univreview.fragment.MypageFragment
import com.univreview.log.Logger
import com.univreview.model.Review
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.util.Util
import com.univreview.view.contract.UploadReviewContract
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 6..
 */
class UploadReviewPresenter(val review: Review = Review()) : UploadReviewContract {
    lateinit var view : UploadReviewContract.View
    lateinit var context : Context


    override fun registerReview() {
        review.let {
            it.userId = App.userId
            it.subjectDetail.subject.name = view.subjectName
            it.subjectDetail.professor.name = view.professorName
        }

        if (review.alertMessage == null) {
            callPostSimpleReviewApi(review)
            view.showProgress()
        } else {
            Util.simpleMessageDialog(context, review.alertMessage)
        }
    }

    private fun callPostSimpleReviewApi(review: Review) {
        Logger.v("post review: " + review)
        Logger.v("prof id: " + review.professorId)
        Logger.v("subj id: " + review.subjectId)
        Retro.instance.reviewService().postSimpleReview(App.setAuthHeader(App.userToken), review)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.dismissProgress() }
                .subscribe({ result -> response(result.review) },{ ErrorUtils.parseError(it) })
    }

    override fun checkReviewExist() {
        Retro.instance.reviewService().getReviewExist(App.setAuthHeader(App.userToken), review.subjectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    view.setReviewExist(result.exist)
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