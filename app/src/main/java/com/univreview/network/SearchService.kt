package com.univreview.network

import com.univreview.model.*
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * Created by DavidHa on 2017. 8. 27..
 */
interface SearchService {
    @GET(Retro.VERSION + "universities")
    fun callUniversityList(@Query("name") name: String, @Query("page") page: Int): Observable<DataListModel<SearchResult>>

    // subjectType : M 교양제외
    @GET(Retro.VERSION + "majors")
    fun callMajorList(@Query("subjectType") subjectType: String, @Query("university_id") universityId: Long?, @Query("name") name: String, @Query("page") page: Int): Observable<DataListModel<SearchResult>>

    @GET(Retro.VERSION + "subjects")
    fun callSubjects(@HeaderMap headers: Map<String, String>, @Query("major_id") majorId: Long?, @Query("name") name: String, @Query("page") page: Int): Observable<DataListModel<SearchResult>>

    @GET(Retro.VERSION + "subjects/{subjectId}/courses")
    fun callCourse(@HeaderMap headers: Map<String, String>, @Path("subjectId") subjectId: Long?, @Query("page") page: Int): Observable<DataListModel<SearchResult>>

}