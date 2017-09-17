package com.univreview.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 3. 22..
 */
public class UserAuthCompletedFragment extends BaseFragment {
    @BindView(R.id.complete_btn) Button completeBtn;

    public static UserAuthCompletedFragment newInstance(){
        UserAuthCompletedFragment fragment = new UserAuthCompletedFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_user_auth_completed, container, false);
        ButterKnife.bind(this, view);
        toolbar.setVisibility(View.GONE);
        rootLayout.addView(view);
        completeBtn.setOnClickListener(v -> getActivity().finish());
        return rootLayout;
    }
}
