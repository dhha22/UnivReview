package com.univreview.fragment.review;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.adapter.contract.ReviewDetailAdapterContract;
import com.univreview.dialog.ListDialog;
import com.univreview.fragment.BaseFragment;
import com.univreview.listener.OnItemClickListener;
import com.univreview.log.Logger;
import com.univreview.model.AbstractDataProvider;
import com.univreview.model.Review;
import com.univreview.model.ReviewComment;
import com.univreview.util.Util;
import com.univreview.view.CommentInput;
import com.univreview.view.CommentItemView;
import com.univreview.view.RecyclerViewCustom;
import com.univreview.view.ReviewDetailHeader;
import com.univreview.view.contract.ReviewDetailContract;
import com.univreview.view.presenter.ReviewDetailPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 24..
 */
public class ReviewDetailFragment extends BaseFragment implements ReviewDetailContract.View {
    private static final int DEFAULT_PAGE = 1;
    private static final int POSITION_NONE = -1;
    private ReviewDetailHeader headerView;
    @BindView(R.id.recycler_view) RecyclerViewCustom commentRecyclerView;
    @BindView(R.id.comment_input) CommentInput commentInput;
    private Review data;
    private CommentAdapter commentAdapter;
    public  boolean isRefresh = true;
    private ListDialog dialog;
    private List<String> dialogList = new ArrayList<>();
    private int page = DEFAULT_PAGE;
    private long reviewId;
    private ReviewDetailPresenter presenter;


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
        presenter = new ReviewDetailPresenter();
        presenter.view = this;

        if(data != null){
            reviewId = data.id;
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_review_detail, container, false);
        ButterKnife.bind(this, view);
        toolbar.setBackBtnVisibility(true);
        rootLayout.setBackgroundColor(Util.getColor(getContext(), R.color.backgroundColor));
        rootLayout.addView(view);
        init(data);
        return rootLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isRefresh){
            Logger.v("review detail fragment refresh");
            presenter.loadReviewSingle();
            presenter.loadCommentItem(DEFAULT_PAGE);
            isRefresh = false;
        }
    }

    private void init(Review data) {
        headerView = new ReviewDetailHeader(getContext());
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentAdapter = new CommentAdapter(getContext(), headerView);
        commentRecyclerView.setAdapter(commentAdapter);
        commentInput.setSendListener(v -> postReviewComment(getReviewId(), commentInput.getInputMsg()));
        presenter.setAdapterModel(commentAdapter);
        presenter.setAdapterView(commentAdapter);
        if(data != null) {
            setData(data);
            setDialog(data.userId);
        }
        headerView.setCommentMoreBtnListener(v -> presenter.loadCommentItem(page));
    }

    @Override
    public void setData(Review data) {
        Logger.v("set review data: " + data);
        headerView.setData(data);
    }

    public void scrollToPosition(int position){
        Logger.v("scroll position: " + position);
        commentRecyclerView.scrollToPosition(position);
    }

    @Override
    public void setCommentMoreBtn(boolean hasMore) {
        headerView.setCommentMoreBtn(hasMore);
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    public long getReviewId(){
       return reviewId;
    }

    private void setDialog(long userId) {

        if (data.reviewDetail != null) {
            dialogList.add("리뷰수정");
        } else {
            dialogList.add("상세리뷰 쓰기");
        }

        if ((long) App.userId != userId) {
            dialogList.add(0, "리뷰신고");
        }
        dialog = new ListDialog(getContext(), dialogList, dialogItemClickListener);
        headerView.setEtcBtnClickListener(v -> dialog.show());

    }

    private OnItemClickListener dialogItemClickListener = (view, position) -> {
      switch (position){
          case 0:   // 리뷰신고
              Navigator.goReviewReport(getContext(), data.id);
              break;
          case 1:   // 리뷰 수정 or 상세리뷰 쓰기
              Navigator.goUploadReviewDetail(getContext(), data, POSITION_NONE);
              break;
      }
    };

    private class CommentAdapter extends CustomAdapter implements ReviewDetailAdapterContract.Model, ReviewDetailAdapterContract.View{

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

        @Override
        public void addItem(AbstractDataProvider item) {
            list.add(0, item);
            notifyDataSetChanged();
        }

        @Override
        public void removeItem(int position) {
            list.remove(position -1);
            notifyItemRemoved(position);
        }
    }


    @Override
     public void showCommentDeleteDialog(DialogInterface.OnClickListener clickListener){
        new AlertDialog.Builder(getContext())
                .setMessage("댓글을 삭제하시겠습니까?")
                .setPositiveButton("확인", clickListener)
                .setNegativeButton("취소", null)
                .show();
    }


    private void postReviewComment(long id, String message) {
        if (message != null) {
            showProgress();
            ReviewComment body = new ReviewComment();
            body.reviewId = id;
            body.commentDetail = message;
            commentRecyclerView.smoothScrollToPosition(commentAdapter.getItemCount());
            presenter.postComment(body);
        } else {
            Util.toast("메세지를 입력해주세요");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
