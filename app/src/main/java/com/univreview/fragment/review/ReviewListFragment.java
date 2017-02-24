package com.univreview.fragment.review;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.fragment.AbsListFragment;
import com.univreview.log.Logger;
import com.univreview.model.Review;
import com.univreview.util.AnimationUtils;
import com.univreview.view.ReviewItemView;
import com.univreview.view.UnivReviewRecyclerView;
import com.univreview.widget.PreCachingLayoutManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 2. 21..
 */
public class ReviewListFragment extends AbsListFragment {
    private static final String REVIEW = "review";
    private static final String PROFESSOR = "professor";
    @BindView(R.id.smooth_app_bar_layout) SmoothAppBarLayout appBarLayout;
    @BindView(R.id.toolbar) Toolbar customToolbar;
    @BindView(R.id.toolbar_title_layout) LinearLayout toolbarTitleLayout;
    @BindView(R.id.filter_layout) LinearLayout filterLayout;
    @BindView(android.R.id.list) UnivReviewRecyclerView recyclerView;
    @BindView(R.id.toolbar_title_txt) TextView toolbarTitleTxt;
    @BindView(R.id.title_txt) TextView titleTxt;
    @BindView(R.id.filter_name_txt) TextView filterNameTxt;
    private ReviewAdapter adapter;
    private String type;
    private long id;
    private String name;

    public static ReviewListFragment newInstance(String type, long id, String name) {
        ReviewListFragment fragment = new ReviewListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putLong("id", id);
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        id = getArguments().getLong("id");
        name = getArguments().getString("name");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);
        ButterKnife.bind(this, view);
        init();
        rootLayout.addView(view);
        return rootLayout;
    }

    private void init(){
        toolbar.setVisibility(View.GONE);
        adapter = new ReviewAdapter(context);
        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(context);
        layoutManager.setExtraLayoutSpace(App.SCREEN_HEIGHT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        titleTxt.setText(name);
        toolbarTitleTxt.setText(name);

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int height = appBarLayout.getHeight() - appBarLayout.getBottom();
            Logger.v("appbar height: " + appBarLayout.getHeight());
            Logger.v("appbar bottom: " + appBarLayout.getBottom());
            float value = (float) appBarLayout.getBottom() / appBarLayout.getHeight();
            Logger.v("height: " + height);
            AnimationUtils.setScale(titleTxt, value);
            if (height == 0) {
                AnimationUtils.fadeOut(context, toolbarTitleLayout);
                AnimationUtils.fadeIn(context, titleTxt);
                AnimationUtils.fadeIn(context, filterLayout);
            } else if (height >= customToolbar.getHeight() *1.1) {
                AnimationUtils.fadeIn(context, toolbarTitleLayout);
                AnimationUtils.fadeOut(context, titleTxt);
            } else {
                AnimationUtils.fadeOut(context, toolbarTitleLayout);
                AnimationUtils.fadeIn(context, titleTxt);
                AnimationUtils.fadeOut(context, filterLayout);
            }
        });


    }

    @Override
    public UnivReviewRecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void refresh() {

    }

    private class ReviewAdapter extends CustomAdapter {

        public ReviewAdapter(Context context) {
            super(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new ReviewItemView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
           // ((ViewHolder) holder).v.setData((Review) list.get(position));
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {
            final ReviewItemView v;

            public ViewHolder(View itemView) {
                super(itemView);
                v = (ReviewItemView) itemView;
            }
        }
    }

    private void response(List<Review> reviews){
        Observable.from(reviews)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> adapter.addItem(result), error-> errorResponse(error));
    }

    private void errorResponse(Throwable e){

    }
}
