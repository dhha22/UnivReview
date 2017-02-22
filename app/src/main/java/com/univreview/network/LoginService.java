package com.univreview.network;

import com.univreview.model.Login;
import com.univreview.model.ResponseModel;
import com.univreview.model.Token;
import com.univreview.model.UserModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
    @POST("auth")
    Observable<UserModel> login(@HeaderMap Map<String, String> headers, @Body Login login);

    @DELETE("auth")
    Observable<ResponseModel> logout();
}
