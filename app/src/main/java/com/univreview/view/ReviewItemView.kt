package com.univreview.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.univreview.R
import com.univreview.model.enumeration.ReviewType
import com.univreview.model.model_kotlin.Review
import kotlinx.android.synthetic.main.review_item.view.*

/**
 * Created by DavidHa on 2017. 8. 28..
 */
class ReviewItemView(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet) {

    private lateinit var review: Review
    private var id: Long = 0L

    init {
        LayoutInflater.from(context).inflate(R.layout.review_item, this, true)
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    fun setData(review: Review, type: ReviewType) {
        setMode(type)
        this.id = review.id
        this.review = review.apply {
            headerView.setUserData(user!!)
            headerView.setSubjectProfessor(subject!!.name, professor!!.name)
            headerView.setTimeTxt(createdAt!!)
            difficultyTxt.text = getDifficultyRateMessage()
            assignmentTxt.text = getAssignmentRateMessage()
            attendanceTxt.text = getAttendanceRateMessage()
            gradeTxt.text = getGradeRateMessage()
            achievementTxt.text = getAchievementRateMessage()

            difficultyRatingBar.rating = difficultyRate
            assignmentRatingBar.rating = assignmentRate
            attendanceRatingBar.rating = attendanceRate
            gradeRatingBar.rating = gradeRate
            achievementRatingBar.rating = achievementRate
            setContent(content)
            bottomView.setReview(this)
            bottomView.setLike(isLike)
            bottomView.setLikeCntTxt(likeCount)
            bottomView.setCommentCntTxt(commentCount)

        }
    }

    fun setContent(content : String?) {
        if (content == null) {  // review 내용
            contentTxt.visibility = View.GONE
        } else {
            contentTxt.visibility = View.VISIBLE
            contentTxt.text = content
        }
    }


    fun setMode(type: ReviewType) {
        when (type) {
            ReviewType.WRITE_REVIEW -> { // 사용자가 리뷰를 쓸 때
                headerView.isUserLayoutVisible(false)
                headerView.isEtcBtnVisible(false)
                contentTxt.visibility = View.GONE
                bottom_line.visibility = View.GONE
                bottomView.visibility = View.GONE
            }
            ReviewType.MY_REVIEW -> {    // 마이리뷰를 봤을 때
                headerView.isUserLayoutVisible(false)
            }
            ReviewType.READ_REVIEW -> {  // 다른사람 리뷰를 읽을 때

            }
        }
    }

}