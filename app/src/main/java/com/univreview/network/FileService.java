package com.univreview.network;

import com.univreview.model.FileUploadModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public interface FileService {
    @Multipart
    @POST("file")
    Observable<FileUploadModel> postFile(@Part MultipartBody.Part file, @Part("location") RequestBody location);
}
