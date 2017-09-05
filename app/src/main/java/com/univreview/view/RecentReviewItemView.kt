package com.univreview.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout

import com.univreview.App
import com.univreview.Navigator
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.model.RandomImageModel
import com.univreview.model.model_kotlin.Review
import kotlinx.android.synthetic.main.recent_review_item.view.*


/**
 * Created by DavidHa on 2017. 8. 28..
 */
class RecentReviewItemView(context: Context?) : FrameLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.recent_review_item, this, true)
        App.picasso.load(RandomImageModel().imageURL)
                .fit()
                .centerCrop()
                .into(image)
    }

    fun setData(review: Review) {
        review.let {
            Logger.v("professor name : " + it.professor?.name)
            subjectTxt.text = it.subject?.name
            professorTxt.text = it.professor?.name

            difficultyTxt.text = it.getDifficultyRateMessage()
            assignmentTxt.text = it.getAssignmentRateMessage()
            attendanceTxt.text = it.getAttendanceRateMessage()
            gradeTxt.text = it.getGradeRateMessage()
            achievementTxt.text = it.getAchievementRateMessage()

            difficultyRatingBar.rating = it.difficultyRate
            assignmentRatingBar.rating = it.assignmentRate
            attendanceRatingBar.rating = it.attendanceRate
            gradeRatingBar.rating = it.gradeRate
            achievementRatingBar.rating = it.achievementRate
        }
        setOnClickListener { Navigator.goReviewDetail(context, review) }
    }
}