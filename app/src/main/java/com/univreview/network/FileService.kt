package com.univreview.network

import com.univreview.model.FileUploadModel
import com.univreview.model.DataModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import rx.Observable

/**
 * Created by DavidHa on 2017. 9. 24..
 */
interface FileService {
    @Multipart
    @POST(Retro.VERSION + "files")
    fun postFile(@Part file: MultipartBody.Part, @Part("location") location: RequestBody): Observable<DataModel<FileUploadModel>>
}