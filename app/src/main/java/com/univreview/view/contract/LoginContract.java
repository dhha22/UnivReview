package com.univreview.view.contract;

import android.content.Context;

import com.facebook.login.LoginResult;

/**
 * Created by DavidHa on 2017. 7. 31..
 */

public interface LoginContract {
    interface View {
        void showProgress();
        void dismissProgress();
    }

    void facebookOnSuccess(final LoginResult loginResult);
    void kakaoOnSuccess();
    void setContext(Context context);
    void attachView(View view);
    void detachView();
}
