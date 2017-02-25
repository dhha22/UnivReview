package com.univreview.network;

import com.univreview.model.PointHistory;
import com.univreview.model.UserModel;
import com.univreview.model.Register;
import com.univreview.model.ResponseModel;
import com.univreview.model.UserTicket;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public interface UserService {
    @FormUrlEncoded
    @POST("signUp")
    Observable<UserModel> register(@HeaderMap Map<String, String> headers, @Body Register body);

    @GET("profile/")
    Observable<UserModel> getProfile(@HeaderMap Map<String, String> headers);


    //point
    @GET("pointHistory")
    Observable<PointHistory> getPoint();

    @GET("userTicket/{userId}")
    Observable<UserTicket> getUserTicket();

}
