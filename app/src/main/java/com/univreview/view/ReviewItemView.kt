package com.univreview.view

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.univreview.App
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.model.model_kotlin.Review
import com.univreview.network.Retro
import com.univreview.util.Util
import kotlinx.android.synthetic.main.review_item.view.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.StringBuilder
import java.util.jar.Attributes

/**
 * Created by DavidHa on 2017. 8. 28..
 */
class ReviewItemView(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet) {

    private lateinit var review: Review
    private var id: Long = 0L

    init {
        LayoutInflater.from(context).inflate(R.layout.review_item, this, true)
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        likeLayout.setOnClickListener { callReviewLike() }

    }

    fun setData(review: Review) {
        this.id = review.id
        this.review = review.apply {
            user?.let {
                nameTxt.text = it.name
                if (it.authenticated) {
                    authMark.visibility = View.VISIBLE
                } else {
                    authMark.visibility = View.GONE
                }
            }

            // subject professor 이름 설정
            if (subjectProfessorTxt.visibility == View.VISIBLE) {
                Logger.v("subjectProfessor")
                val builder = SpannableStringBuilder()
                var index = 0
                builder.append(subject?.name + " ")
                Util.addSizeSpan(builder, index, Util.dpToPx(context, 16))
                Util.addColorSpan(context, builder, index, R.color.colorPrimary)
                index = builder.length
                builder.append(professor?.name + " 교수님")
                Util.addSizeSpan(builder, index, Util.dpToPx(context, 14))
                Util.addColorSpan(context, builder, index, R.color.professorTxtColor)
                subjectProfessorTxt.text = builder
            } else {
                subjectTxt.text = subject?.name
                professorTxt.text = professor?.name
            }


            if (content == null) {  // review 내용
                contentTxt.visibility = View.GONE
            } else {
                contentTxt.visibility = View.VISIBLE
                contentTxt.text = content
            }

            likeImg.isSelected = isLike
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

    fun moreBtnSetOnClickListener(click: (view: View) -> Unit) {
        moreBtn.setOnClickListener { click.invoke(this) }
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

    private fun callReviewLike() {
        setLikeState(likeImg.isSelected)
        if (id != 0L) {
            Retro.instance.reviewService().callReviewLike(App.setHeader(), review.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        // 좋아요 성공했을 경우
                        review.isLike = likeImg.isSelected
                        if (review.isLike) {
                            review.likeCount++
                        } else {
                            review.likeCount--
                        }
                    }, { setLikeState(likeImg.isSelected) })
        } else {
            setLikeState(likeImg.isSelected)
            Util.toast("네트워크 설정을 확인해주세요.")
        }
    }

    private fun setLikeState(isLike: Boolean) {
        val index = likeCnt.text.indexOf("명")
        var count = likeCnt.text.substring(0, index).toInt()
        if (isLike) { // 좋아요가 눌린 상태
            count--
            likeImg.isSelected = false
        } else {  // 좋아요를 누르기 전 상태
            count++
            likeImg.isSelected = true
        }
        likeCnt.text = String.format(context.getString(R.string.people_cnt), count)

    }

}