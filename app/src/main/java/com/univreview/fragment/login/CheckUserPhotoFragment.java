package com.univreview.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.univreview.App;
import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.util.ImageUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 31..
 */
public class CheckUserPhotoFragment extends BaseFragment {
    private static final String CAMERA = "camera";
    private static final String ALBUM = "album";
    @BindView(R.id.check_image) ImageView checkImage;
    @BindView(R.id.camera_btn) Button cameraBtn;
    @BindView(R.id.ok_btn) Button okBtn;
    private String type;
    private String path;

    public static CheckUserPhotoFragment newInstance(String type, String path){
        CheckUserPhotoFragment fragment = new CheckUserPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("path", path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        path = getArguments().getString("path");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_check_user_photo, container, false);
        ButterKnife.bind(this, view);
        toolbar.setCancelBtnVisibility(true);
        if (CAMERA.equals(type)) {
            cameraBtn.setVisibility(View.VISIBLE);
        } else if (ALBUM.equals(type)) {
            cameraBtn.setVisibility(View.GONE);
        }

        Logger.v("path: " + path);

        if (path == null) {
            App.picasso.load(ImageUtil.TEMP_IMAGE_URI.toString())
                    .into(checkImage);
        } else {
            App.picasso.load("file://" + path)
                    .into(checkImage);
        }
        rootLayout.addView(view);
        return rootLayout;
    }


}
