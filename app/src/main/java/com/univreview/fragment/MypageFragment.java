package com.univreview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.log.Logger;
import com.univreview.model.AbstractDataProvider;
import com.univreview.model.ActivityResultEvent;
import com.univreview.model.Setting;
import com.univreview.util.ImageUtil;
import com.univreview.view.SettingItemView;

import java.util.Arrays;

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
    @BindView(R.id.name_txt) TextView nameTxt;
    @BindView(R.id.department_txt) TextView departmentTxt;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    public static MypageFragment newInstance(){
        MypageFragment fragment = new MypageFragment();
        return fragment;
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
        MyPageAdapter adapter = new MyPageAdapter(context);
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
            ((ViewHolder)holder).v.setTitle(list.get(position).getName());
        }

        @Override
        public AbstractDataProvider getItem(int position) {
            return list.get(position);
        }

        @Override
        public void addItem(AbstractDataProvider item) {
            list.add(item);
            notifyDataSetChanged();
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
}
