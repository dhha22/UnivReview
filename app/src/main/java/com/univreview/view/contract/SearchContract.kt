package com.univreview.view.contract

import com.univreview.fragment.AbsListFragment

/**
 * Created by DavidHa on 2017. 8. 6..
 */
interface SearchContract {
    interface View : BaseView {
        fun setResult(page: Int)
        fun setStatus(status: AbsListFragment.Status)
    }

    fun searchUniversity(name: String, page: Int)
    fun searchDepartment(id: Long, name: String, page: Int)
    fun searchMajor(id: Long, name: String, page: Int)
    fun searchProfessor(departmentId: Long?, name: String, page: Int)
    fun searchSubject(majorId: Long?, name: String, page: Int)
    fun searchProfFromSubj(subjectId: Long, name: String, page: Int)
    fun searchSubjFromProf(profId: Long, page: Int)
    fun stopSearch()
}