package com.univreview.fragment.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.model.BusProvider;
import com.univreview.model.Register;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DavidHa on 2017. 1. 11..
 */
public class RegisterUserInfoFragment extends Fragment {
    @BindView(R.id.profile_image) CircleImageView profileImage;
    @BindView(R.id.input_name) TextView inputName;
    @BindView(R.id.next_btn) Button nextBtn;
    private Register register;
    private Context context;


    public static RegisterUserInfoFragment newInstance(Register register){
        RegisterUserInfoFragment fragment = new RegisterUserInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_register_user_info, container, false);
        this.context = getContext();
        ButterKnife.bind(this, view);

        setData(register);
        return view;
    }

    public void setData(Register register) {
        if (register.profileUrl.length() > 0) {
            App.picasso.load(register.profileUrl)
                    .fit()
                    .into(profileImage);
        }
        inputName.setText(register.nickName);

        nextBtn.setOnClickListener(v -> Navigator.goRegisterUnivInfo(context, register));
    }


}
