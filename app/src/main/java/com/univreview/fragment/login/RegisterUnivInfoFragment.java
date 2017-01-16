package com.univreview.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.univreview.R;
import com.univreview.model.Register;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 11..
 */
public class RegisterUnivInfoFragment extends Fragment {
    @BindView(R.id.student_btn) Button studentBtn;
    @BindView(R.id.professor_btn) Button professorBtn;
    @BindView(R.id.university_txt) TextView universityTxt;
    @BindView(R.id.department_txt) TextView departmentTxt;
    @BindView(R.id.major_txt) TextView majorTxt;

    private Register register;

    public static RegisterUnivInfoFragment newInstance(Register register){
        RegisterUnivInfoFragment fragment = new RegisterUnivInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("register", register);
        fragment.setArguments(bundle);
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
        View view = inflater.inflate(R.layout.fragment_register_univ_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setData(){

    }
}
