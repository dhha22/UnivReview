package com.univreview.fragment.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.univreview.App
import com.univreview.Navigator
import com.univreview.R
import com.univreview.fragment.BaseWriteFragment
import com.univreview.log.Logger
import com.univreview.model.enumeration.ReviewType
import com.univreview.model.Review
import com.univreview.model.ReviewDetail
import com.univreview.network.Retro
import com.univreview.util.Util
import kotlinx.android.synthetic.main.fragment_upload_review_detail.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 8..
 */
class UploadReviewDetailFragment : BaseWriteFragment() {
    companion object {

        @JvmStatic
        fun getInstance(review: Review, isFirst: Boolean): UploadReviewDetailFragment {
            val fragment = UploadReviewDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable("review", review)
            bundle.putBoolean("isFirst", isFirst)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var isFirst: Boolean = false
    private lateinit var review: Review


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        review = arguments.getParcelable("review")
        isFirst = arguments.getBoolean("isFirst")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_upload_review_detail, container, false)
        toolbar.setToolbarBackgroundColor(R.color.colorPrimary)
        toolbar.setBackBtnVisibility(true)
        toolbar.setOnRightMenuListener { registerReview(review.id) }
        rootLayout.addView(view)
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setReviewData(review)
        if (review.content != null) {
            toolbar.setTitleTxt("리뷰 수정")
            inputReview.setText(review.content)
            inputReview.setSelection(inputReview.text.toString().length)
        } else {
            toolbar.setTitleTxt("상세리뷰 쓰기")
        }
    }

    private fun setReviewData(review: Review) {
        reviewItem.setMode(ReviewType.WRITE_REVIEW)
        reviewItem.setData(review)
    }

    private fun registerReview(reviewId: Long) {
        Util.hideKeyboard(context, inputReview)
        val reviewDetail = ReviewDetail(inputReview.text.toString().trim())
        if (reviewDetail.getAlertMessage() == null) {
            Logger.v("review detail: " + reviewDetail)
            progressDialog.show()
            callPutReviewDetail(reviewId, reviewDetail)
        } else {
            Util.simpleMessageDialog(context, reviewDetail.getAlertMessage())
        }
    }

    // 리뷰 작성 경로
    // UploadReview -> UploadReviewDetail -> (ReviewDetail)
    // MyReview List -> ReviewDetail -> UploadReviewDetail


    // 리뷰 수정 경로
    // MyReview List -> ReviewDetail -> UploadReviewDetail

    private fun callPutReviewDetail(reviewId: Long, reviewDetail: ReviewDetail) {
        Logger.v("review id: " + reviewId)
        Retro.instance.reviewService().callPutReview(App.setHeader(), reviewId, reviewDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { progressDialog.dismiss() }
                .subscribe({
                    review.content = it.data.content
                    review.notifyUpdate()
                    if (isFirst) Navigator.goReviewDetail(context, review)
                    activity.finish()
                }, { this.errorResponse(it) })
    }


    private fun errorResponse(e: Throwable) {
        Logger.e(e)
    }
}