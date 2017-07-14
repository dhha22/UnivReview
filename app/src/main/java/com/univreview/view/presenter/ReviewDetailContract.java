package com.univreview.view.presenter;

import android.content.DialogInterface;

import com.univreview.model.Review;
import com.univreview.model.ReviewComment;

/**
 * Created by DavidHa on 2017. 7. 14..
 */

public interface ReviewDetailContract{
    interface View{
        void setData(Review data);
        long getReviewId();
        void setPage(int page);
        void showCommentDeleteDialog(DialogInterface.OnClickListener clickListener);
        void dismissProgress();
    }

    void attachView(View view);
    void detachView();
    void loadReviewSingle();
    void postComment(ReviewComment body);
    void loadCommentItem(int page);
}
