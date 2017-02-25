package com.univreview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.univreview.model.UserModel;
import com.univreview.model.Setting;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.ImageUtil;
import com.univreview.view.SettingItemView;

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
    @BindView(R.id.profile_image_layout) RelativeLayout profileImageLayout;
    @BindView(R.id.profile_image) ImageView profileImage;
    @BindView(R.id.name_txt) TextView nameTxt;
    @BindView(R.id.department_txt) TextView departmentTxt;
    @BindView(R.id.major_txt) TextView majorTxt;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private MyPageAdapter adapter;
    private long userId;


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
        Observable.from(Arrays.asList(new Setting("My 리뷰"), new Setting("포인트")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> adapter.addItem(result), Logger::e);

        adapter.setOnItemClickListener((view, position) -> {
            switch (position) {
                case MY_REVIEW:
                    break;
                case POINT:
                    Navigator.goPointList(context);
                    break;
            }
        });
        profileImageLayout.setOnClickListener(v -> Navigator.goAlbum(context));
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
        App.picasso.load(userModel.user.studentImageUrl)
                .fit()
                .into(profileImage);

        //review count
        //point count
        adapter.clear();
        Observable.from(Arrays.asList(new Setting(userModel.review + "개"), new Setting(userModel.user.point + " point")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> adapter.addItem(result), Logger::e);

    }

    private class MyPageAdapter extends CustomAdapter{
        private List<String> titles = Arrays.asList("My 리뷰", "포인트");

        public MyPageAdapter(Context context) {
            super(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new SettingItemView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).v.setTitle(titles.get(position));
            ((ViewHolder) holder).v.setPreviewTxt(list.get(position).getName());
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{
            final SettingItemView v;
            public ViewHolder(View itemView) {
                super(itemView);
                v = (SettingItemView)itemView;
                v.setOnClickListener(view -> itemClickListener.onItemClick(v, getAdapterPosition()));
            }
        }
    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        if (activityResultEvent.getRequestCode() == Navigator.ALBUM) {
            String albumPath = new ImageUtil(context).getPath(activityResultEvent.getIntent().getData());
            Logger.v("album path: " + albumPath);
            //"file://" + albumPath;

        }

    }

    private void callProfileApi(Long userId) {
        if (userId == 0l) userId = null;
        Logger.v("user id: " + userId);
        Retro.instance.userService().getProfile(App.setAuthHeader(App.userToken))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> setUserData(result), ErrorUtils::parseError);
    }
}
