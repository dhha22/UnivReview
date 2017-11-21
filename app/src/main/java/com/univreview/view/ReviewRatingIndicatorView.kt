package com.univreview.view

import android.content.Context
import android.util.AttributeSet
import com.dhha22.bindadapter.Item
import com.dhha22.bindadapter.ItemView
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.model.Review
import kotlinx.android.synthetic.main.review_rating_indicator.view.*

/**
 * Created by DavidHa on 2017. 11. 21..
 */
class ReviewRatingIndicatorView(context: Context, attributeSet: AttributeSet?) : ItemView(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    init {
        setContentView(R.layout.review_rating_indicator)
        setFullSpan()
    }

    override fun setData(data: Item?) {
        super.setData(data)
        if (data is Review) {
            Logger.v("review rating: " + data)
            difficultyRate.rating = data.difficultyRate
            assignmentRate.rating = data.assignmentRate
            attendanceRate.rating = data.attendanceRate
            gradeRate.rating = data.gradeRate
            achievementRate.rating = data.achievementRate
            difficultyTxt.text = data.getDifficultyRateMessage()
            assignmentTxt.text = data.getAssignmentRateMessage()
            attendanceTxt.text = data.getAttendanceRateMessage()
            gradeTxt.text = data.getGradeRateMessage()
            achievementTxt.text = data.getAchievementRateMessage()
        }
    }
}