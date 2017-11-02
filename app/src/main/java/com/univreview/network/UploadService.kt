package com.univreview.network

import com.univreview.model.DataModel
import com.univreview.model.ImageUploadResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import rx.Single

/**
 * Created by DavidHa on 2017. 9. 24..
 */
interface UploadService {

    // 학생증 업로드
    @Multipart
    @POST(Retro.VERSION + "users/studentId")
    fun postStudentId(@Part student_id: MultipartBody.Part):Single<DataModel<ImageUploadResponse>>

    // 사용자 프로필 사진 업로드
    @Multipart
    @POST(Retro.VERSION + "users/profileImage")
    fun postProfileImage(@Part profile_image: MultipartBody.Part):Single<DataModel<ImageUploadResponse>>


}