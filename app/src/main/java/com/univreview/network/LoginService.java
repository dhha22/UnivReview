package com.univreview.network;

import com.univreview.model.ResponseModel;

import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public interface LoginService {
    // type (K : kakao, F : faceBook)
    @POST("auth")
    Observable<ResponseModel> login(@Field("userType") String userType,
                                    @Field("userId") String userId,
                                    @Field("accessToken") String accessToken);

    @DELETE("auth")
    Observable<ResponseModel> logout();
}
