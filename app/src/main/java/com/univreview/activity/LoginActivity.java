package com.univreview.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.univreview.App;
import com.univreview.BuildConfig;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.log.Logger;
import com.univreview.model.Login;
import com.univreview.model.Register;
import com.univreview.model.UserModel;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.Util;
import com.univreview.view.presenter.LoginPresenter;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2016. 12. 25..
 */
public class LoginActivity extends BaseActivity implements LoginPresenter.View{
    @BindView(R.id.facebook_login_btn) Button facebookLoginBtn;
    @BindView(R.id.kakao_login_btn) Button kakaoLoginBtn;
    private CallbackManager facebookCallbackManager;
    private SessionCallback kakaoCallback;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setCurrentActivity(this);
        setFullScreen();
        if (App.userToken != null) {
            Navigator.goMain(this);
            finish();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new LoginPresenter();
        presenter.attachView(this);
        presenter.setContext(this);

        //facebook
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginBtn.setOnClickListener(v -> facebookLogin());

        //kakao
        kakaoCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(kakaoCallback);
        kakaoLoginBtn.setOnClickListener(v -> kakaoLogin());
    }

    private void facebookLogin() {
        Logger.v("facebook login");
        showProgress();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(facebookCallbackManager, facebookCallback);
    }

    private void kakaoLogin(){
        Logger.v("kakao login");
        showProgress();
        boolean isOpen = Session.getCurrentSession().checkAndImplicitOpen();
        if(!isOpen){
            dismissProgress();
            Session.getCurrentSession().open(AuthType.KAKAO_TALK , LoginActivity.this);
        }
    }

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(final LoginResult loginResult) {
           presenter.facebookOnSuccess(loginResult);
        }

        @Override
        public void onCancel() {
            dismissProgress();
        }

        @Override
        public void onError(FacebookException error) {
            dismissProgress();
            Logger.e(error);
            if (error instanceof FacebookAuthorizationException) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                    facebookLogin();
                }
            }
        }
    };

    //kakao

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            // 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴
            Logger.v("카카오 로그인 세션 오픈");
            presenter.kakaoOnSuccess();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                dismissProgress();
                Logger.e(exception.getMessage());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(facebookCallbackManager != null) {
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        Session.getCurrentSession().removeCallback(kakaoCallback);
    }
}
