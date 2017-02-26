package com.univreview.fragment.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.ActivityResultEvent;
import com.univreview.model.Register;
import com.univreview.model.Token;
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
    private static final int UNIVERSITY = 0;
    private static final int DEPARTMENT = 1;
    private static final int MAJOR = 2;
    private static final int NEXT = 3;
    @BindView(R.id.student_btn) Button studentBtn;
    @BindView(R.id.professor_btn) Button professorBtn;
    @BindView(R.id.university_txt) TextView universityTxt;
    @BindView(R.id.department_txt) TextView departmentTxt;
    @BindView(R.id.major_layout) LinearLayout majorLayout;
    @BindView(R.id.major_txt) TextView majorTxt;
    @BindView(R.id.next_btn) Button nextBtn;
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
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_register_univ_info, container, false);
        ButterKnife.bind(this, view);
        rootLayout.setBackground(Util.getDrawable(context, R.drawable.cr_login_bg));
        toolbar.setBackBtnVisibility(true);
        init();
        rootLayout.addView(view);
        return rootLayout;
    }

    private void init() {
        buttonStateManager = new ButtonStateManager(context, new SimpleButtonState(studentBtn), new SimpleButtonState(professorBtn));
        buttonStateManager.setDrawable(R.drawable.rounded_white_rect, R.drawable.fill_rounded_rect);
        buttonStateManager.setTxtColor(R.color.white, R.color.white);

        buttonClicked(STUDENT);
        studentBtn.setOnClickListener(v -> buttonClicked(STUDENT));
        professorBtn.setOnClickListener(v -> buttonClicked(PROFESSOR));
    }

    private void buttonClicked(int state) {
        if (state == STUDENT) {
            majorLayout.setVisibility(View.VISIBLE);
            nextBtn.setText("확인");
            nextBtn.setOnClickListener(v -> {
                if (formVerification(STUDENT, NEXT)) callTempTokenApi(register);
            });

        } else if (state == PROFESSOR) {
            nextBtn.setText("교수님 인증하기");
            nextBtn.setOnClickListener(v -> {
                if (formVerification(PROFESSOR, NEXT)) Navigator.goRegisterUserIdentity(context);
            });
            majorLayout.setVisibility(View.GONE);
        }

        universityTxt.setOnClickListener(v -> {
            if (formVerification(STUDENT, UNIVERSITY)) {
                Navigator.goSearch(getActivity(), "university", universityTxt.getText().toString(), false);
            }
        });

        departmentTxt.setOnClickListener(v -> {
            if (formVerification(STUDENT, DEPARTMENT)) {
                Navigator.goSearch(getActivity(), "department",
                        register.universityId, departmentTxt.getText().toString(), false);
            }
        });

        majorTxt.setOnClickListener(v -> {
            if (formVerification(STUDENT, MAJOR)) {
                Logger.v("register department id: " + register.departmentId);
                Navigator.goSearch(getActivity(), "major",
                        register.departmentId, majorTxt.getText().toString(), false);
            }
        });

        buttonStateManager.clickButton(state);
    }

    private boolean formVerification(int state, int clickPosition) {
        if (universityTxt.getText().length() == 0) {
            return showAlertDialog(state, clickPosition, UNIVERSITY);
        } else if (departmentTxt.getText().length() == 0) {
            return showAlertDialog(state, clickPosition, DEPARTMENT);
        } else if (majorTxt.getText().length() == 0) {
            return showAlertDialog(state, clickPosition, MAJOR);
        }else{
            return true;
        }
    }

    private boolean showAlertDialog(int state, int clickPosition, int position) {
        if (clickPosition <= position) {
            return true;
        } else {
            if (position == UNIVERSITY) {
                Util.simpleMessageDialog(context, "대학을 선택해주세요.");
            } else if (position == DEPARTMENT) {
                Util.simpleMessageDialog(context, "학과군을 선택해주세요.");
            } else if (position == MAJOR && state != PROFESSOR) {
                Util.simpleMessageDialog(context, "전공을 선택해주세요.");
            }
            return false;
        }
    }


    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        if (activityResultEvent.getResultCode() == getActivity().RESULT_OK) {
            if (activityResultEvent.getRequestCode() == Navigator.SEARCH) {
                Intent data = activityResultEvent.getIntent();
                long id = data.getLongExtra("id", 0);
                String name = data.getStringExtra("name");
                String type = data.getStringExtra("type");
                Logger.v("on activity result: " + type);
                if ("university".equals(type)) {
                    universityTxt.setText(name);
                    departmentTxt.setText(null);
                    majorTxt.setText(null);
                    register.universityId = id;
                    register.departmentId = null;
                    register.majorId = null;
                } else if ("department".equals(type)) {
                    departmentTxt.setText(name);
                    majorTxt.setText(null);
                    register.departmentId = id;
                    register.majorId = null;
                } else if ("major".equals(type)) {
                    majorTxt.setText(name);
                    register.majorId = id;
                }

            }
        }
    }

    //api
    private void callTempTokenApi(Register register){
        Retro.instance.tokenService().tempToken(App.setAuthHeader(""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> callRegisterApi(register, result), error -> Logger.e(error));
    }

    private void callRegisterApi(Register register, Token token){
        Logger.v("userToken: " + token);
        Logger.v("register : " + register);
        Retro.instance.userService().register(App.setAuthHeader(token.getToken()), register)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> Logger.v(result), error -> Logger.e(error));
    }




}
