package com.univreview.view.contract;

import com.univreview.adapter.contract.SearchAdapterContract;
import com.univreview.fragment.AbsListFragment;

/**
 * Created by DavidHa on 2017. 7. 15..
 */

public interface SearchContract {
    interface View{
        void setResult(int page);
        void setStatus(AbsListFragment.Status status);
    }

    void attachView(View view);
    void detachView();
    void setSubjectType(String type);
    void setSearchAdapterModel(SearchAdapterContract.Model model);
    void searchUniversity(String name, int page);
    void searchDepartment(long id, String name, int page);
    void searchMajor(long id, String name, int page);
    void searchProfessor(Long departmentId, String name, int page);
    void searchSubject(Long majorId, String name, int page);
    void searchProfFromSubj(long subjectId, String name, int page);
}
