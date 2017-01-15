package com.univreview.network;

import com.univreview.App;
import com.univreview.model.ResponseModel;
import com.univreview.model.Token;
import com.univreview.util.Util;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public enum Retro {
    instance;
    private final String BASE_URL = "http://52.78.219.48/api/";
    private final String VERSION = "v1/";
    private LoginService loginService;
    private TokenService tokenService;

    Retro() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
                .header("Content-Type", "application/json")
                .build()));

        OkHttpClient client = builder.build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + VERSION)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(App.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        loginService = retrofit.create(LoginService.class);
        tokenService = retrofit.create(TokenService.class);
    }

    public LoginService loginService(){
        return loginService;
    }

    public TokenService tokenService(){
        return tokenService;
    }
}
