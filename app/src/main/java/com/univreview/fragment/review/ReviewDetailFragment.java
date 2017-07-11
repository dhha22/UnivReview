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
import com.univreview.dialog.ListDialog;
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
import com.univreview.view.CommentInput;
import com.univreview.view.CommentItemView;
import com.univreview.view.RecyclerViewCustom;
import com.univreview.view.ReviewDetailHeader;
import com.univreview.view.ReviewRatingIndicatorView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 24..
 */
public class ReviewDetailFragment extends BaseFragment {
    private static final int DEFAULT_PAGE = 1;
    private static final int POSITION_NONE = -1;
    private ReviewDetailHeader headerView;
    @BindView(R.id.recycler_view) RecyclerViewCustom commentRecyclerView;
    @BindView(R.id.comment_input) CommentInput commentInput;
    private Review data;
    private CommentAdapter commentAdapter;
    public static boolean isRefresh = false;
    private ListDialog dialog;
    private List<String> dialogList;
    private int page = DEFAULT_PAGE;

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
            callReviewComment(data.id, DEFAULT_PAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_review_detail, container, false);
        ButterKnife.bind(this, view);
        toolbar.setBackBtnVisibility(true);
        rootLayout.setBackgroundColor(Util.getColor(context, R.color.backgroundColor));
        rootLayout.addView(view);
        init();
        return rootLayout;
    }

    private void init() {
        headerView = new ReviewDetailHeader(context);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        commentAdapter = new CommentAdapter(context, headerView);
        commentAdapter.setOnItemLongClickListener(commentItemLongClickListener);
        commentRecyclerView.setAdapter(commentAdapter);
        commentInput.setSendListener(v -> postReviewComment(data.id, commentInput.getInputMsg()));

        if(data != null && data.user.name != null) {
            setData(data);
        }else{
            callReviewSingleApi(data.id);
        }

        callReviewComment(data.id, DEFAULT_PAGE);
    }

    private void setData(Review data) {
        this.data = data;
        headerView.setData(data);
        if (App.userId == data.userId) {
            dialogList = Arrays.asList("리뷰신고","리뷰수정");
            if (data.reviewDetail != null) {
                dialogList = Arrays.asList("리뷰신고","리뷰수정");
            } else {
                dialogList = Arrays.asList("리뷰신고","상세리뷰 쓰기");
            }
        } else {
            dialogList = Arrays.asList("리뷰신고");
        }
        dialog = new ListDialog(context, dialogList, itemClickListener);
        headerView.setMoreBtnOnClickListener(v -> dialog.show());
        isRefresh = false;
    }

    private OnItemClickListener itemClickListener = (view, position) -> {
      switch (position){
          case 0:   // 리뷰신고
              Navigator.goReviewReport(context, data.id);
              break;
          case 1:   // 리뷰 수정 or 상세리뷰 쓰기
              Navigator.goUploadReviewDetail(context, data, POSITION_NONE);
              break;
      }
    };

    private class CommentAdapter extends CustomAdapter{

        public CommentAdapter(Context context, View headerView) {
            super(context, headerView, null);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == CONTENT) {
                return new ViewHolder(new CommentItemView(context));
            }
            return super.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == CONTENT) {
                ((ViewHolder) holder).v.setData((ReviewComment) list.get(position - 1));
            }
        }


        protected class ViewHolder extends RecyclerView.ViewHolder{
            final CommentItemView v;
            public ViewHolder(View itemView) {
                super(itemView);
                v = (CommentItemView)itemView;
                v.setOnLongClickListener(view -> itemLongClickListener.onLongClick(view, getAdapterPosition()));
            }

        }
    }


    private OnItemLongClickListener commentItemLongClickListener = (view, position) -> {
        Logger.v("click position: " + position);
        new AlertDialog.Builder(context)
                .setMessage("댓글을 삭제하시겠습니까?")
                .setPositiveButton("확인", (dialog, which) -> deleteReviewComment(commentAdapter.getItem(position).getId()))
                .setNegativeButton("취소", null)
                .show();
        return true;
    };



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

    private void callReviewComment(long reviewId, int page) {
        // 가장 최신에 달린 댓글이 list 하단에 존재
        Retro.instance.reviewService().getReviewComment(App.setAuthHeader(App.userToken), reviewId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    this.page++;
                    Logger.v("comment result: " + result.toString());
                    Observable.from(result.comments)
                            .takeLast(5)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(data -> commentAdapter.addItem(data), Logger::e);
                }, error -> {
                    this.page = DEFAULT_PAGE;
                    ErrorUtils.parseError(error);
                });
    }

    private void postReviewComment(long id, String message) {
        if (message != null) {
            ReviewComment body = new ReviewComment();
            body.reviewId = id;
            body.commentDetail = message;
            Retro.instance.reviewService().postReviewComment(App.setAuthHeader(App.userToken), body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> callReviewComment(data.id, DEFAULT_PAGE), ErrorUtils::parseError);
        } else {
            Util.toast("메세지를 입력해주세요");
        }
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
