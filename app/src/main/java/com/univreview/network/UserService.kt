package com.univreview.network

import com.univreview.model.*
import retrofit2.http.*
import rx.Observable

/**
 * Created by DavidHa on 2017. 9. 24..
 */
interface UserService {

    // user get profile image
    @GET(Retro.VERSION + "users/profile")
    fun callUserProfile(@HeaderMap headers: Map<String, String>): Observable<DataModel<User>>

    @PUT("auth")
    fun updateUserInfo(@HeaderMap headers: Map<String, String>, @Body user: UpdateUser): Observable<DataModel<User>>

    // user review point
    @GET(Retro.VERSION + "point_histories")
    fun callPointHistories(@HeaderMap headers: Map<String, String>): Observable<DataModel<PointHistory>>

    // user review ticket
    @GET(Retro.VERSION + "tickets")
    fun callTicket(@HeaderMap headers: Map<String, String>): Observable<DataModel<Ticket>>

    @POST(Retro.VERSION + "tickets")
    fun buyReviewTicket(@HeaderMap headers: Map<String, String>): Observable<DataModel<Ticket>>
}