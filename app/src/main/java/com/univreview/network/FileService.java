package com.univreview.network;

import com.univreview.model.ResponseModel;

import retrofit2.http.Multipart;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public interface FileService {
    @Multipart
    @POST("file")
    Observable<ResponseModel> postFile();
}
