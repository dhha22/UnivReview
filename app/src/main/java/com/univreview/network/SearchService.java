/*
package com.univreview.network;

import com.univreview.model.SearchModel;
import com.univreview.model.model_kotlin.Course;
import com.univreview.model.model_kotlin.DataListModel;
import com.univreview.model.model_kotlin.Department;
import com.univreview.model.model_kotlin.Major;
import com.univreview.model.model_kotlin.Professor;
import com.univreview.model.model_kotlin.Subject;
import com.univreview.model.model_kotlin.University;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

*/
/**
 * Created by DavidHa on 2017. 1. 16..
 *//*

public interface SearchService {

    @GET(Retro.VERSION + "universities")
    Observable<DataListModel<University>> callUniversityList(@Query("name") String name);

    @GET(Retro.VERSION + "departments")
    Observable<DataListModel<Department>> callDepartmentList(@Query("university_id") long universityId, @Query("name") String name);

    @GET(Retro.VERSION + "majors")
    Observable<DataListModel<Major>> callMajorList(@Query("university_id") long universityId, @Query("department_id") long departmentId, @Query("name") String name);

    @GET(Retro.VERSION +"subjects")
    Observable<DataListModel<Subject>> callSubjects(@HeaderMap Map<String, String> headers, @Query("major_id") Long majorId, @Query("name") String name);

    @GET(Retro.VERSION +"professors")
    Observable<DataListModel<Professor>> callProfessors(@HeaderMap Map<String, String> headers, @Query("name") String name);

    @GET(Retro.VERSION + "subjects/{subjectId}/courses")
    Observable<DataListModel<Course>> callCourse(@HeaderMap Map<String, String> headers, @Path("subjectId") long subjectId);

}
*/
