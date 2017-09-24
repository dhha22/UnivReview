package com.univreview.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.univreview.R
import com.univreview.model.Review
import kotlinx.android.synthetic.main.review_detail_header.view.*


/**
 * Created by DavidHa on 2017. 9. 4..
 */
class ReviewDetailHeader(context: Context) : FrameLayout(context) {
    private lateinit var review: Review

    init {
        LayoutInflater.from(context).inflate(R.layout.review_detail_header, this, true)

    }

    fun setData(review: Review) {
        this.review = review.apply {
            setContent(content)
            headerView.setUserData(user!!)
            headerView.setSubjectProfessor(subject!!.name, professor!!.name)
            headerView.setTimeTxt(createdAt!!)
            reviewRatingIndicatorView.setData(this)
            bottomView.setReview(this)
            bottomView.setLike(isLike)
            bottomView.setLikeCntTxt(likeCount)
            bottomView.setCommentCntTxt(commentCount)
        }
    }

    fun setContent(content : String?){
        if (content == null) {  // review 내용
            reviewDetailLayout.visibility = View.GONE
        } else {
            reviewDetailLayout.visibility = View.VISIBLE
            contentTxt.text = content
        }
    }
}