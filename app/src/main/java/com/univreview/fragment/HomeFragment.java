package com.univreview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.R;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class HomeFragment extends BaseFragment {
    private RecyclerView latestReview;
    private RecyclerView latestDepartmentReview;

    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    /*private class LatestDepartmentReviewAdapter extends CustomAdapter<>{

    }*/


}
