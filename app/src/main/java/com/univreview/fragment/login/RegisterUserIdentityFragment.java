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
        toolbar.setCancelBtnVisibility(true);
        cameraBtn.setOnClickListener(v -> Navigator.goCamera(context));
        albumBtn.setOnClickListener(v -> Navigator.goAlbum(context));
        rootLayout.addView(view);
        return rootLayout;
    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        if (activityResultEvent.getResultCode() == getActivity().RESULT_OK) {
            if (activityResultEvent.getRequestCode() == Navigator.CAMERA) {
                Navigator.goCheckUserPhoto(context, CAMERA, null);
            } else if (activityResultEvent.getRequestCode() == Navigator.ALBUM) {
                String albumPath = new ImageUtil(context).getPath(activityResultEvent.getIntent().getData());
                Navigator.goCheckUserPhoto(context, ALBUM, albumPath);
            }
            getActivity().finish();
        }
    }
}
