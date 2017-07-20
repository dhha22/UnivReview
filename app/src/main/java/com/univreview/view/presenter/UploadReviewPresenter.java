package com.univreview.view.presenter;

import com.univreview.view.contract.UploadReviewContract;

/**
 * Created by DavidHa on 2017. 7. 20..
 */

public class UploadReviewPresenter implements UploadReviewContract {
    private UploadReviewContract.View view;



    @Override
    public void attachView(View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }
}
