package com.univreview.network;

import com.univreview.model.PushId;
import com.univreview.model.model_kotlin.DataListModel;
import com.univreview.model.model_kotlin.DataModel;
import com.univreview.model.model_kotlin.RvPoint;
import com.univreview.model.model_kotlin.Ticket;
import com.univreview.model.model_kotlin.User;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public interface UserService {


    //push 등록
    @POST("push/id")
    Observable<ResponseBody> postPushId(@HeaderMap Map<String, String> headers, @Body PushId pushId);


    @GET(Retro.VERSION + "users/profile")
    Observable<DataModel<User>> callUserProfile(@HeaderMap Map<String, String> headers);

    // user review point
    @GET(Retro.VERSION + "point_histories")
    Observable<DataListModel<RvPoint>> callPointHistories(@HeaderMap Map<String, String> headers);

    // user review ticket
    @GET(Retro.VERSION + "tickets")
    Observable<DataListModel<Ticket>> callTicket(@HeaderMap Map<String, String> headers);

    @POST(Retro.VERSION + "tickets")
    Observable<DataModel<Ticket>> buyReviewTicket(@HeaderMap Map<String, String> headers);

}
