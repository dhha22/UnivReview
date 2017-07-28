package com.univreview.view.contract;

import com.univreview.fragment.AbsListFragment;
import com.univreview.model.Professor;
import com.univreview.model.Subject;
import com.univreview.model.enumeration.ReviewSearchType;
import com.univreview.view.ReviewTotalScoreView;

import java.util.List;

/**
 * Created by DavidHa on 2017. 7. 14..
 */

public interface ReviewListContract {
    interface View{
        void setDialog(List<String> list);
        Long getSubjectId();
        Long getProfessorId();
        Long getUserId();
        void setResult(int page);
        void setStatus(AbsListFragment.Status status);
        ReviewTotalScoreView getReviewTotalScoreView();
        void setHeaderViewVisibility(boolean isVisibility);
        void setFilterName(String filterName);
    }

    void attachView(View view);
    void detachView();
    void loadReviewItem(ReviewSearchType type, int page);
    void searchProfessor(long subjectId);
    void searchSubject(long professorId);
    List<Subject> getSubjects();
    List<Professor> getProfessors();
}
