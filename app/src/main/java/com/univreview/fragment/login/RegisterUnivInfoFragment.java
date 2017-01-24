package com.univreview.fragment.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.ActivityResultEvent;
import com.univreview.model.BusProvider;
import com.univreview.model.Register;
import com.univreview.network.Retro;
import com.univreview.util.ButtonStateManager;
import com.univreview.util.SimpleButtonState;
import com.univreview.util.Util;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 11..
 */
public class RegisterUnivInfoFragment extends BaseFragment {
    private static final int STUDENT = 0;
    private static final int PROFESSOR = 1;
    @BindView(R.id.student_btn) Button studentBtn;
    @BindView(R.id.professor_btn) Button professorBtn;
    @BindView(R.id.university_txt) TextView universityTxt;
    @BindView(R.id.department_txt) TextView departmentTxt;
    @BindView(R.id.major_layout) LinearLayout majorLayout;
    @BindView(R.id.major_txt) TextView majorTxt;
    @BindView(R.id.next_btn) Button nextBtn;
    private ButtonStateManager buttonStateManager;
    private Register register;
    private Context context;

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
        this.context = getContext();
        buttonStateManager = new ButtonStateManager(Arrays.asList(new SimpleButtonState(studentBtn), new SimpleButtonState(professorBtn)));

        studentBtn.setOnClickListener(v -> buttonClicked(STUDENT));
        professorBtn.setOnClickListener(v -> buttonClicked(PROFESSOR));
        return view;
    }

    private void buttonClicked(int state) {
        if (state == STUDENT) {
            majorLayout.setVisibility(View.VISIBLE);
            nextBtn.setText("확인");
            nextBtn.setOnClickListener(v -> {
                if (formVerification(state)) callRegisterApi(register);
            });

        } else if (state == PROFESSOR) {
            nextBtn.setText("교수님 인증하기");
            nextBtn.setOnClickListener(v -> {
                if (formVerification(state)) Navigator.goRegisterUserIdentity(context);
            });
            majorLayout.setVisibility(View.GONE);
        }

        departmentTxt.setOnClickListener(v -> {
            if (formVerification(state)) {
                Navigator.goSearch(getActivity(), "department",
                        register.universityId, departmentTxt.getText().toString());
            }
        });

        majorTxt.setOnClickListener(v -> {
            if (formVerification(state)) {
                Navigator.goSearch(getActivity(), "major",
                        register.departmentId, majorTxt.getText().toString());
            }
        });

        buttonStateManager.clickButton(state);
    }

    private boolean formVerification(int state) {
        if (universityTxt.getText().length() == 0) {
            Util.simpleMessageDialog(context, "대학을 선택해주세요.");
        } else if (departmentTxt.getText().length() == 0) {
            Util.simpleMessageDialog(context, "학과군을 선택해주세요.");
        } else if (majorTxt.getText().length() == 0) {
            Util.simpleMessageDialog(context, "전공을 선택해주세요.");
        } else if (universityTxt.getText().length() > 0 && departmentTxt.getText().length() > 0) {
            if (state == PROFESSOR) {
                return true;
            } else if (state == STUDENT && majorTxt.getText().length() > 0) {
                return true;
            }
        }
        return false;
    }



    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        if (activityResultEvent.getResultCode() == getActivity().RESULT_OK) {
            if (activityResultEvent.getRequestCode() == Navigator.SEARCH) {
                Intent data = activityResultEvent.getData();
                int id = data.getIntExtra("id", 0);
                String name = data.getStringExtra("name");
                String type = data.getStringExtra("type");
                if ("department".equals(type)) {
                    departmentTxt.setText(name);
                    register.departmentId = id;
                } else if ("major".equals(type)) {
                    majorTxt.setText(name);
                    register.majorId = id;
                }

            }
        }
    }

    //api
    private void callRegisterApi(Register register){
        Retro.instance.userService().register(register)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


}
