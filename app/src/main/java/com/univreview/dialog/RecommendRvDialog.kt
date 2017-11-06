package com.univreview.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import butterknife.ButterKnife
import com.univreview.Navigator
import com.univreview.R
import com.univreview.activity.BaseActivity
import com.univreview.model.Review
import kotlinx.android.synthetic.main.dialog_recommend_rv.*

/**
 * Created by DavidHa on 2017. 11. 6..
 */
class RecommendRvDialog(context: Context, val review: Review) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        //외부 dim 처리
        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.8f
        window.attributes = lpWindow
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.dialog_recommend_rv)
        ButterKnife.bind(this)
        setCancelable(false)

        // 계속해서 상세 리뷰를 남긴다 (리뷰 업로드 상세 페이지로 이동)
        goReviewDetail.setOnClickListener {
            dismiss()
            Navigator.goUploadReviewDetail(context, review, true)
            (context as Activity).finish()
        }

        // 다음에 리뷰를 남긴다 (리뷰 디테일 페이지로 이동)
        next.setOnClickListener {
            dismiss()
            //com.univreview.Navigator.goReviewDetail(context, review);
            (context as BaseActivity).setOnBackPressedListener(null)
            (context as Activity).onBackPressed()
        }
    }
}