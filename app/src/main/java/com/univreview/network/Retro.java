package com.univreview.network;

import android.net.Uri;

import com.univreview.App;
import com.univreview.BuildConfig;
import com.univreview.log.Logger;
import com.univreview.model.FileUploadModel;
import com.univreview.util.ImageUtil;
import com.univreview.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public enum Retro {
    instance;
    private String BASE_URL;
    private final String TEST_URL = "http://ec2-52-78-140-75.ap-northeast-2.compute.amazonaws.com/";
    private final String REAL_URL = "https://api.8hakgoon.com/api/";
    private final String VERSION = "v1/";
    private UserService userService;
    private LoginService loginService;
    private TokenService tokenService;
    private SearchService searchService;
    private ReviewService reviewService;
    private FileService fileService;

    Retro() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            BASE_URL = TEST_URL;
             //BASE_URL = REAL_URL;
        } else {
            BASE_URL = REAL_URL;
        }

        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
                .header("User-Agent", "Android")
                .header("Content-Type", "application/json")
                .build()));

        OkHttpClient client = builder.build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + VERSION)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(App.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        userService = retrofit.create(UserService.class);
        loginService = retrofit.create(LoginService.class);
        tokenService = retrofit.create(TokenService.class);
        searchService = retrofit.create(SearchService.class);
        reviewService = retrofit.create(ReviewService.class);
        fileService = retrofit.create(FileService.class);
    }

    public UserService userService(){
        return userService;
    }

    public LoginService loginService(){
        return loginService;
    }

    public TokenService tokenService(){
        return tokenService;
    }

    public SearchService searchService(){
        return searchService;
    }

    public ReviewService reviewService(){
        return reviewService;
    }

    public Observable<FileUploadModel> fileService(String path, String type) {
        Logger.v("path: " + path);

        final Thread thread = new Thread(() -> {
            try {
                ImageUtil.compressImage(path);
            } catch (Exception e) {
                Logger.e(e);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        File file = new File(ImageUtil.IMAGE_PATH + "tmp.jpg");
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody location = RequestBody.create(okhttp3.MultipartBody.FORM, "/" + type);
        return fileService.postFile(App.setAuthHeader(App.userToken), body, location);
    }
}
