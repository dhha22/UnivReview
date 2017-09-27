package com.univreview.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.univreview.App
import com.univreview.R
import com.univreview.model.Review
import com.univreview.network.Retro
import com.univreview.util.Util
import kotlinx.android.synthetic.main.review_item_bottom.view.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 9. 23..
 */
class ReviewItemBottomView(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet) {
    private lateinit var review: Review

    init {
        LayoutInflater.from(context).inflate(R.layout.review_item_bottom, this, true)
        likeLayout.setOnClickListener { callReviewLike() }
    }

    fun setReview(review: Review) {
        this.review = review
    }

    fun setLike(isLike: Boolean) {
        likeImg.isSelected = isLike
    }

    fun setLikeCntTxt(likeCnt: Long) {
        likeCntTxt.text = String.format(context.getString(R.string.people_cnt), likeCnt)

    }

    fun setCommentCntTxt(commentCnt: Long) {
        commentCntTxt.text = String.format(context.getString(R.string.people_cnt), commentCnt)
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
                        review.notifyUpdate()
                    }, { setLikeState(likeImg.isSelected) })
        } else {
            setLikeState(likeImg.isSelected)
            Util.toast("네트워크 설정을 확인해주세요.")
        }
    }

    private fun setLikeState(isLike: Boolean) {
        val index = likeCntTxt.text.indexOf("명")
        var count = likeCntTxt.text.substring(0, index).toInt()
        if (isLike) { // 좋아요가 눌린 상태
            count--
            likeImg.isSelected = false
        } else {  // 좋아요를 누르기 전 상태
            count++
            likeImg.isSelected = true
        }
        likeCntTxt.text = String.format(context.getString(R.string.people_cnt), count)
    }

    fun increaseCommentCnt(isIncrease: Boolean) {
        val index = commentCntTxt.text.indexOf("명")
        val count = commentCntTxt.text.substring(0, index).toLong()
        if (isIncrease) {
            review.commentCount = count + 1
        } else {
            review.commentCount = count - 1
        }
        commentCntTxt.text = String.format(context.getString(R.string.people_cnt), review.commentCount)
        review.notifyUpdate()
    }
}