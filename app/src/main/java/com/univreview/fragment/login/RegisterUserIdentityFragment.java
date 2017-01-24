package com.univreview.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.univreview.R;
import com.univreview.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 24..
 */
public class RegisterUserIdentityFragment extends BaseFragment{
    @BindView(R.id.camera_btn) Button cameraBtn;
    @BindView(R.id.album_btn) Button albumBtn;

    public static RegisterUserIdentityFragment newInstance(){
        RegisterUserIdentityFragment fragment = new RegisterUserIdentityFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_user_identity, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
