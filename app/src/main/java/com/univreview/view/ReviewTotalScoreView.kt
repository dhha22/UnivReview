package com.univreview.view

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.univreview.App
import com.univreview.R
import com.univreview.model.model_kotlin.Review
import com.univreview.util.Util
import kotlinx.android.synthetic.main.review_total_score.view.*

/**
 * Created by DavidHa on 2017. 9. 13..
 */
class ReviewTotalScoreView(context: Context, attributeSet: AttributeSet? = null) : CardView(context, attributeSet) {
    init {
        LayoutInflater.from(context).inflate(R.layout.review_total_score, this, true)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, Util.dpToPx(context, 154), 0, App.dp12)
        cardElevation = App.dp1.toFloat()
        layoutParams = params
    }

    fun setData(difficultyRateAvg: Float, assignmentRateAvg: Float, attendanceRateAvg: Float, gradeRate: Float, achievementRate: Float) {
        val review = Review(difficultyRateAvg, assignmentRateAvg, attendanceRateAvg, gradeRate, achievementRate)
        averageScoreTxt.text = review.getAverageRate().toString()
        averageIndicator.rating = review.getAverageRate()
        reviewRatingIndicatorView.setData(review)
    }
}