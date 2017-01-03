package com.univreview.network;

import com.univreview.model.ResponseModel;

import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public interface TokenService {
    @PUT("token")
    Observable<ResponseModel> refreshToken();

    @POST("tempToken")
    Observable<ResponseModel> tempToken();
}
