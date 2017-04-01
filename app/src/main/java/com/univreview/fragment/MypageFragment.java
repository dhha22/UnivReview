package com.univreview.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.log.Logger;
import com.univreview.model.AbstractDataProvider;
import com.univreview.model.ActivityResultEvent;
import com.univreview.model.User;
import com.univreview.model.UserModel;
import com.univreview.model.Setting;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.ImageUtil;
import com.univreview.util.Util;
import com.univreview.view.SettingItemView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class MypageFragment extends BaseFragment {
    private static final int MY_REVIEW = 0;
    private static final int POINT = 1;
    private static final int USER_IDENTIFY = 2;
    @BindView(R.id.profile_image_layout) RelativeLayout profileImageLayout;
    @BindView(R.id.profile_image) ImageView profileImage;
    @BindView(R.id.name_txt) TextView nameTxt;
    @BindView(R.id.department_txt) TextView departmentTxt;
    @BindView(R.id.major_txt) TextView majorTxt;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.setting_btn) ImageButton settingBtn;
    private MyPageAdapter adapter;
    private long userId;
    private List<Setting> settings = Arrays.asList(new Setting(0, "My 리뷰"), new Setting(1, "포인트"), new Setting(2, "학생 인증"));


    public static MypageFragment newInstance(long userId) {
        MypageFragment fragment = new MypageFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("userId", userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getLong("userId");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        ButterKnife.bind(this, view);
        toolbar.setVisibility(View.GONE);
        rootLayout.addView(view);
        init();
        return rootLayout;
    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyPageAdapter(context);
        recyclerView.setAdapter(adapter);
        Observable.from(settings)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> adapter.addItem(result), Logger::e);

        profileImageLayout.setOnClickListener(v -> Navigator.goPermissionChecker(context, "album"));
        settingBtn.setOnClickListener(v -> Navigator.goSetting(context));
        adapter.setOnItemClickListener((view, position) -> {
            switch (position) {
                case MY_REVIEW:
                    Navigator.goReviewList(context, "myReview", App.userId, "내 리뷰");
                    break;
                case POINT:
                    if (adapter.getItem(position) != null) {
                        String name = ((Setting) adapter.getItem(position)).previewStr;
                        int index = name.indexOf("point") - 1;
                        Navigator.goPointList(context, Integer.parseInt(name.substring(0, index)));
                    } else {
                        Navigator.goPointList(context, 0);
                    }
                    break;
                case USER_IDENTIFY:
                    Navigator.goRegisterUserIdentity(context);
                    break;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        callProfileApi(userId);
    }

    public void setUserData(UserModel userModel){
        Logger.v(userModel.toString());
        nameTxt.setText(userModel.user.name);
        departmentTxt.setText(userModel.user.department.getName());
        majorTxt.setText(userModel.user.major.getName());
        Util.setProfileImage(userModel.user.profileImageUrl, profileImage);

        adapter.clear();
        Observable.from(settings)
                .map(setting -> {
                    switch ((int) setting.id) {
                        case 0:
                            setting.previewStr = userModel.review + "개";
                            break;
                        case 1:
                            setting.previewStr = userModel.user.point + " point";
                            break;
                    }
                    return setting;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> adapter.addItem(result), Logger::e);
    }

    private class MyPageAdapter extends CustomAdapter{

        public MyPageAdapter(Context context) {
            super(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new SettingItemView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).v.setTitle(list.get(position).getName());
            ((ViewHolder) holder).v.setPreviewTxt(((Setting) list.get(position)).previewStr);
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {
            final SettingItemView v;

            public ViewHolder(View itemView) {
                super(itemView);
                v = (SettingItemView) itemView;
                v.setOnClickListener(view -> itemClickListener.onItemClick(v, getAdapterPosition()));
            }
        }
    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        Logger.v("on activity result");
        if(activityResultEvent.getResultCode() == activity.RESULT_OK) {
            if(activityResultEvent.getRequestCode() == Navigator.PERMISSION_CHECKER){
               Navigator.goAlbum(context);
            }else if (activityResultEvent.getRequestCode() == Navigator.ALBUM) {
                String albumPath = ImageUtil.getPath(activityResultEvent.getIntent().getData());
                Logger.v("album path: " + albumPath);
                callFileUploadApi(activityResultEvent.getIntent().getData());
            }
        }
    }

    //api

    private void callProfileApi(Long userId) {
        if (userId == 0l) userId = null;
        Logger.v("user id: " + userId);
        Retro.instance.userService().getProfile(App.setAuthHeader(App.userToken))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setUserData, ErrorUtils::parseError);
    }

    private void callFileUploadApi(Uri uploadUri) {
        Retro.instance.fileService(uploadUri, "profile")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> callUserUpdateApi(result.fileLocation), Logger::e);
    }

    private void callUserUpdateApi(String profileUrl) {
        Logger.v("file location: " + profileUrl);
        User user = new User();
        user.profileImageUrl = profileUrl;
        Logger.v("post user model: " + user);
        Retro.instance.userService().postProfile(App.setAuthHeader(App.userToken), user, App.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> Util.setProfileImage(result.user.profileImageUrl, profileImage), Logger::e);
    }
}
