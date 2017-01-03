package com.univreview.network;

import com.univreview.App;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public enum Retro {
    instance;
    private String BASE_URL;
    Retro(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
                .header("Accept", "application/json")
                .header("Client-Type", "Android")

                .build()));

        OkHttpClient client = builder.build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(App.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
