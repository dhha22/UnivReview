package com.univreview.view

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.univreview.App
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.model.model_kotlin.Review
import com.univreview.network.Retro
import com.univreview.util.TimeUtil
import com.univreview.util.Util
import kotlinx.android.synthetic.main.review_detail_header.view.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 * Created by DavidHa on 2017. 9. 4..
 */
class ReviewDetailHeader(context: Context) : FrameLayout(context) {
    private lateinit var review: Review

    init {
        LayoutInflater.from(context).inflate(R.layout.review_detail_header, this, true)
        likeLayout.setOnClickListener { callReviewLike() }
    }

    fun setData(review: Review) {
        this.review = review.apply {
            user?.let {
                nameTxt.text = it.name
                if (it.authenticated) {
                    authMark.visibility = View.VISIBLE
                } else {
                    authMark.visibility = View.GONE
                }
            }
            if (content == null) {  // review 내용
                reviewDetailLayout.visibility = View.GONE
            } else {
                contentTxt.visibility = View.VISIBLE
                contentTxt.text = content
            }

            // subject professor 이름 설정
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

            likeImg.isSelected = isLike
            likeCnt.text = String.format(context.getString(R.string.people_cnt), likeCount)
            commentCnt.text = String.format(context.getString(R.string.people_cnt), commentCount)
            timeTxt.text = TimeUtil().getPointFormat(createdAt)
            reviewRatingIndicatorView.setData(this)
        }
    }

    private fun callReviewLike() {
        setLikeState(likeImg.isSelected)
        if (review.id != 0L) {
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

    fun setEtcBtnClickListener(clickListener: OnClickListener) {
        etcBtn.setOnClickListener(clickListener)
    }

    fun setCommentMoreBtnListener(click: () -> Unit) {
        commentMore.setOnClickListener { click.invoke() }
    }

    fun setCommentMoreBtn(hasMore: Boolean) {
        if (hasMore) {
            commentMore.visibility = View.VISIBLE
        } else {
            commentMore.visibility = View.GONE
        }
    }
}