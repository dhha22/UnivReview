package com.univreview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.adapter.LatestReviewAdapter;
import com.univreview.log.Logger;
import com.univreview.model.Review;
import com.univreview.view.LatestReviewItemView;
import com.univreview.widget.DisableableCoordinatorLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class HomeFragment extends BaseFragment {
    @BindView(R.id.coordinator_layout) DisableableCoordinatorLayout coordinatorLayout;
    @BindView(R.id.app_bar_layout) AppBarLayout appBarLayout;
    @BindView(R.id.subject_txt) TextView subjectTxt;
    @BindView(R.id.major_txt) TextView majorTxt;
    @BindView(R.id.professor_txt) TextView professorTxt;
    @BindView(R.id.latest_culture_recycler_view) RecyclerView latestCultureRecyclerView;
    @BindView(R.id.latest_major_recycler_view) RecyclerView latestMajorRecyclerView;
    private LatestReviewAdapter cultureAdapter;
    private LatestReviewAdapter majorAdapter;
    private boolean isExpand = false;
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

    private void init(){
        coordinatorLayout.setPassScrolling(false);
        subjectTxt.setOnClickListener(v ->expand(isExpand));

        LinearLayoutManager cultureLayoutManager = new LinearLayoutManager(context);
        cultureLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager majorLayoutManager = new LinearLayoutManager(context);
        majorLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        cultureAdapter = new LatestReviewAdapter(context);
        majorAdapter = new LatestReviewAdapter(context);
        latestCultureRecyclerView.setLayoutManager(cultureLayoutManager);
        latestMajorRecyclerView.setLayoutManager(majorLayoutManager);
        latestCultureRecyclerView.setAdapter(cultureAdapter);
        latestMajorRecyclerView.setAdapter(majorAdapter);
    }


    private void expand(boolean isExpand) {
        Logger.v("isExpand: " + isExpand);
        if (isExpand) {
            this.isExpand = false;
            majorTxt.setVisibility(View.INVISIBLE);
            professorTxt.setVisibility(View.INVISIBLE);
            appBarLayout.setExpanded(false, true);
            subjectTxt.setHint("과목명·교수명·학과명");

        } else {
            this.isExpand = true;
            majorTxt.setVisibility(View.VISIBLE);
            professorTxt.setVisibility(View.VISIBLE);
            appBarLayout.setExpanded(true, true);
            subjectTxt.setHint("과목명");
        }
    }



}
