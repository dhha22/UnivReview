package com.univreview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.adapter.RecentRvAdapter;
import com.univreview.log.Logger;
import com.univreview.model.enumeration.ReviewSearchType;
import com.univreview.model.model_kotlin.Review;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class HomeFragment extends BaseFragment {
    private static final boolean DEFAULT_EXPAND_STATE = false;
    @BindView(R.id.app_bar_layout) AppBarLayout appBarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapse_btn) ImageButton collapseBtn;
    @BindView(R.id.subject_txt) TextView subjectTxt;
    @BindView(R.id.professor_txt) TextView professorTxt;
    @BindView(R.id.latest_culture_recycler_view) RecyclerView latestCultureRecyclerView;
    @BindView(R.id.latest_major_recycler_view) RecyclerView latestMajorRecyclerView;
    private RecentRvAdapter cultureAdapter;
    private RecentRvAdapter majorAdapter;
    private boolean isExpand = DEFAULT_EXPAND_STATE;

    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        Logger.v("init");
        subjectTxt.setOnClickListener(v -> setSubjectState(isExpand));
        professorTxt.setOnClickListener(v -> Navigator.goSearch(getContext(), ReviewSearchType.PROFESSOR, true));
        collapseBtn.setOnClickListener(v -> setCollapseBtnState());

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int height = appBarLayout.getHeight() - appBarLayout.getBottom();
            float value = (float) appBarLayout.getBottom() / appBarLayout.getHeight();

            if (value == 1) {
                collapseBtn.setVisibility(View.VISIBLE);
                collapseBtn.setAlpha(1f);
                professorTxt.setVisibility(View.VISIBLE);
            } else if (value <= 0.7) {
                collapseBtn.setAlpha((float) Math.log(value * 1.8));
                professorTxt.setVisibility(View.GONE);
            } else {
                professorTxt.setVisibility(View.VISIBLE);
                collapseBtn.setAlpha((float) Math.log(value * 1.8));
            }

            if (value < 0.55) {
                collapseBtn.setVisibility(View.GONE);
            }

            collapseBtn.setAlpha((float) Math.log(value * 1.8));
            if (height == 0) {
                setSearchFormData(true);
            } else {
                setSearchFormData(false);
            }
        });
        LinearLayoutManager cultureLayoutManager = new LinearLayoutManager(getContext());
        cultureLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager majorLayoutManager = new LinearLayoutManager(getContext());
        majorLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        cultureAdapter = new RecentRvAdapter(getContext());
        majorAdapter = new RecentRvAdapter(getContext());
        latestCultureRecyclerView.setLayoutManager(cultureLayoutManager);
        latestMajorRecyclerView.setLayoutManager(majorLayoutManager);
        latestCultureRecyclerView.setAdapter(cultureAdapter);
        latestMajorRecyclerView.setAdapter(majorAdapter);
        latestCultureRecyclerView.setNestedScrollingEnabled(false);
        latestMajorRecyclerView.setNestedScrollingEnabled(false);

    }

    private void setSubjectState(boolean isExpand) {
        if (!isExpand) {
            setSearchFormData(true);
            this.isExpand = true;
            appBarLayout.setExpanded(true, true);
        } else {
            Logger.v("go search");
            Navigator.goSearch(getContext(), ReviewSearchType.SUBJECT, true);
        }

    }

    private void setCollapseBtnState() {
       this. isExpand = false;
        appBarLayout.setExpanded(false, true);
        setSearchFormData(isExpand);
    }



    private void setSearchFormData(boolean isExpand) {
        Logger.v("isExpand: " + isExpand);
        if (isExpand) {
            this.isExpand = true;
            subjectTxt.setHint("과목명");
        } else {
            this.isExpand = false;
            subjectTxt.setHint("과목명·교수명");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        callRecentReviewApi();
    }


    private void callRecentReviewApi() {
        Retro.instance.reviewService().callRecentReview(App.setHeader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> cultureResponse(result.getData().getGeneralReviews(),
                        result.getData().getMasterReviews()), Logger::e);
    }

    private void cultureResponse(List<Review> cultures, List<Review> majors) {
        cultureAdapter.clear();
        majorAdapter.clear();
        Observable.from(cultures)
                .subscribe(result -> cultureAdapter.addItem(result), ErrorUtils::parseError);
        Observable.from(majors)
                .subscribe(result -> majorAdapter.addItem(result), ErrorUtils::parseError);
    }

}
