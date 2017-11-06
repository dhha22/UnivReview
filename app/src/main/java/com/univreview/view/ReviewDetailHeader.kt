package com.univreview.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.dhha22.bindadapter.Item
import com.dhha22.bindadapter.ItemView
import com.univreview.R
import com.univreview.model.Review
import kotlinx.android.synthetic.main.review_detail_header.view.*


/**
 * Created by DavidHa on 2017. 9. 4..
 */
class ReviewDetailHeader(context: Context) : ItemView(context) {

    init {
        setContentView(R.layout.review_detail_header)
        setFullSpan()
    }

    override fun setData(data: Item?) {
        super.setData(data)
        if(data is Review){
            setContent(data.content)
            headerView.setUserData(data.user!!)
            headerView.setSubjectProfessor(data.subject!!.name, data.professor!!.name)
            headerView.setTimeTxt(data.createdAt!!)
            reviewRatingIndicatorView.setData(data)
            bottomView.setReview(data)
            bottomView.setLike(data.isLike)
            bottomView.setLikeCntTxt(data.likeCount)
            bottomView.setCommentCntTxt(data.commentCount)
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