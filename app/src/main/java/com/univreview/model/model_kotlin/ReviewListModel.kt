package com.univreview.model.model_kotlin

/**
 * Created by DavidHa on 2017. 9. 3..
 */
data class ReviewListModel(val difficultyRateAvg: Float = 0F,
                           val assignmentRateAvg: Float = 0F,
                           val attendanceRateAvg: Float = 0F,
                           val gradeRateAvg: Float = 0F,
                           val achievementRateAvg: Float = 0F,
                           val totalAvg: Float = 0F,
                           val reviews: List<Review>)