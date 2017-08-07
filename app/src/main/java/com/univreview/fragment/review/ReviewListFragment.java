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
import com.univreview.activity.NavigationActivity;
import com.univreview.adapter.CustomAdapter;
import com.univreview.adapter.contract.ReviewListAdapterContract;
import com.univreview.dialog.ListDialog;
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
import com.univreview.model.enumeration.ReviewSearchType;
import com.univreview.network.Retro;
import com.univreview.util.AnimationUtils;
import com.univreview.util.ErrorUtils;
import com.univreview.util.Util;
import com.univreview.view.ReviewItemView;
import com.univreview.view.ReviewTotalScoreView;
import com.univreview.view.UnivReviewRecyclerView;
import com.univreview.view.contract.ReviewListContract;
import com.univreview.view.presenter.ReviewListPresenter;
import com.univreview.widget.PreCachingLayoutManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.univreview.model.enumeration.ReviewSearchType.MY_REVIEW;
import static com.univreview.model.enumeration.ReviewSearchType.PROFESSOR;
import static com.univreview.model.enumeration.ReviewSearchType.SUBJECT;

/**
 * Created by DavidHa on 2017. 2. 21..
 */
/*
public class ReviewListFragment extends AbsListFragment implements ReviewListContract.View{
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
    private ReviewTotalScoreView headerView;
    private ReviewAdapter adapter;
    private ReviewSearchType type;
    private long id;
    private String name;
    private RandomImageModel randomImageModel;
    private BottomSheetBehavior behavior;
    private ReviewListPresenter presenter;
    private Long subjectId;
    private Long professorId;
    private Long userId;
    private ListDialog filterDialog;

    public static ReviewListFragment newInstance(ReviewSearchType type, long id, String name) {
        ReviewListFragment fragment = new ReviewListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        bundle.putLong("id", id);
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (ReviewSearchType) getArguments().getSerializable("type");
        id = getArguments().getLong("id");
        name = getArguments().getString("name");
        randomImageModel = new RandomImageModel();
        presenter = new ReviewListPresenter();
        presenter.view = this;

        switch (type){
            case SUBJECT:
                subjectId = id;
                break;
            case PROFESSOR:
                professorId = id;
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((BaseActivity)getActivity()).setOnBackPressedListener(backPressedListener);
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);
        ButterKnife.bind(this, view);
        init();
        recyclerView.setMode(UnivReviewRecyclerView.Mode.DISABLED);
        recyclerView.setBackgroundColor(Util.getColor(getContext(), R.color.backgroundColor));
        rootLayout.addView(view);
        return rootLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (reviewSingleId != -1 && reviewItemRefreshPosition != -1) {
            //callReviewSingleApi(reviewSingleId, reviewItemRefreshPosition);
        }
    }

    private void init() {
        App.picasso.load(randomImageModel.getImageURL()).fit().centerCrop().into(toolbarImage);
        if (type.equals(MY_REVIEW)) {
            adapter = new ReviewAdapter(getContext());
            appBarLayout.setVisibility(View.GONE);
            toolbar.setBackBtnVisibility(true);
            toolbar.setTitleTxt(name);
        } else if (type.equals(SUBJECT) || type.equals(PROFESSOR)) {
            headerView = new ReviewTotalScoreView(getContext());
            adapter = new ReviewAdapter(getContext(), headerView);
            toolbar.setVisibility(View.GONE);
            recyclerView.setPadding(0, (int) Util.dpToPx(getContext(), 38), 0, 0);
            appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
                int height = appBarLayout.getHeight() - appBarLayout.getBottom();
                float value = (float) appBarLayout.getBottom() / appBarLayout.getHeight();
                AnimationUtils.setScale(titleTxt, value);
                if (height == 0) {
                    AnimationUtils.fadeOut(getContext(), toolbarTitleLayout);
                    AnimationUtils.fadeIn(getContext(), titleTxt);
                    AnimationUtils.fadeIn(getContext(), filterLayout);
                } else if (height >= customToolbar.getHeight() * 1.02) {
                    AnimationUtils.fadeIn(getContext(), toolbarTitleLayout);
                    AnimationUtils.fadeOut(getContext(), titleTxt);
                } else {
                    AnimationUtils.fadeOut(getContext(), toolbarTitleLayout);
                    AnimationUtils.fadeIn(getContext(), titleTxt);
                    AnimationUtils.fadeOut(getContext(), filterLayout);
                }
            });

            titleTxt.setText(name);
            toolbarBackBtn.setOnClickListener(v -> getActivity().onBackPressed());
            toolbarTitleTxt.setText(name);
            toolbarSubtitleTxt.setText("전체");
        }
        setRecyclerView();
        setBottomSheetBehavior();

        if(subjectId != null) {
            presenter.searchProfessor(subjectId);
        }else if(professorId != null){
            presenter.searchSubject(professorId);
        }
    }

    public void setHeaderViewVisibility(boolean isVisibility){
        if(isVisibility) {
            headerView.setVisibility(View.VISIBLE);
        }else{
            headerView.setVisibility(View.GONE);
        }
    }

    private void setRecyclerView(){
        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(getContext());
        layoutManager.setExtraLayoutSpace(App.SCREEN_HEIGHT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        presenter.setAdapterModel(adapter);
        presenter.setAdapterView(adapter);
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

    private void setBottomSheetBehavior(){
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
        presenter.loadReviewItem(type, getDEFAULT_PAGE());
    }

    @Override
    public void loadMore() {
        setStatus(Status.LOADING_MORE);
        Logger.v("page: " + getPage());
        presenter.loadReviewItem(type, getPage());
    }


    private class ReviewAdapter extends CustomAdapter implements ReviewListAdapterContract.Model, ReviewListAdapterContract.View{
        public ReviewAdapter(Context context) {
            super(context);
        }

        public ReviewAdapter(Context context, View headerView) {
            super(context, headerView);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == CONTENT) {
                return new ViewHolder(new ReviewItemView(context));
            }
            return super.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
           if (getItemViewType(position) == CONTENT) {
                if (!type.equals(MY_REVIEW)) {
                    ((ViewHolder) holder).v.setData((Review) list.get(position - 1), position - 1);
                } else {
                    ((ViewHolder) holder).v.setData((Review) list.get(position), position);
                    ((ViewHolder) holder).v.setPosition(position);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (!type.equals(MY_REVIEW)) {
                return list.size() + HEADER;
            }
            return super.getItemCount();
        }

        @Override
        public int getItemViewType(int position) {
            if (!type.equals(MY_REVIEW) && position == 0) {
                return HEADER;
            }
            return super.getItemViewType(position);
        }

        @Override
        public Review getItem(int position) {
            return (Review) list.get(position);
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
                Navigator.goUploadReviewDetail(getContext(), review, position);
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
                Navigator.goReviewReport(getContext(), review.id);
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
            ((BaseActivity) getActivity()).setOnBackPressedListener(null);
            getActivity().onBackPressed();
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

    @Override
    public void setDialog(List<String> list) {
        if (filterDialog == null) {
            list.add(0, "전체");
            filterDialog = new ListDialog(getContext(), list, dialogItemClickListener);
            filterLayout.setOnClickListener(v-> filterDialog.show());
        }
    }

    private OnItemClickListener dialogItemClickListener = (view, position) -> {
        String filterName = "";
        if (position != 0) {
            switch (type) {
                case SUBJECT:  // 교수명 리스트
                    professorId = presenter.searchModel.professors.get(position - 1).getId();
                    filterName = presenter.searchModel.professors.get(position - 1).getName();
                    break;
                case PROFESSOR: // 과목 리스트
                    subjectId = presenter.searchModel.subjects.get(position - 1).getId();
                    filterName = presenter.searchModel.subjects.get(position - 1).getName();
                    break;
            }
        } else {
            filterName = "전체";
            switch (type) {
                case SUBJECT:
                    professorId = null;
                    break;
                case PROFESSOR:
                    subjectId = null;
            }
        }
        presenter.loadReviewItem(type, getDEFAULT_PAGE());
        setFilterName(filterName);
    };

    @Override
    public void setFilterName(String filterName){
        filterNameTxt.setText(filterName);
        toolbarSubtitleTxt.setText(filterName);
    }



    */
/*private void callReviewSingleApi(long id, int position) {
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
    }*//*



    @Override
    public ReviewTotalScoreView getReviewTotalScoreView() {
        return headerView;
    }

    @Override
    public Long getSubjectId() {
        Logger.v("subjectId: " + subjectId);
        return subjectId;
    }

    @Override
    public Long getProfessorId() {
        Logger.v("professorId: " + professorId);
        return professorId;
    }

    @Override
    public Long getUserId() {
        Logger.v("userId: " + userId);
        return userId;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
*/
