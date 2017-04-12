package com.univreview.fragment.review;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.R;
import com.univreview.fragment.BaseWriteFragment;

import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 4. 12..
 */
public class ReviewReportFragment extends BaseWriteFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_review_report, container, false);
        ButterKnife.bind(this, view);

        return rootLayout;
    }
}
