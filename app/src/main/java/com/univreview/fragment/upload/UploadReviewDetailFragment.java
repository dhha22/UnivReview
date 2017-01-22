package com.univreview.fragment.upload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.R;

import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class UploadReviewDetailFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_review_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
