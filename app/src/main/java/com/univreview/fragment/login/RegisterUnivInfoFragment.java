package com.univreview.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.R;

/**
 * Created by DavidHa on 2017. 1. 11..
 */
public class RegisterUnivInfoFragment extends Fragment {

    public static RegisterUnivInfoFragment newInstance(){
        RegisterUnivInfoFragment fragment = new RegisterUnivInfoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_univ_info, container, false);
        return view;
    }
}
