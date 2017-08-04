package com.univreview.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.squareup.otto.Subscribe;
import com.univreview.BuildConfig;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.model.ActivityResultEvent;
import com.univreview.util.ImageUtil;
import com.univreview.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 24..
 */
public class RegisterUserIdentityFragment extends BaseFragment{
    private static final String CAMERA = "camera";
    private static final String ALBUM = "album";
    @BindView(R.id.camera_btn) Button cameraBtn;
    @BindView(R.id.album_btn) Button albumBtn;

    public static RegisterUserIdentityFragment newInstance(){
        RegisterUserIdentityFragment fragment = new RegisterUserIdentityFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_register_user_identity, container, false);
        ButterKnife.bind(this, view);
        rootLayout.setBackgroundColor(Util.getColor(getContext(), R.color.colorPrimary));
        toolbar.setCancelBtnVisibility(true);
        toolbar.setTitleTxt("학교인증하기");
        cameraBtn.setOnClickListener(v -> Navigator.goPermissionChecker(getContext(), "camera"));
        albumBtn.setOnClickListener(v -> Navigator.goPermissionChecker(getContext(), "album"));
        rootLayout.addView(view);
        return rootLayout;
    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        if (activityResultEvent.getResultCode() == getActivity().RESULT_OK) {
            if (activityResultEvent.getRequestCode() == Navigator.PERMISSION_CHECKER) {
                String type = activityResultEvent.getIntent().getStringExtra("type");
                if ("camera".equals(type)) {
                    Navigator.goCamera(getContext());
                } else if ("album".equals(type)) {
                    Navigator.goAlbum(getContext());
                }
            } else if (activityResultEvent.getRequestCode() == Navigator.CAMERA) {
                String albumPath = ImageUtil.IMAGE_PATH + "tmp.jpg";
                if (BuildConfig.DEBUG) {
                    Util.toast(albumPath);
                }
                Navigator.goCheckUserPhoto(getContext(), CAMERA, albumPath);
                getActivity().finish();
            } else if (activityResultEvent.getRequestCode() == Navigator.ALBUM) {
                String albumPath = ImageUtil.getPath(activityResultEvent.getIntent().getData());
                if (BuildConfig.DEBUG) {
                    Util.toast(albumPath);
                }
                Navigator.goCheckUserPhoto(getContext(), ALBUM, albumPath);
                getActivity().finish();
            }
        }
    }
}
