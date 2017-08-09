package com.univreview.fragment.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.univreview.App
import com.univreview.Navigator
import com.univreview.R
import com.univreview.activity.BaseActivity
import com.univreview.fragment.BaseWriteFragment
import com.univreview.log.Logger
import com.univreview.model.Review
import com.univreview.model.ReviewDetail
import com.univreview.network.Retro
import com.univreview.util.Util
import com.univreview.view.ReviewItemView
import kotlinx.android.synthetic.main.fragment_upload_review_detail.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 8..
 */
class UploadReviewDetailFragment : BaseWriteFragment() {
    companion object {

        @JvmStatic
        fun getInstance(review: Review): UploadReviewDetailFragment {
            val fragment = UploadReviewDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable("review", review)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var isUpdate = false    // 리뷰 수정
    private lateinit var review: Review
    private lateinit var reviewDetail : ReviewDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        review = arguments.getSerializable("review") as Review
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_upload_review_detail, container, false)
        toolbar.setToolbarBackgroundColor(R.color.colorPrimary)
        toolbar.setBackBtnVisibility(true)
        toolbar.setOnConfirmListener { registerReview(review.id) }
        rootLayout.addView(view)
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setReviewData(review)
        if (review.reviewDetail != null) {
            isUpdate = true
            toolbar.setTitleTxt("리뷰 수정")
            inputReview.setText(review.reviewDetail.reviewDetail)
            inputReview.setSelection(inputReview.text.toString().length)
        } else {
            toolbar.setTitleTxt("상세리뷰 쓰기")
        }
    }

    private fun setReviewData(review: Review) {
        reviewItem.setData(review)
        reviewItem.setMode(ReviewItemView.Status.WRITE_REVIEW)
    }

    private fun registerReview(reviewId: Long) {
        Util.hideKeyboard(context, inputReview)
        reviewDetail = ReviewDetail()
        reviewDetail.reviewId = reviewId
        reviewDetail.reviewDetail = inputReview.text.toString()
        if (reviewDetail.alertMessage == null) {
            Logger.v("review detail: " + reviewDetail)
            progressDialog.show()
            if (isUpdate) {
                callPutReviewDetail(review.reviewDetail.id, reviewDetail)
            } else {
                callPostReviewDetail(reviewDetail)
            }
        } else {
            Util.simpleMessageDialog(context, reviewDetail.alertMessage)
        }
    }

    // 리뷰 작성
    // UploadReview -> UploadReviewDetail -> (ReviewDetail)
    // MyReview List -> ReviewDetail -> UploadReviewDetail
    private fun callPostReviewDetail(reviewDetail: ReviewDetail) {
        Retro.instance.reviewService().postDetailReview(App.setAuthHeader(App.userToken), reviewDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { progressDialog.dismiss() }
                .subscribe({
                    activity.finish()
                }, { this.errorResponse(it) })
    }

    // 리뷰 수정
    // MyReview List -> ReviewDetail -> UploadReviewDetail
    private fun callPutReviewDetail(reviewDetailId: Long, reviewDetail: ReviewDetail) {
        Logger.v("review detail id: " + reviewDetailId)
        Retro.instance.reviewService().putReviewDetail(App.setAuthHeader(App.userToken), reviewDetailId, reviewDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { progressDialog.dismiss() }
                .subscribe({
                    activity.finish()
                }, { this.errorResponse(it) })
    }


    private fun errorResponse(e: Throwable) {
        Logger.e(e)
    }
}