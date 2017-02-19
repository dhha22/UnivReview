package com.univreview.network;

import com.univreview.model.ResponseModel;
import com.univreview.model.Token;

import java.util.Map;

import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public interface TokenService {
    @PUT("userToken")
    Observable<Token> refreshToken(@HeaderMap Map<String, String> headers);

    @POST("tempToken")
    Observable<Token> tempToken(@HeaderMap Map<String, String> headers);
}
