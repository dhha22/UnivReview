package com.univreview.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.squareup.otto.Subscribe;
import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.ActivityResultEvent;
import com.univreview.model.Register;
import com.univreview.util.ImageUtil;
import com.univreview.util.SimpleButtonState;
import com.univreview.util.Util;

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
        bundle.putParcelable("register", register);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        register = getArguments().getParcelable("register");
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_register_user_info, container, false);
        ButterKnife.bind(this, view);
        setToolbarTransparent();
        toolbar.setBackBtnVisibility(true);
        rootLayout.setBackground(Util.getDrawable(context, R.drawable.cr_login_bg));
        init(register);
        setData(register);
        rootLayout.addView(view);
        return rootLayout;
    }

    private void init(Register register){
        buttonState = new SimpleButtonState(context, nextBtn);
        buttonState.setDrawable(R.drawable.rounded_white_rect, R.drawable.fill_rounded_primary_rect);
        buttonState.setTxtColor(R.color.white, R.color.white);
        profileImageLayout.setOnClickListener(v -> Navigator.goPermissionChecker(context, "album"));

        nextBtn.setOnClickListener(v -> {
            if (formVerification() && buttonState.getButtonState()) {
                Navigator.goRegisterUnivInfo(context, register);
            }
        });
        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==0){
                    buttonState.setButtonState(false);
                }else{
                    buttonState.setButtonState(true);
                }
            }
        });


    }


    public void setData(Register register) {
        Util.setProfileImage(register.profileUrl, profileImage);
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
        if (activityResultEvent.getResultCode() == activity.RESULT_OK) {
            if (activityResultEvent.getRequestCode() == Navigator.PERMISSION_CHECKER) {
                Navigator.goAlbum(context);
            } else if (activityResultEvent.getRequestCode() == Navigator.ALBUM) {
                String albumPath = ImageUtil.getPath(activityResultEvent.getIntent().getData());
                Logger.v("album path: " + albumPath);
                register.profileUrl = "file://" + albumPath;
                register.profileUri = activityResultEvent.getIntent().getData();
                setData(register);
            }
        }

    }


}
