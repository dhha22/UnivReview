package com.univreview.view

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.dhha22.bindadapter.Item
import com.dhha22.bindadapter.ItemView

import com.univreview.App
import com.univreview.Navigator
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.model.RandomImageModel
import com.univreview.model.Review
import kotlinx.android.synthetic.main.recent_review_item.view.*


/**
 * Created by DavidHa on 2017. 8. 28..
 */
class RecentReviewItemView(context: Context) : ItemView(context) {
    init {
        setContentView(R.layout.recent_review_item)
        App.picasso.load(RandomImageModel().imageURL)
                .fit()
                .centerCrop()
                .into(image)
    }

    override fun setData(data: Item?) {
        super.setData(data)
        if(data is Review){
            Logger.v("professor name : " + data.professor?.name)
            subjectTxt.text = data.subject?.name
            professorTxt.text = data.professor?.name

            difficultyTxt.text = data.getDifficultyRateMessage()
            assignmentTxt.text = data.getAssignmentRateMessage()
            attendanceTxt.text = data.getAttendanceRateMessage()
            gradeTxt.text = data.getGradeRateMessage()
            achievementTxt.text = data.getAchievementRateMessage()


            difficultyRatingBar.rating = data.difficultyRate
            assignmentRatingBar.rating = data.assignmentRate
            attendanceRatingBar.rating = data.attendanceRate
            gradeRatingBar.rating = data.gradeRate
            achievementRatingBar.rating = data.achievementRate
            setOnClickListener { Navigator.goReviewDetail(context, data) }
        }
    }

}