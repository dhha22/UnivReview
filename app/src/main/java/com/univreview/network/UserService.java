package com.univreview.network;

import com.univreview.model.PointHistory;
import com.univreview.model.PointHistoryModel;
import com.univreview.model.User;
import com.univreview.model.UserModel;
import com.univreview.model.Register;
import com.univreview.model.ResponseModel;
import com.univreview.model.UserTicket;
import com.univreview.model.UserTicketModel;

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

    @POST("signUp")
    Observable<UserModel> register(@HeaderMap Map<String, String> headers, @Body Register body);

    @GET("profile")
    Observable<UserModel> getProfile(@HeaderMap Map<String, String> headers);
    //user id 제외
    @POST("profile/")
    Observable<UserModel> postProfile(@HeaderMap Map<String, String> headers, @Body User body, @Path("userId") long userId);


    //point
    @GET("pointHistory/{userId}")
    Observable<PointHistoryModel> getPoint(@HeaderMap Map<String, String> headers, @Path("userId") long userId, @Query("page") int page);

    //user id 제외
    @GET("userTicket/{userId}")
    Observable<UserTicketModel> getUserTicket(@HeaderMap Map<String, String> headers, @Path("userId") long userId);

    @POST("userTicket")
    Observable<UserTicket> postUserTicket(@HeaderMap Map<String, String> headers, @Body UserTicket body);


}
