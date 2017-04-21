package com.univreview.fragment.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.squareup.otto.Subscribe;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
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
        rootLayout.setBackgroundColor(Util.getColor(context, R.color.colorPrimary));
        toolbar.setCancelBtnVisibility(true);
        toolbar.setTitleTxt("학교인증하기");
        cameraBtn.setOnClickListener(v -> Navigator.goPermissionChecker(context, "camera"));
        albumBtn.setOnClickListener(v -> Navigator.goPermissionChecker(context, "album"));
        rootLayout.addView(view);
        return rootLayout;
    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        if (activityResultEvent.getResultCode() == getActivity().RESULT_OK) {
            if (activityResultEvent.getRequestCode() == Navigator.PERMISSION_CHECKER) {
                String type = activityResultEvent.getIntent().getStringExtra("type");
                if ("camera".equals(type)) {
                    Navigator.goCamera(context);
                } else if ("album".equals(type)) {
                    Navigator.goAlbum(context);
                }
            }else if (activityResultEvent.getRequestCode() == Navigator.CAMERA) {
                Navigator.goCheckUserPhoto(context, CAMERA, null, ImageUtil.TEMP_IMAGE_URI.toString());
                activity.finish();
            } else if (activityResultEvent.getRequestCode() == Navigator.ALBUM) {
                String albumPath = ImageUtil.getPath(activityResultEvent.getIntent().getData());
                Navigator.goCheckUserPhoto(context, ALBUM, albumPath, activityResultEvent.getIntent().getData().toString());
                activity.finish();
            }
        }
    }
}
