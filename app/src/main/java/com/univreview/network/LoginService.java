package com.univreview.network;

import com.univreview.model.Login;
import com.univreview.model.SignIn;
import com.univreview.model.model_kotlin.UserModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public interface LoginService {
    // type (K : kakao, F : faceBook)
    // 인증이 안된 유저 : 401
    // 회원이 아닌 유저 : 404

    @POST("auth/sign_in")
    Observable<UserModel> login(@Body SignIn body);

}
