package com.univreview.network

import com.univreview.App
import com.univreview.BuildConfig
import com.univreview.log.Logger
import com.univreview.util.ImageUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by DavidHa on 2017. 8. 10..
 */
enum class Retro {
    instance;

    companion object {
        const val VERSION = "api/v2/"
        const val IMAGE_URL_DEV = "https://s3.ap-northeast-2.amazonaws.com/univ-review-dev/"
        const val IMAGE_URL_PROD = "https://s3.ap-northeast-2.amazonaws.com/univ-review-prod/"
    }

    private val TEST_URL = "http://ec2-52-78-140-75.ap-northeast-2.compute.amazonaws.com/"
    private val REAL_URL = "https://api.8hakgoon.com/api/"
    var userService: UserService
    var loginService: LoginService
    var searchService: SearchService
    var reviewService: ReviewService
    var uploadService: UploadService

    init {
        Logger.v("init retro")

        val BASE_URL: String
        if (BuildConfig.DEBUG) {
            BASE_URL = TEST_URL
            //BASE_URL = REAL_URL
        } else {
            BASE_URL = REAL_URL
        }


        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(App.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        userService = retrofit.create(UserService::class.java)
        loginService = retrofit.create(LoginService::class.java)
        searchService = retrofit.create(SearchService::class.java)
        reviewService = retrofit.create(ReviewService::class.java)
        uploadService = retrofit.create(UploadService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {    // http 로그
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
            builder.addInterceptor {
                it.proceed(it.request().newBuilder()
                        .header("User-Agent", "Android")
                        .header("Content-Type", "application/json")
                        .build())
            }
        }
        return builder.build()
    }

    fun makeMultipartBody(imagePath: String): Observable<MultipartBody.Part> {
        return Observable.just(ImageUtil.compressImage(imagePath))
                .map {
                    val file = File(ImageUtil.IMAGE_PATH + "tmp.jpg")
                    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    MultipartBody.Part.createFormData("file", file.name, requestFile)
                }.subscribeOn(Schedulers.io())
    }

}