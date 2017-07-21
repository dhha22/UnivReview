package com.univreview.view.contract;

import android.content.Context;

import com.univreview.model.Review;

/**
 * Created by DavidHa on 2017. 7. 20..
 */

public interface UploadReviewContract {
    interface View {
        void showProgress();
        void dismissProgress();
        void showRecommendRvDialog();
        String getSubjectName();
        String getProfessorName();
        void showAlertDialog();
        void setReviewExist(boolean isExist);
    }

    Review getReview();
    void setContext(Context context);
    void attachView(View view);
    void detachView();
    void registerReview();
    void checkReviewExist();

}
