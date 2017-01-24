package com.univreview.network;

import com.univreview.model.Register;
import com.univreview.model.ResponseModel;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public interface UserService {
    @POST("user")
    Observable<ResponseModel> register(@Body Register body);
}
