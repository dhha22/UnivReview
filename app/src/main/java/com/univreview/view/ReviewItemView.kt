package com.univreview.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.dhha22.bindadapter.Item
import com.dhha22.bindadapter.ItemView
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.model.enumeration.ReviewType
import com.univreview.model.Review
import kotlinx.android.synthetic.main.review_item.view.*

/**
 * Created by DavidHa on 2017. 8. 28..
 */
class ReviewItemView(context: Context, attributeSet: AttributeSet? = null) :ItemView(context, attributeSet) {

    private lateinit var review: Review
    private var id: Long = 0L

    init {
        setContentView(R.layout.review_item)
        setFullSpan()
    }


    fun setData(item: Item, type: ReviewType) {
        setMode(type)
        val review = item as Review
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

    private fun setContent(content : String?) {
        if (content == null) {  // review 내용
            contentTxt.visibility = View.GONE
        } else {
            contentTxt.visibility = View.VISIBLE
            contentTxt.text = content
        }
    }


    private fun setMode(type: ReviewType) {
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