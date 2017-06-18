package com.univreview.fragment.review;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.activity.BaseActivity;
import com.univreview.adapter.CustomAdapter;
import com.univreview.fragment.BaseFragment;
import com.univreview.listener.OnBackPressedListener;
import com.univreview.listener.OnItemClickListener;
import com.univreview.listener.OnItemLongClickListener;
import com.univreview.log.Logger;
import com.univreview.model.Review;
import com.univreview.model.ReviewComment;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.TimeUtil;
import com.univreview.util.Util;
import com.univreview.view.CommentItemView;
import com.univreview.view.RecyclerViewCustom;
import com.univreview.view.ReviewDetailHeader;
import com.univreview.view.ReviewRatingIndicatorView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 24..
 */
public class ReviewDetailFragment extends BaseFragment {
    private static final int POSITION_NONE = -1;
    private ReviewDetailHeader headerView;
    @BindView(R.id.dim_view) View dimView;
    @BindView(R.id.bottom_sheet) LinearLayout bottomSheet;
    @BindView(R.id.update) TextView update;
    @BindView(R.id.report) TextView report;

    @BindView(R.id.recycler_view) RecyclerViewCustom commentRecyclerView;
    private BottomSheetBehavior behavior;
    private Review data;
    private CommentAdapter commentAdapter;
    public static boolean isRefresh = false;

    public static ReviewDetailFragment newInstance(Review data){
        ReviewDetailFragment fragment = new ReviewDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("review", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (Review)getArguments().getSerializable("review");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.v("review detail fragment refresh");
        if(isRefresh){
            callReviewSingleApi(data.id);
            callReviewComment(data.id);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((BaseActivity)activity).setOnBackPressedListener(backPressedListener);
        View view = inflater.inflate(R.layout.fragment_review_detail, container, false);
        ButterKnife.bind(this, view);
        headerView = new ReviewDetailHeader(context);
        toolbar.setBackBtnVisibility(true);
        rootLayout.setBackgroundColor(Util.getColor(context, R.color.backgroundColor));
        rootLayout.addView(view);
        init();
        return rootLayout;
    }

    private void init() {
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        commentAdapter = new CommentAdapter(context, headerView);
        commentAdapter.setOnItemClickListener(commentItemClickListener);
        commentAdapter.setOnItemLongClickListener(commentItemLongClickListener);
        commentRecyclerView.setAdapter(commentAdapter);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dimView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        if(data != null && data.user.name != null) {
            setData(data);
        }else{
            callReviewSingleApi(data.id);
        }

        callReviewComment(data.id);

        dimView.setOnClickListener(moreBtnClickListener);
        headerView.setMoreBtnOnClickListener(moreBtnClickListener);

    }

    private void setData(Review data) {
        headerView.setData(data);
        if (App.userId == data.userId) {
            update.setOnClickListener(v -> {
                hiddenBottomSheet();
                Navigator.goUploadReviewDetail(context, data, POSITION_NONE);
            });
            report.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);
            if (data.reviewDetail != null) {
                update.setText("리뷰수정");
            } else {
                update.setText("상세리뷰 쓰기");
            }
        } else {
            report.setVisibility(View.VISIBLE);
            update.setVisibility(View.GONE);
            report.setOnClickListener(v -> {
                hiddenBottomSheet();
                Navigator.goReviewReport(context, data.id);
            });
        }
        isRefresh = false;
    }

    private class CommentAdapter extends CustomAdapter{
        private static final int MAX_LENGTH = 5;

        public CommentAdapter(Context context, View headerView) {
            super(context, headerView);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType != HEADER) {
                return new ViewHolder(new CommentItemView(context));
            }
            return super.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) != HEADER) {
                ((ViewHolder) holder).v.setData((ReviewComment) list.get(position - 1));
            }
        }

        @Override
        public int getItemCount() {
            if (list.size() > MAX_LENGTH) {
                return MAX_LENGTH + HEADER;
            }
            return super.getItemCount();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{
            final CommentItemView v;
            public ViewHolder(View itemView) {
                super(itemView);
                v = (CommentItemView)itemView;
                v.setOnLongClickListener(view -> itemLongClickListener.onLongClick(view, getAdapterPosition()));
                v.setOnClickListener(view -> itemClickListener.onItemClick(view, getAdapterPosition()));
            }

        }
    }

    private View.OnClickListener moreBtnClickListener = view -> {
        if(behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            expandBottomSheet();
        }else if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
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

    private OnItemClickListener commentItemClickListener = (view, position) -> {
        Logger.v("click position: " + position);
        putReviewComment(commentAdapter.getItem(position).getId());
    };

    private OnItemLongClickListener commentItemLongClickListener = (view, position) -> {
        Logger.v("click position: " + position);
        new AlertDialog.Builder(context)
                .setMessage("댓글을 삭제하시겠습니까?")
                .setPositiveButton("확인", (dialog, which) -> deleteReviewComment(commentAdapter.getItem(position).getId()))
                .setNegativeButton("취소", null)
                .show();
        return true;
    };


    private void expandBottomSheet() {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        dimView.setVisibility(View.VISIBLE);
    }

    private void hiddenBottomSheet() {
        dimView.setVisibility(View.GONE);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }


    private void callReviewSingleApi(long reviewId){
        Logger.v("review id: " + reviewId);
        Retro.instance.reviewService().getReview(App.setAuthHeader(App.userToken), reviewId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Review review = result.review;
                    review.subjectDetail.subject.name = review.subjectName;
                    review.subjectDetail.professor.name = review.professorName;
                    review.user.name = review.userName;
                    review.user.authenticated = review.authenticated;
                    setData(review);
                }, ErrorUtils::parseError);
    }

    private void callReviewComment(long reviewId){
        Retro.instance.reviewService().getReviewComment(App.setAuthHeader(App.userToken), reviewId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Logger.v("comment result: " + result.toString());
                    Observable.from(result.comments)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(data -> commentAdapter.addItem(data), Logger::e);
                }, ErrorUtils::parseError);
    }

    private void postReviewComment(long id){
        ReviewComment body = new ReviewComment();
        body.reviewId = id;
        body.commentDetail = "테스트";
        Retro.instance.reviewService().postReviewComment(App.setAuthHeader(App.userToken), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    try {
                        Logger.v("result: " + result.string());
                    }catch (IOException e){

                    }
                }, ErrorUtils::parseError);
    }

    private void putReviewComment(long id){
        ReviewComment body = new ReviewComment();
        body.commentDetail = " 수정 테스트";
        Retro.instance.reviewService().putReviewComment(App.setAuthHeader(App.userToken),id ,body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    try {
                        Logger.v("result: " + result.string());
                    }catch (IOException e){

                    }
                }, ErrorUtils::parseError);
    }

    private void deleteReviewComment(long id){
        Retro.instance.reviewService().deleteComment(App.setAuthHeader(App.userToken), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    try {
                        Logger.v("result: " + result.string());
                    }catch (IOException e){

                    }
                }, ErrorUtils::parseError);
    }

}
