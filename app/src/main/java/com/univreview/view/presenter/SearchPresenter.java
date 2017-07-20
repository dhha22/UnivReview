package com.univreview.view.presenter;

import com.univreview.App;
import com.univreview.adapter.contract.SearchAdapterContract;
import com.univreview.fragment.AbsListFragment;
import com.univreview.log.Logger;
import com.univreview.model.AbstractDataProvider;
import com.univreview.model.SearchModel;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.view.contract.SearchContract;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 7. 20..
 */

public class SearchPresenter implements SearchContract {
    private static final int DEFAULT_PAGE = 1;
    private String subjectType;
    private SearchContract.View view;
    private SearchAdapterContract.Model searchAdapterModel;

    @Override
    public void searchUniversity(String name, int page) {
        Retro.instance.searchService().getUniversities(name, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(() -> {
                    if (page == DEFAULT_PAGE) searchAdapterModel.clear();
                })
                .subscribe(result -> response(result, page), this::errorResponse);
    }

    @Override
    public void searchDepartment(long id, String name, int page) {
        Retro.instance.searchService().getDepartments(id, name, subjectType, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(() -> {
                    if (page == DEFAULT_PAGE) searchAdapterModel.clear();
                })
                .subscribe(result -> response(result, page), this::errorResponse);
    }

    @Override
    public void searchMajor(long id, String name, int page) {
        Retro.instance.searchService().getMajors(App.universityId, id, name, subjectType, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(() -> {
                    if (page == DEFAULT_PAGE) searchAdapterModel.clear();
                })
                .subscribe(result -> response(result, page), this::errorResponse);
    }

    @Override
    public void searchProfessor(Long departmentId, String name, int page) {
        if(departmentId == 0) departmentId = null;
        Retro.instance.searchService().getProfessors(App.universityId, departmentId, name, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(() -> {
                    if (page == DEFAULT_PAGE) searchAdapterModel.clear();
                })
                .subscribe(result -> response(result, page), this::errorResponse);
    }

    @Override
    public void searchSubject(Long majorId, String name, int page) {
        if(majorId ==0) majorId = null;
        Logger.v("university id: " + App.universityId);
        Logger.v("major id: " + majorId);
        Retro.instance.searchService().getSubjects(App.universityId, majorId, name, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(() -> {
                    if (page == DEFAULT_PAGE) searchAdapterModel.clear();
                })
                .subscribe(result -> response(result, page), this::errorResponse);
    }

    private void response(SearchModel result, int page) {
        view.setResult(page);
        view.setStatus(AbsListFragment.Status.IDLE);
        Observable<AbstractDataProvider> observable = null;
        if (result.universities.size() > 0) {
            observable = Observable.from(result.universities);
        } else if (result.departments.size() > 0) {
            observable = Observable.from(result.departments);
        } else if (result.majors.size() > 0) {
            observable = Observable.from(result.majors);
        } else if (result.professors.size() > 0) {
            observable = Observable.from(result.professors);
        } else if (result.subjects.size() > 0) {
            observable = Observable.from(result.subjects);
        }

        if (observable != null) {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> searchAdapterModel.addItem(data), Logger::e);
        }
    }

    private void errorResponse(Throwable e){
        view.setStatus(AbsListFragment.Status.ERROR);
        ErrorUtils.parseError(e);
    }

    @Override
    public void setSearchAdapterModel(SearchAdapterContract.Model searchAdapterModel) {
        this.searchAdapterModel = searchAdapterModel;
    }

    @Override
    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    @Override
    public void attachView(View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }
}
