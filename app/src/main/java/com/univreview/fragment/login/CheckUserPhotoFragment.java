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
            reselectBtn.setText("다시 찍기");
            reselectBtn.setOnClickListener(v -> Navigator.goPermissionChecker(context, "camera"));
        } else if (ALBUM.equals(type)) {
            reselectBtn.setText("앨범 가기");
            reselectBtn.setOnClickListener(v -> Navigator.goPermissionChecker(context, "album"));
        }

        setCheckImage(path);
        okBtn.setOnClickListener(v -> upload());
        rootLayout.addView(view);
        return rootLayout;
    }

    private void upload() {
        Retro.instance.fileService(path, "file")
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
            } else if (activityResultEvent.getRequestCode() == Navigator.CAMERA){
                path = ImageUtil.IMAGE_PATH + "tmp.jpg";
                setCheckImage(path);
            }else if(activityResultEvent.getRequestCode() == Navigator.ALBUM){
                path = ImageUtil.getPath(activityResultEvent.getIntent().getData());
                setCheckImage(path);
            }
        }
    }

    private void setCheckImage(String path){
        Logger.v("path: " + path);
        App.picasso.invalidate("file://" + path);
        App.picasso.load("file://" + path)
                .fit()
                .centerInside()
                .into(checkImage);
    }

}
