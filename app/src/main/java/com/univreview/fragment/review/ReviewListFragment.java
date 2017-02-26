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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.fragment.AbsListFragment;
import com.univreview.listener.EndlessRecyclerViewScrollListener;
import com.univreview.log.Logger;
import com.univreview.model.Review;
import com.univreview.network.Retro;
import com.univreview.util.AnimationUtils;
import com.univreview.util.Util;
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
    private static final String SUBJECT = "subject";
    private static final String MAJOR = "major";
    private static final String PROFESSOR = "professor";
    private static final String MY_REVIEW = "myReview";
    @BindView(R.id.smooth_app_bar_layout) SmoothAppBarLayout appBarLayout;
    @BindView(R.id.toolbar) Toolbar customToolbar;
    @BindView(R.id.toolbar_title_layout) LinearLayout toolbarTitleLayout;
    @BindView(R.id.filter_layout) LinearLayout filterLayout;
    @BindView(R.id.toolbar_back_btn) ImageButton toolbarBackBtn;
    @BindView(android.R.id.list) UnivReviewRecyclerView recyclerView;
    @BindView(R.id.toolbar_title_txt) TextView toolbarTitleTxt;
    @BindView(R.id.toolbar_subtitle_txt) TextView toolbarSubtitleTxt;
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

    private void init() {
        if (type.equals(MY_REVIEW)) {
            appBarLayout.setVisibility(View.GONE);
            toolbar.setBackBtnVisibility(true);
            toolbar.setBackgroundColor(Util.getColor(context, R.color.colorPrimary));
            toolbar.setTitleTxt(name);
        } else if (type.equals(SUBJECT) || type.equals(PROFESSOR) || type.equals(MAJOR)) {
            toolbar.setVisibility(View.GONE);
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
                } else if (height >= customToolbar.getHeight() * 1.02) {
                    AnimationUtils.fadeIn(context, toolbarTitleLayout);
                    AnimationUtils.fadeOut(context, titleTxt);
                } else {
                    AnimationUtils.fadeOut(context, toolbarTitleLayout);
                    AnimationUtils.fadeIn(context, titleTxt);
                    AnimationUtils.fadeOut(context, filterLayout);
                }
            });
            titleTxt.setText(name);
            toolbarBackBtn.setOnClickListener(v -> activity.onBackPressed());
            toolbarTitleTxt.setText(name);
            toolbarSubtitleTxt.setText("전체");
        }
        adapter = new ReviewAdapter(context);
        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(context);
        layoutManager.setExtraLayoutSpace(App.SCREEN_HEIGHT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(view, dx, dy);
                if (getLastVisibleItemPosition() == getTotalItemCount() - 1) {
                    lastItemExposed();
                }
            }
        });
    }

    @Override
    public UnivReviewRecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void loadMore() {
        setStatus(Status.LOADING_MORE);
        callReviewListApi(id, page);
    }

    @Override
    public void refresh() {
        setStatus(Status.REFRESHING);
        Logger.v("page: " + page);
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
            if(type.equals(MY_REVIEW) || type.equals(PROFESSOR)){
                ((ViewHolder) holder).v.setMode(ReviewItemView.Status.MY_REVIEW);
            }else if(type.equals(SUBJECT)){
                ((ViewHolder) holder).v.setMode(ReviewItemView.Status.READ_REVIEW);
            }
           // ((ViewHolder) holder).v.setData((Review) list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {
            final ReviewItemView v;

            public ViewHolder(View itemView) {
                super(itemView);
                v = (ReviewItemView) itemView;
            }
        }
    }

    private void callReviewListApi(Long id, int page){
        if(type.equals(SUBJECT)){

        }
       /* Retro.instance.reviewService().getReviews(subjectId, professorId, page)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response(result.reviews), this::errorResponse);*/
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
