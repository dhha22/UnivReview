package com.univreview.network

import com.univreview.model.*
import retrofit2.http.*
import rx.Observable

/**
 * Created by DavidHa on 2017. 8. 23..
 */
interface LoginService {

    // 로그인
    @POST("auth/sign_in")
    fun signIn(@Body body: SignIn): Observable<UserModel>

    // 로그아웃
    @DELETE("auth/sign_out")
    fun signOut(@HeaderMap headers: Map<String, String>): Observable<UserModel>

    // 회원가입
    @POST("auth")
    fun signUp(@Body body: User): Observable<UserModel>

    // 회원탈퇴
    @DELETE("auth")
    fun deleteAuth(@HeaderMap headers: Map<String, String>): Observable<UserModel>

    // 이메일 유효성
    @GET("auth/isBoundEmail")
    fun validateEmail(@Query("email") email: String): Observable<DataModel<ValidateEmail>>

    // 사용자 이름 유효성
    @GET("auth/isBoundName")
    fun validateName(@Query("name") name : String) : Observable<DataModel<ValidateName>>

}