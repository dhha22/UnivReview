package com.univreview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.activity.BaseActivity;
import com.univreview.listener.OnBackPressedListener;

/**
 * Created by DavidHa on 2017. 4. 1..
 */
public class BaseWriteFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((BaseActivity) getActivity()).setOnBackPressedListener(onBackPressedListener);
        return rootLayout;
    }

    private OnBackPressedListener onBackPressedListener = () -> {
        new AlertDialog.Builder(getContext())
                .setMessage("작성을 취소하시겠습니까?")
                .setPositiveButton("예", (dialog, i) -> {
                    ((BaseActivity) getActivity()).setOnBackPressedListener(null);
                    getActivity().onBackPressed();
                })
                .setNegativeButton("아니오", null)
                .setCancelable(false)
                .show();
    };


}
