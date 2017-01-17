package com.univreview.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.univreview.R;
import com.univreview.model.Register;
import com.univreview.util.ButtonStateManager;
import com.univreview.util.SimpleButtonState;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 11..
 */
public class RegisterUnivInfoFragment extends Fragment {
    private static final int STUDENT = 0;
    private static final int PROFESSOR = 1;
    @BindView(R.id.student_btn) Button studentBtn;
    @BindView(R.id.professor_btn) Button professorBtn;
    @BindView(R.id.university_txt) TextView universityTxt;
    @BindView(R.id.department_txt) TextView departmentTxt;
    @BindView(R.id.major_layout) LinearLayout majorLayout;
    @BindView(R.id.major_txt) TextView majorTxt;
    private ButtonStateManager buttonStateManager;
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
        buttonStateManager = new ButtonStateManager(Arrays.asList(new SimpleButtonState(studentBtn), new SimpleButtonState(professorBtn)));
        studentBtn.setOnClickListener(v -> buttonClicked(STUDENT));
        professorBtn.setOnClickListener(v -> buttonClicked(PROFESSOR));
        return view;
    }

    private void buttonClicked(int state) {
        if (state == STUDENT) {
            majorLayout.setVisibility(View.VISIBLE);
        } else if (state == PROFESSOR) {
            majorLayout.setVisibility(View.GONE);
        }
        buttonStateManager.clickButton(state);
    }

    public void setData(){

    }
}
