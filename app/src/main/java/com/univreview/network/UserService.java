package com.univreview.network;

import com.univreview.model.PointHistoryModel;
import com.univreview.model.PushId;
import com.univreview.model.ResultMessage;
import com.univreview.model.User;
import com.univreview.model.UserModel;
import com.univreview.model.UserTicket;
import com.univreview.model.UserTicketModel;
import com.univreview.model.model_kotlin.DataListModel;
import com.univreview.model.model_kotlin.DataModel;
import com.univreview.model.model_kotlin.RvPoint;
import com.univreview.model.model_kotlin.Ticket;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
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


    @GET("profile")
    Observable<UserModel> getProfile(@HeaderMap Map<String, String> headers);

    @POST("profile/{userId}")
    Observable<UserModel> postProfile(@HeaderMap Map<String, String> headers, @Body User body, @Path("userId") long userId);

    //push 등록
    @POST("push/id")
    Observable<ResponseBody> postPushId(@HeaderMap Map<String, String> headers, @Body PushId pushId);
    


    // user review point
    @GET(Retro.VERSION + "point_histories")
    Observable<DataListModel<RvPoint>> callPointHistories(@HeaderMap Map<String, String> headers);

    // user review ticket
    @GET(Retro.VERSION + "tickets")
    Observable<DataListModel<Ticket>> callTicket(@HeaderMap Map<String, String> headers);

    @GET(Retro.VERSION + "tickets")
    Observable<DataListModel<Ticket>> buyReviewTicket(@HeaderMap Map<String, String> headers);

}
