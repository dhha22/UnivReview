package com.univreview.network

import com.univreview.App
import com.univreview.BuildConfig
import com.univreview.log.Logger
import com.univreview.model.FileUploadModel
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
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by DavidHa on 2017. 8. 10..
 */
enum class Retro {
    instance;
    companion object {
        @JvmField
        val VERSION = "api/v2/"
    }

    private val TEST_URL = "http://ec2-52-78-140-75.ap-northeast-2.compute.amazonaws.com/"
    private val REAL_URL = "https://api.8hakgoon.com/api/"
    private var userService: UserService
    private var loginService: LoginService
    private var tokenService: TokenService
    private var searchService: SearchService
    private var reviewService: ReviewService
    private var fileService: FileService

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
        tokenService = retrofit.create(TokenService::class.java)
        searchService = retrofit.create(SearchService::class.java)
        reviewService = retrofit.create(ReviewService::class.java)
        fileService = retrofit.create(FileService::class.java)
    }

    fun createOkHttpClient(): OkHttpClient {
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

    fun userService(): UserService {
        return userService
    }

    fun loginService(): LoginService {
        return loginService
    }

    fun tokenService(): TokenService {
        return tokenService
    }

    fun searchService(): SearchService {
        return searchService
    }

    fun reviewService(): ReviewService {
        return reviewService
    }

    fun fileService(path: String, type: String): Observable<FileUploadModel> {
        Logger.v("path: " + path)

        val thread = Thread {
            try {
                ImageUtil.compressImage(path)
            } catch (e: Exception) {
                Logger.e(e)
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: Exception) {
            Logger.e(e.toString())
        }

        val file = File(ImageUtil.IMAGE_PATH + "tmp.jpg")
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val location = RequestBody.create(okhttp3.MultipartBody.FORM, "/" + type)
        return fileService.postFile(App.setAuthHeader(App.userToken), body, location)
    }

}