package com.univreview.network

import com.univreview.model.SignIn
import com.univreview.model.model_kotlin.User
import com.univreview.model.model_kotlin.UserModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import rx.Observable

/**
 * Created by DavidHa on 2017. 8. 23..
 */
interface LoginService {

    // 로그인
    @POST("auth/sign_in")
    fun callSignIn(@Body body : SignIn) : Observable<UserModel>

    // 로그아웃
    @DELETE("auth/sign_out")
    fun callSignOut(@HeaderMap headers : Map<String, String>) : Observable<UserModel>

    // 회원가입
    @POST("auth")
    fun callSignUp(@Body body : User) : Observable<UserModel>

    // 회원탈퇴
    @DELETE("auth")
    fun callDeleteAuth(@HeaderMap headers : Map<String, String>) : Observable<UserModel>

}