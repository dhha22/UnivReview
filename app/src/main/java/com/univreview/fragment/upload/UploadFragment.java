package com.univreview.fragment.upload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.R;
import com.univreview.fragment.BaseFragment;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class UploadFragment extends BaseFragment {
    public static UploadFragment newInstance(){
        UploadFragment fragment = new UploadFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        return view;
    }
}
