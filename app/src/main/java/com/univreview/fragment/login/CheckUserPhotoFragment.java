package com.univreview.fragment.login;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.otto.Subscribe;
import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.ActivityResultEvent;
import com.univreview.model.User;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.ImageUtil;
import com.univreview.util.Util;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 31..
 */
public class CheckUserPhotoFragment extends BaseFragment {
    private static final String CAMERA = "camera";
    private static final String ALBUM = "album";
    @BindView(R.id.check_image) ImageView checkImage;
    @BindView(R.id.reselect_btn) Button reselectBtn;
    @BindView(R.id.ok_btn) Button okBtn;
    private String type;
    private String path;
    private String uri;

    public static CheckUserPhotoFragment newInstance(String type, String path, String uri){
        CheckUserPhotoFragment fragment = new CheckUserPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("path", path);
        bundle.putString("uri", uri);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        path = getArguments().getString("path");
        uri = getArguments().getString("uri");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_check_user_photo, container, false);
        ButterKnife.bind(this, view);
        toolbar.setCancelBtnVisibility(true);
        if (CAMERA.equals(type)) {
            reselectBtn.setText("다시 찍기");
            reselectBtn.setOnClickListener(v -> Navigator.goPermissionChecker(context, "camera"));
        } else if (ALBUM.equals(type)) {
            reselectBtn.setText("앨범 가기");
            reselectBtn.setOnClickListener(v -> Navigator.goPermissionChecker(context, "album"));
        }

        Logger.v("path: " + path);

        if (path == null) {
            App.picasso.invalidate(ImageUtil.TEMP_IMAGE_URI.toString());
            App.picasso.load(ImageUtil.TEMP_IMAGE_URI.toString())
                    .fit()
                    .centerInside()
                    .into(checkImage);
        } else {
            App.picasso.load("file://" + path)
                    .fit()
                    .centerInside()
                    .into(checkImage);
        }
        okBtn.setOnClickListener(v -> upload());
        rootLayout.addView(view);
        return rootLayout;
    }

    private void upload() {
        Retro.instance.fileService(Uri.parse(uri), "file")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> progressDialog.show())
                .subscribe(result -> {
                    Logger.v("result: " + result);
                    User user = new User();
                    user.studentImageUrl = result.fileLocation;
                    Retro.instance.userService().postProfile(App.setAuthHeader(App.userToken), user, App.userId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doAfterTerminate(() -> {
                                progressDialog.dismiss();
                                Navigator.goUserAuthCompleted(context);
                                activity.finish();
                            })
                            .subscribe(data -> Logger.v("result: " + data), ErrorUtils::parseError);
                }, error -> {
                    progressDialog.dismiss();
                    ErrorUtils.parseError(error);
                });
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
            } else if (activityResultEvent.getRequestCode() == Navigator.CAMERA) {
                Logger.v("camera");
                uri = ImageUtil.TEMP_IMAGE_URI.toString();
                App.picasso.invalidate(uri);
                App.picasso.load(uri)
                        .fit()
                        .centerInside()
                        .into(checkImage);
            } else if (activityResultEvent.getRequestCode() == Navigator.ALBUM) {
                Logger.v("album");
                uri = activityResultEvent.getIntent().getData().toString();
                String albumPath = ImageUtil.getPath(activityResultEvent.getIntent().getData());
                App.picasso.load("file://" + albumPath)
                        .fit()
                        .centerInside()
                        .into(checkImage);
            }
        }
    }

}
