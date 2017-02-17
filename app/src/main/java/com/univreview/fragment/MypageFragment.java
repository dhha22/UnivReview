package com.univreview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.univreview.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class MypageFragment extends BaseFragment {
    @BindView(R.id.name_txt) TextView nameTxt;
    @BindView(R.id.department_txt) TextView departmentTxt;
    public static MypageFragment newInstance(){
        MypageFragment fragment = new MypageFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        ButterKnife.bind(this, view);
        rootLayout.addView(view);
        return rootLayout;
    }
}
