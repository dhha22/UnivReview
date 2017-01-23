package com.univreview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.adapter.LatestReviewAdapter;
import com.univreview.model.Review;
import com.univreview.view.LatestReviewItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class HomeFragment extends BaseFragment {
    @BindView(R.id.latest_culture_recycler_view) RecyclerView latestCultureRecyclerView;
    @BindView(R.id.latest_major_recycler_view) RecyclerView latestMajorRecyclerView;
    private LatestReviewAdapter cultureAdapter;
    private LatestReviewAdapter majorAdapter;
    private Context context;

    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        this.context = getContext();
        init();
        return view;
    }

    private void init(){
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






}
