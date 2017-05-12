package com.univreview.fragment.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.activity.BaseActivity;
import com.univreview.adapter.CustomAdapter;
import com.univreview.fragment.AbsListFragment;
import com.univreview.listener.EndlessRecyclerViewScrollListener;
import com.univreview.listener.OnBackPressedListener;
import com.univreview.listener.OnItemClickListener;
import com.univreview.log.Logger;
import com.univreview.model.AbstractDataProvider;
import com.univreview.model.ActivityResultEvent;
import com.univreview.model.RandomImageModel;
import com.univreview.model.Review;
import com.univreview.model.ReviewListModel;
import com.univreview.network.Retro;
import com.univreview.util.AnimationUtils;
import com.univreview.util.ErrorUtils;
import com.univreview.util.Util;
import com.univreview.view.ReviewItemView;
import com.univreview.view.ReviewTotalScoreView;
import com.univreview.view.UnivReviewRecyclerView;
import com.univreview.widget.PreCachingLayoutManager;

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
    private static final String PROFESSOR = "professor";
    private static final String MY_REVIEW = "myReview";
    private static final int POSITION_NONE = -1;
    public static long reviewSingleId = POSITION_NONE;
    public static int reviewItemRefreshPosition = POSITION_NONE;
    @BindView(R.id.dim_view) View dimView;
    @BindView(R.id.smooth_app_bar_layout) SmoothAppBarLayout appBarLayout;
    @BindView(R.id.toolbar) Toolbar customToolbar;
    @BindView(R.id.toolbar_image) ImageView toolbarImage;
    @BindView(R.id.toolbar_title_layout) LinearLayout toolbarTitleLayout;
    @BindView(R.id.filter_layout) LinearLayout filterLayout;
    @BindView(R.id.toolbar_back_btn) ImageButton toolbarBackBtn;
    @BindView(android.R.id.list) UnivReviewRecyclerView recyclerView;
    @BindView(R.id.toolbar_title_txt) TextView toolbarTitleTxt;
    @BindView(R.id.toolbar_subtitle_txt) TextView toolbarSubtitleTxt;
    @BindView(R.id.title_txt) TextView titleTxt;
    @BindView(R.id.filter_name_txt) TextView filterNameTxt;
    @BindView(R.id.bottom_sheet) LinearLayout bottomSheet;
    @BindView(R.id.update) TextView update;
    @BindView(R.id.report) TextView report;
    private ReviewAdapter adapter;
    private String type;
    private long id;
    private String name;
    private Long subjectId;
    private Long professorId;
    private RandomImageModel randomImageModel;
    private boolean isFirstError = true;
    private BottomSheetBehavior behavior;

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
        randomImageModel = new RandomImageModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((BaseActivity)activity).setOnBackPressedListener(backPressedListener);
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);
        ButterKnife.bind(this, view);
        init();
        recyclerView.setMode(UnivReviewRecyclerView.Mode.DISABLED);
        recyclerView.setBackgroundColor(Util.getColor(context, R.color.backgroundColor));
        rootLayout.addView(view);
        return rootLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (reviewSingleId != -1 && reviewItemRefreshPosition != -1) {
            callReviewSingleApi(reviewSingleId, reviewItemRefreshPosition);
        }
    }

    private void init() {
        App.picasso.load(randomImageModel.getImageURL()).fit().centerCrop().into(toolbarImage);
        if (type.equals(MY_REVIEW)) {
            appBarLayout.setVisibility(View.GONE);
            toolbar.setBackBtnVisibility(true);
            toolbar.setTitleTxt(name);
        } else if (type.equals(SUBJECT) || type.equals(PROFESSOR)) {
            toolbar.setVisibility(View.GONE);
            recyclerView.setPadding(0, (int) Util.dpToPx(context, 38), 0, 0);
            appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
                int height = appBarLayout.getHeight() - appBarLayout.getBottom();
              //  Logger.v("appbar height: " + appBarLayout.getHeight());
              //  Logger.v("appbar bottom: " + appBarLayout.getBottom());
                float value = (float) appBarLayout.getBottom() / appBarLayout.getHeight();
              //  Logger.v("height: " + height);
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
            filterLayout.setOnClickListener(v-> Navigator.goSimpleSearchResult(context, type, id));
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
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN){
                    dimView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        dimView.setOnClickListener(view -> {
            if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                expandBottomSheet();
            } else if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                hiddenBottomSheet();
            }
        });
    }


    @Override
    public UnivReviewRecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void refresh() {
        Logger.v("refresh");
        setStatus(Status.REFRESHING);
        callReviewListApi(id, DEFAULT_PAGE);
    }

    @Override
    public void loadMore() {
        setStatus(Status.LOADING_MORE);
        Logger.v("page: " + page);
        callReviewListApi(id, page);
    }


    private class ReviewAdapter extends CustomAdapter {
        private static final int HEADER = 0;
        private static final int CONTENT = 1;
        private float totalRate;
        private Review reviewAverage = new Review();

        public ReviewAdapter(Context context) {
            super(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER) {
                return new HeaderViewHolder(new ReviewTotalScoreView(context));
            }
            return new ViewHolder(new ReviewItemView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == HEADER) {
                ((HeaderViewHolder) holder).v.setData(totalRate, reviewAverage);
            } else if (getItemViewType(position) == CONTENT) {
                if (!type.equals(MY_REVIEW)) {
                    ((ViewHolder) holder).v.setData((Review) list.get(position - 1), position - 1);
                } else {
                    ((ViewHolder) holder).v.setData((Review) list.get(position), position);
                    ((ViewHolder) holder).v.setPosition(position);
                }
            }
        }

        public void setTotalRate(float rate, Review average) {
            this.totalRate = rate;
            this.reviewAverage = average;
            notifyDataSetChanged();
        }


        @Override
        public int getItemCount() {
            if (!type.equals(MY_REVIEW)) {
                return list.size() + 1;
            }
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (!type.equals(MY_REVIEW) && position == 0) {
                return HEADER;
            }
            return CONTENT;
        }

        @Override
        public Review getItem(int position) {
            return (Review) list.get(position);
        }

        protected class HeaderViewHolder extends RecyclerView.ViewHolder{
            final ReviewTotalScoreView v;

            public HeaderViewHolder(View itemView) {
                super(itemView);
                v = (ReviewTotalScoreView) itemView;
            }
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {
            final ReviewItemView v;

            public ViewHolder(View itemView) {
                super(itemView);
                v = (ReviewItemView) itemView;
                v.setMoreBtnClickListener(moreBtnClickListener);
                if (type.equals(MY_REVIEW)) {
                    v.setMode(ReviewItemView.Status.MY_REVIEW);
                } else if (type.equals(SUBJECT) || type.equals(PROFESSOR)) {
                    v.setMode(ReviewItemView.Status.READ_REVIEW);
                }
            }
        }
    }

    private OnItemClickListener moreBtnClickListener = (view, position) -> {
        Review review = adapter.getItem(position);
        Logger.v("review data: " + review);
        if (App.userId == review.userId) {
            update.setOnClickListener(v -> {
                hiddenBottomSheet();
                Navigator.goUploadReviewDetail(context, review, position);
            });
            report.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);
            if (review.reviewDetail != null) {
                update.setText("리뷰수정");
            } else {
                update.setText("상세리뷰 쓰기");
            }

        } else {
            report.setVisibility(View.VISIBLE);
            update.setVisibility(View.GONE);
            report.setOnClickListener(v -> {
                hiddenBottomSheet();
                Navigator.goReviewReport(context, review.id);
            });
        }

        if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            expandBottomSheet();
        } else if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            hiddenBottomSheet();
        }
    };

    private OnBackPressedListener backPressedListener = () -> {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            hiddenBottomSheet();
        } else {
            ((BaseActivity) activity).setOnBackPressedListener(null);
            activity.onBackPressed();
        }
    };


    private void expandBottomSheet() {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        dimView.setVisibility(View.VISIBLE);
    }

    private void hiddenBottomSheet() {
        dimView.setVisibility(View.GONE);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        if (activityResultEvent.getResultCode() == getActivity().RESULT_OK) {
            if (activityResultEvent.getRequestCode() == Navigator.SEARCH) {
                Intent data = activityResultEvent.getIntent();
                long id = data.getLongExtra("id", 0);
                String name = data.getStringExtra("name");
                String type = data.getStringExtra("type");
                Logger.v("on activity result: " + type);
                if (SUBJECT.equals(type)) {
                    professorId = id;
                } else if (PROFESSOR.equals(type)) {
                    subjectId = id;
                }
                isFirstError = true;
                page = DEFAULT_PAGE;
                refresh();
                filterNameTxt.setText(name);
                toolbarSubtitleTxt.setText(name);
            }
        }
    }

    private void callReviewListApi(Long id, int page) {
        Long userId = null;
        if (type.equals(SUBJECT)) {
            subjectId = id;
        } else if (type.equals(PROFESSOR)) {
            professorId = id;
        } else if (type.equals(MY_REVIEW)) {
            userId = App.userId;
        }

        if (subjectId != null && subjectId == 0) subjectId = null;
        if (professorId != null && professorId == 0) professorId = null;

        Logger.v("page: " + page);
        Logger.v("type: " + type);
        Logger.v("subject id: " + subjectId);
        Logger.v("professor id: " + professorId);
        Logger.v("user id: " + userId);
        if (type.equals(MY_REVIEW)) {
            Retro.instance.reviewService().getMyReviews(App.setAuthHeader(App.userToken), page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::response, this::errorResponse, () -> {
                        if (page == DEFAULT_PAGE) adapter.clear();
                    });
        } else {
            Retro.instance.reviewService().getReviews(App.setAuthHeader(App.userToken), subjectId, professorId, userId, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::response, this::errorResponse, () -> {
                        if (page == DEFAULT_PAGE) adapter.clear();
                    });
        }
    }

    private void callReviewSingleApi(long id, int position) {
        Retro.instance.reviewService().getReview(App.setAuthHeader(App.userToken), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    reviewSingleId = POSITION_NONE;
                    reviewItemRefreshPosition = POSITION_NONE;
                })
                .subscribe(result -> {
                    Review review = result.review;
                    review.subjectDetail.subject.name = review.subjectName;
                    review.subjectDetail.professor.name = review.professorName;
                    review.user.name = review.userName;
                    review.user.authenticated = review.authenticated;
                    adapter.setItem(position, review);
                }, this::errorResponse);
    }

    private void response(ReviewListModel reviewListModel) {
        setResult(page);
        setStatus(Status.IDLE);
        Logger.v("result: " + reviewListModel);
        adapter.setTotalRate(reviewListModel.totalAverageRates, reviewListModel.getReviewAverage());
        Observable.from(reviewListModel.reviews)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> adapter.addItem(result), Logger::e);
    }

    private void errorResponse(Throwable e) {
        setStatus(Status.ERROR);
        if (isFirstError) {
            adapter.addItem(null);
            isFirstError = false;
        }
        ErrorUtils.parseError(e);
    }
}
