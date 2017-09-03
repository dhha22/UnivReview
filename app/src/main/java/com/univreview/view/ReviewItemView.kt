package com.univreview.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.univreview.App
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.model.model_kotlin.Review
import com.univreview.network.Retro
import kotlinx.android.synthetic.main.review_item.view.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.StringBuilder

/**
 * Created by DavidHa on 2017. 8. 28..
 */
class ReviewItemView(context: Context) : FrameLayout(context) {

    private lateinit var review: Review

    init {
        LayoutInflater.from(context).inflate(R.layout.review_item, this, true)
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    fun setData(review: Review) {
        Logger.v("review: $review")
        this.review = review.apply {
            user?.let {
                nameTxt.text = it.name
                if (it.authenticated) {
                    authMark.visibility = View.VISIBLE
                } else {
                    authMark.visibility = View.GONE
                }
            }

            subjectTxt.text = subject?.name
            professorTxt.text = professor?.name

            if (content == null) {  // review 내용
                contentTxt.visibility = View.GONE
            } else {
                contentTxt.visibility = View.VISIBLE
                contentTxt.text = content
            }

            likeCnt.text = String.format(context.getString(R.string.people_cnt), likeCount)
            commentCnt.text = String.format(context.getString(R.string.people_cnt), commentCount)

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
        }

    }

    enum class Status {
        WRITE_REVIEW, MY_REVIEW, READ_REVIEW
    }

    fun setMode(status: Status) {
        when (status) {
            ReviewItemView.Status.WRITE_REVIEW -> { // 사용자가 리뷰를 쓸 때
                subjectTxt.visibility = View.GONE
                professorTxt.visibility = View.GONE
                subjectProfessorTxt.visibility = View.VISIBLE
                userLayout.visibility = View.GONE
                contentTxt.visibility = View.GONE
                moreBtn.visibility = View.GONE
                bottom_line.visibility = View.GONE
                bottom_layout.visibility = View.GONE
            }
            ReviewItemView.Status.MY_REVIEW -> {    // 마이리뷰를 봤을 때
                userLayout.visibility = View.GONE
            }
            ReviewItemView.Status.READ_REVIEW -> {  // 다른사람 리뷰를 읽을 때

            }
        }
    }

    private fun callReviewLike(id: Long) {
        Retro.instance.reviewService().callReviewLike(App.setHeader(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, {

                })
    }

}