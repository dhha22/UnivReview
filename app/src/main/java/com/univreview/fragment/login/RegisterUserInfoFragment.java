package com.univreview.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.R;
import com.univreview.model.Login;
import com.univreview.model.Register;

import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 11..
 */
public class RegisterUserInfoFragment extends Fragment {
    private Register register;

    public static RegisterUserInfoFragment newInstance(Register register){
        RegisterUserInfoFragment fragment = new RegisterUserInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("register", register);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        register = (Register) getArguments().getSerializable("register");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_user_info, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public void setData(Register register){

    }
}
