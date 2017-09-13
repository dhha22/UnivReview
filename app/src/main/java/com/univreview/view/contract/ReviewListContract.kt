package com.univreview.view.contract

import com.univreview.fragment.AbsListFragment
import com.univreview.listener.OnItemClickListener
import com.univreview.model.Review
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.view.ReviewTotalScoreView

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface ReviewListContract {
    interface View : BaseView {
        fun setDialog(list: List<String>, itemClickListener: OnItemClickListener)
        fun setResult(page: Int)
        fun setStatus(status: AbsListFragment.Status)
        fun setHeaderData(difficultyRateAvg: Float, assignmentRateAvg: Float, attendanceRateAvg: Float, gradeRate: Float, achievementRate: Float)
        fun setHeaderViewVisibility(isVisibility: Boolean)
        fun setFilterName(filterName: String)
    }

    fun loadReviewItem(type: ReviewSearchType, page: Int)
    fun searchProfessor(subjectId: Long)
    fun searchSubject(professorId: Long)
}