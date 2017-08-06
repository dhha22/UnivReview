package com.univreview.view.contract

import com.univreview.fragment.AbsListFragment
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.view.ReviewTotalScoreView

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface ReviewListContract {
    interface View {
        fun setDialog(list: List<String>)
        val subjectId: Long?
        val professorId: Long?
        val userId: Long?
        fun setResult(page: Int)
        fun setStatus(status: AbsListFragment.Status)
        val reviewTotalScoreView: ReviewTotalScoreView
        fun setHeaderViewVisibility(isVisibility: Boolean)
        fun setFilterName(filterName: String)
    }

     fun loadReviewItem(type: ReviewSearchType, page: Int)
     fun searchProfessor(subjectId: Long)
     fun searchSubject(professorId: Long)
}