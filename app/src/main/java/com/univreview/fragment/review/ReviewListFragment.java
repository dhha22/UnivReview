package com.univreview.fragment.review;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.view.UnivReviewRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 2. 21..
 */
public class ReviewListFragment extends BaseFragment {
    @BindView(android.R.id.list) UnivReviewRecyclerView recyclerView;

    public static ReviewListFragment newInstance(String type, long id) {
        ReviewListFragment fragment = new ReviewListFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);
        ButterKnife.bind(this, view);

        rootLayout.addView(view);
        return rootLayout;
    }
}
