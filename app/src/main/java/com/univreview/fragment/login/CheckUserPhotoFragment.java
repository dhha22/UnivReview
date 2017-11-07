package com.univreview.fragment.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.ActivityResultEvent;
import com.univreview.util.ImageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 31..
 */
public class CheckUserPhotoFragment extends BaseFragment {
    private static final String CAMERA = "camera";
    @BindView(R.id.checkImage) ImageView checkImage;
    @BindView(R.id.reselect_btn) Button reselectBtn;
    @BindView(R.id.ok_btn) Button okBtn;
    private String type;
    private String path;

    public static CheckUserPhotoFragment newInstance(String type, String path) {
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
        toolbar.setTitleTxt("학생증 확인");
        toolbar.setCancelToolbarStyle();
        //reselectBtn.setOnClickListener();
        setCheckImage(path);
        okBtn.setOnClickListener(v -> upload());
        rootLayout.addView(view);
        return rootLayout;
    }

    private void upload() {
        Logger.v("upload path: " + path);
       /* Retro.instance.fileService(path, "studentCard")
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    Navigator.goUserAuthCompleted(getContext());
                    getActivity().finish();
                })
                .subscribe(result -> Logger.v("result: " + result), error -> Logger.e(error));*/
    }


    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        if (activityResultEvent.getResultCode() == Activity.RESULT_OK) {
            if (activityResultEvent.getRequestCode() == Navigator.PERMISSION_CHECKER) {
                String type = activityResultEvent.getIntent().getStringExtra("type");
                if ("camera".equals(type)) {
                    Navigator.goCamera(getContext());
                }
            } else if (activityResultEvent.getRequestCode() == Navigator.CAMERA) {
                path = ImageUtil.IMAGE_PATH + "tmp.jpg";
                setCheckImage(path);
            }
        }
    }

    private void setCheckImage(String path) {
        Logger.v("path: " + path);
        App.picasso.invalidate("file://" + path);
        App.picasso.load("file://" + path)
                .fit()
                .centerCrop()
                .into(checkImage);
    }

}
