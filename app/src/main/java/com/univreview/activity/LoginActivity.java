package com.univreview.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

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
import com.univreview.R;
import com.univreview.log.Logger;
import com.univreview.network.Retro;
import com.univreview.util.Util;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2016. 12. 25..
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.facebook_login_btn) Button facebookLoginBtn;
    @BindView(R.id.kakao_login_btn) Button kakaoLoginBtn;
    @BindView(R.id.text) TextView textView;
    private CallbackManager facebookCallbackManager;
    private ProgressDialog progressDialog;
    private SessionCallback kakaoCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressDialog = Util.progressDialog(this);

        //facebook
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginBtn.setOnClickListener(v -> facebookLogin());

        //kakao
        kakaoCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(kakaoCallback);
        kakaoLoginBtn.setOnClickListener(v -> kakaoLogin());
    }



    private void facebookLogin(){
        progressDialog.show();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(facebookCallbackManager, facebookCallback);
    }

    private void kakaoLogin(){
        progressDialog.show();
        boolean isOpen = Session.getCurrentSession().checkAndImplicitOpen();
        if(!isOpen){
            progressDialog.dismiss();
            Session.getCurrentSession().open(AuthType.KAKAO_TALK , LoginActivity.this);
        }
    }

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(final LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                Logger.v("facebook response" + response.toString());
                try {
                    String userId = object.getString("id");   //facebook user id
                    String accessToken = loginResult.getAccessToken().getToken();    //facebookAccessToken
                    object.getJSONObject("picture").getJSONObject("data").getString("url"); //facebook profile image
                    callLoginApi("F", userId, accessToken);
                } catch (Exception e) {
                    Logger.e(e.toString());
                }

            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException error) {
            progressDialog.dismiss();
            Logger.e(error);
            if (error instanceof FacebookAuthorizationException) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                    facebookLogin();
                } else {

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
            kakaoRequestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception.getMessage());
            }
        }
    }

    protected void kakaoRequestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;
                if (ErrorCode == ClientErrorCode) {
                    Util.toast("카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.");
                } else {
                    Logger.e("오류로 카카오로그인 실패 ");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Logger.e("카카오 로그인 에러");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Logger.v("카카오 로그인 성공");
                String userId = String.valueOf(userProfile.getId());
                String accessToken = Session.getCurrentSession().getAccessToken();

                userProfile.getProfileImagePath();  //kakao profile image
                userProfile.getNickname();  //kakao nickname
                callLoginApi("K", userId, accessToken);
            }

            @Override
            public void onNotSignedUp() {
                Logger.v("자동로그인 아님");
                // 자동가입이 아닐경우 동의창
            }
        });
    }

    private void callLoginApi(String userType, String userId, String userAccessToken){
        Retro.instance.loginService().login(userType, userId, userAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void loginResponse(){

    }
}
