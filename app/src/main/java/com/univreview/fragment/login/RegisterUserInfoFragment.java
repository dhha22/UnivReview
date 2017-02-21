package com.univreview.fragment.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.ActivityResultEvent;
import com.univreview.model.BusProvider;
import com.univreview.model.Register;
import com.univreview.util.ButtonState;
import com.univreview.util.ImageUtil;
import com.univreview.util.SimpleButtonState;
import com.univreview.util.Util;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DavidHa on 2017. 1. 11..
 */
public class RegisterUserInfoFragment extends BaseFragment {
    @BindView(R.id.profile_image_layout) RelativeLayout profileImageLayout;
    @BindView(R.id.profile_image) CircleImageView profileImage;
    @BindView(R.id.input_name) EditText inputName;
    @BindView(R.id.next_btn) Button nextBtn;
    private Register register;
    private SimpleButtonState buttonState;


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
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_register_user_info, container, false);
        ButterKnife.bind(this, view);
        toolbar.setBackBtnVisibility(true);
        init(register);
        setData(register);
        rootLayout.addView(view);
        return rootLayout;
    }

    private void init(Register register){
        buttonState = new SimpleButtonState(context, nextBtn);
        buttonState.setDrawable(R.drawable.rounded_rect, R.drawable.fill_rounded_rect);
        buttonState.setTxtColor(R.color.colorPrimaryDark, R.color.white);
        profileImageLayout.setOnClickListener(v -> Navigator.goAlbum(context));

        nextBtn.setOnClickListener(v -> {
            if (formVerification() && buttonState.getButtonState()) {
                Navigator.goRegisterUnivInfo(context, register);
            }
        });


    }


    public void setData(Register register) {
        if (register.profileUrl.length() > 0) {
            App.picasso.load(register.profileUrl)
                    .fit()
                    .into(profileImage);
        }
        inputName.setText(register.nickName);
        inputName.setSelection(register.nickName.length());
        buttonState.setButtonState(true);
    }

    private boolean formVerification() {
        if (inputName.getText().length() == 0) {
            Util.simpleMessageDialog(context, "이름을 입력해주시길 바랍니다.");
        } else {
            return true;
        }
        return false;
    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        if (activityResultEvent.getRequestCode() == Navigator.ALBUM) {
            String albumPath = new ImageUtil(context).getPath(activityResultEvent.getIntent().getData());
            Logger.v("album path: " + albumPath);
            register.profileUrl = "file://" + albumPath;
            setData(register);
        }

    }


}
