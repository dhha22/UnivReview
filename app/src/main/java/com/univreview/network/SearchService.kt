package com.univreview.network

import com.univreview.model.model_kotlin.*
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
    fun callUniversityList(@Query("name") name: String): Observable<DataListModel<University>>

    @GET(Retro.VERSION + "departments")
    fun callDepartmentList(@Query("university_id") universityId: Long, @Query("name") name: String): Observable<DataListModel<Department>>

    @GET(Retro.VERSION + "majors")
    fun callMajorList(@Query("university_id") universityId: Long, @Query("department_id") departmentId: Long, @Query("name") name: String): Observable<DataListModel<Major>>

    @GET(Retro.VERSION + "subjects")
    fun callSubjects(@HeaderMap headers: Map<String, String>, @Query("major_id") majorId: Long?, @Query("name") name: String): Observable<DataListModel<Subject>>

    @GET(Retro.VERSION + "professors")
    fun callProfessors(@HeaderMap headers: Map<String, String>, @Query("name") name: String): Observable<DataListModel<Professor>>

    @GET(Retro.VERSION + "subjects/{subjectId}/courses")
    fun callCourse(@HeaderMap headers: Map<String, String>, @Path("subjectId") subjectId: Long): Observable<DataListModel<Course>>
}