package com.univreview.view.presenter;

import com.univreview.App;
import com.univreview.adapter.contract.ReviewListAdapterContract;
import com.univreview.fragment.AbsListFragment;
import com.univreview.log.Logger;
import com.univreview.model.Professor;
import com.univreview.model.ReviewListModel;
import com.univreview.model.SearchModel;
import com.univreview.model.Subject;
import com.univreview.model.enumeration.ReviewSearchType;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.view.contract.ReviewListContract;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 7. 14..
 */

public class ReviewListPresenter implements ReviewListContract {
    private static final int DEFAULT_PAGE = 1;
    private ReviewListContract.View view;
    private ReviewListAdapterContract.Model adapterModel;
    private ReviewListAdapterContract.View adapterView;
    private SearchModel searchModel;

    @Override
    public void loadReviewItem(ReviewSearchType type, int page) {
        Observable<ReviewListModel> observable;
        if (type.equals(ReviewSearchType.MY_REVIEW)) {
            observable = Retro.instance.reviewService().getMyReviews(App.setAuthHeader(App.userToken), page);

        } else {
            observable = Retro.instance.reviewService().getReviews(App.setAuthHeader(App.userToken),
                    view.getSubjectId(), view.getProfessorId(), view.getUserId(), page);
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    if (page == DEFAULT_PAGE) adapterModel.clearItem();
                })
                .subscribe(result -> response(type, page, result), error -> errorResponse(page, error));
    }

    private void response(ReviewSearchType type, int page, ReviewListModel reviewListModel) {
        view.setResult(page);
        view.setStatus(AbsListFragment.Status.IDLE);
        Logger.v("result size: " + reviewListModel.reviews.size());
        Logger.v("review: " + reviewListModel.totalAverageRates + " " + reviewListModel.getReviewAverage());
        if(!type.equals(ReviewSearchType.MY_REVIEW)) {
            view.getReviewTotalScoreView().setData(reviewListModel.totalAverageRates, reviewListModel.getReviewAverage());
        }
        Observable.from(reviewListModel.reviews)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> adapterModel.addItem(result), Logger::e);
    }

    private void errorResponse(int page, Throwable e) {
        view.setStatus(AbsListFragment.Status.ERROR);
        if (page == DEFAULT_PAGE){
            view.setHeaderViewVisibility(false);
            adapterModel.clearItem();
        }
        ErrorUtils.parseError(e);
    }

    @Override
    public void searchProfessor(long subjectId) {
        Logger.v("subject id: " + subjectId);
        Retro.instance.searchService().getProfessorFromSubject(subjectId, DEFAULT_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::searchResponse,  ErrorUtils::parseError);
    }

    @Override
    public void searchSubject(long professorId) {
        Logger.v("professor id: " + professorId);
        Retro.instance.searchService().getSubjectFromProfessor(professorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::searchResponse,  ErrorUtils::parseError);
    }

    private void searchResponse(SearchModel result) {
        this.searchModel = result;
        Observable<String> observable = null;
        if (result.subjects.size() > 0) {
            observable = Observable.from(result.subjects)
                    .map(Subject::getName);

        } else if (result.professors.size() > 0) {
            observable = Observable.from(result.professors)
                    .map(Professor::getName);
        }

        if (observable != null) {
            observable.toList()
                    .subscribe(name -> view.setDialog(name), Logger::e);
        }
    }

    @Override
    public List<Subject> getSubjects() {
        if (searchModel != null) {
            return searchModel.subjects;
        }
        return null;
    }

    @Override
    public List<Professor> getProfessors() {
        if (searchModel != null) {
            return searchModel.professors;
        }
        return null;
    }

    @Override
    public void attachView(View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void setAdapterView(ReviewListAdapterContract.View adapterView) {
        this.adapterView = adapterView;
    }

    public void setAdapterModel(ReviewListAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }
}
