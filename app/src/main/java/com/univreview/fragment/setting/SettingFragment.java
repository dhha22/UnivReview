package com.univreview.fragment.setting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.App;
import com.univreview.BuildConfig;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.Setting;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.Util;
import com.univreview.view.SettingItemView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 3. 6..
 */
public class SettingFragment extends BaseFragment {
    private static final int VERSION = 0;
    private static final int NOTIFICATION = 1;
    private static final int LOGOUT = 2;
    private static final int USER_DELETE = 3;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private SettingAdapter adapter;
    private List<Setting> settings;


    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        toolbar.setBackgroundColor(Util.getColor(context, R.color.colorPrimary));
        toolbar.setBackBtnVisibility(true);
        toolbar.setTitleTxt("설정");
        rootLayout.addView(view);
        init();
        return rootLayout;
    }

    private void init(){

        if(BuildConfig.DEBUG){
            settings = Arrays.asList(new Setting(0, "버전 정보"), new Setting(1, "알림 설정"), new Setting(2, "로그아웃"), new Setting(3, "회원탈퇴"));
        }else{
            settings = Arrays.asList(new Setting(0, "버전 정보"), new Setting(1, "알림 설정"), new Setting(2, "로그아웃"));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new SettingAdapter(context);
        recyclerView.setAdapter(adapter);
        Observable.from(settings)
                .map(setting -> {
                    switch ((int)setting.id){
                        case VERSION:
                            setting.previewStr = context.getResources().getString(R.string.app_version);
                            break;
                        case NOTIFICATION:
                            break;
                    }
                    return setting;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> adapter.addItem(result), Logger::e);
        adapter.setOnItemClickListener((view, position) -> {
            switch (position){
                case NOTIFICATION:
                    break;
                case LOGOUT:
                    Navigator.goLogin(context);
                    break;
                case USER_DELETE:
                    callUserDelete();
            }
        });
    }

    private void callUserDelete(){
        Retro.instance.userService().deleteUser(App.setAuthHeader(App.userToken))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> Navigator.goLogin(context), ErrorUtils::parseError);
    }

    public class SettingAdapter extends CustomAdapter {

        public SettingAdapter(Context context) {
            super(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new SettingItemView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position == 0) {
                ((ViewHolder) holder).v.setNextImgVisibility(false);
            }
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


}
