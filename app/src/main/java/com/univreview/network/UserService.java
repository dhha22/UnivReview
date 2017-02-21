package com.univreview.network;

import com.univreview.model.ProfileModel;
import com.univreview.model.Register;
import com.univreview.model.ResponseModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public interface UserService {
    @POST("signUp")
    Observable<ResponseModel> register(@HeaderMap Map<String, String> headers, @Body Register body);

    @GET("profile")
    Observable<ProfileModel> getProfile(@Query("userId") Long userId);

   /* @GET("pointHistory")
    Observable<>*/
}
