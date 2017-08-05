package com.univreview.view.presenter;

import android.content.Context;
import android.os.Bundle;

import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.log.Logger;
import com.univreview.model.Login;
import com.univreview.model.Register;
import com.univreview.model.UserModel;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.Util;
import com.univreview.view.contract.LoginContract;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 7. 31..
 */

public class LoginPresenter implements LoginContract {
    private LoginContract.View view;
    private Context context;


    @Override
    public void facebookOnSuccess(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
            Logger.v("facebook responseMajor" + response.toString());
            try {
                String userId = object.getString("id");   //facebook user id
                String accessToken = loginResult.getAccessToken().getToken();    //facebookAccessToken
                String nickName = object.getString("name");
                String profileUrl = object.getJSONObject("picture").getJSONObject("data").getString("url"); //facebook profile image
                callLoginApi("F", userId, accessToken, nickName, profileUrl);
            } catch (Exception e) {
                Logger.e(e.toString());
            }

        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void kakaoOnSuccess() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                view.dismissProgress();
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
                view.dismissProgress();
                Logger.e("카카오 로그인 에러");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Logger.v("카카오 로그인 성공");
                String userId = String.valueOf(userProfile.getId());
                String accessToken = Session.getCurrentSession().getAccessToken();
                String nickName = userProfile.getNickname();  //kakao nickname
                String profileURL = userProfile.getProfileImagePath();  //kakao profile image

                callLoginApi("K", userId, accessToken, nickName, profileURL);
            }

            @Override
            public void onNotSignedUp() {
                Logger.v("자동로그인 아님");
                // 자동가입이 아닐경우 동의창
            }
        });
    }

    //api
    private void callLoginApi(String userType, String userId, String accessToken, String nickName, String profileURL) {
        Logger.v("userType: " + userType + ", userId: " + userId + ", accessToken: " + accessToken + ", nickName: " + nickName + ", profileUrl: " + profileURL);
        Retro.instance.loginService().login(App.setAuthHeader(""), new Login(userType, userId, accessToken))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> view.dismissProgress())
                .subscribe(this::response,
                        error -> loginErrorResponse(error, new Register(userType, userId, accessToken, nickName, profileURL)));
    }

    private void response(UserModel userModel){
        Logger.v("response: " + userModel);
        App.setUserId(userModel.user.id);
        App.setUserToken(userModel.auth.getToken());
        App.setUniversityId(userModel.user.universityId);
        Navigator.goMain(context);
    }


    private void loginErrorResponse(Throwable error, Register register) {
        if (ErrorUtils.parseError(error) == ErrorUtils.ERROR_404) {   //신규회원
            Navigator.goRegisterUserInfo(context, register);
        } else {
           Util.toast("서버 오류입니다.");
        }
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
        context = null;
    }
}
