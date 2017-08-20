package com.univreview.network;

import com.univreview.model.MajorSubjectModel;
import com.univreview.model.SearchModel;
import com.univreview.model.model_kotlin.DataListModel;
import com.univreview.model.model_kotlin.Department;
import com.univreview.model.model_kotlin.DepartmentListModel;
import com.univreview.model.model_kotlin.Major;
import com.univreview.model.model_kotlin.ResultModel;
import com.univreview.model.model_kotlin.University;
import com.univreview.model.model_kotlin.UniversityListModel;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 1. 16..
 */
public interface SearchService {
    @GET("university")
    Observable<SearchModel> getUniversities(@Query("name") String universityName, @Query("page") int page);


    @GET(Retro.VERSION + "universities")
    Observable<DataListModel<University>> callUniversityList();

    @GET(Retro.VERSION + "departments")
    Observable<DataListModel<Department>> callDepartmentList(@Query("university_id") long universityId);

    @GET(Retro.VERSION + "majors")
    Observable<DataListModel<Major>> callMajorList(@Query("university_id") long universityId, @Query("department_id") long departmentId);


    // M : 전공 , C: 교양
    @GET("department")
    Observable<SearchModel> getDepartments(@Query("universityId") long universityId, @Query("name") String departName, @Query("subjectType") String subjectType, @Query("page") int page);

    @GET("major")
    Observable<SearchModel> getMajors(@Query("universityId") long universityId, @Query("departmentId") Long departmentId, @Query("name") String majorName, @Query("subjectType") String subjectType, @Query("page") int page);

    @GET("subject")
    Observable<SearchModel> getSubjects(@Query("universityId") long universityId, @Query("majorId") Long majorId, @Query("name") String subjectName, @Query("page") int page);

    @GET("professor")
    Observable<SearchModel> getProfessors(@Query("universityId") long universityId, @Query("departmentId") Long departmentId, @Query("name") String professorName, @Query("page") int page);

    @GET("majorSubject")
    Observable<MajorSubjectModel> getMajorSubject(@Query("universityId") long universityId);

    @GET("professorSubject")
    Observable<SearchModel> getProfessorFromSubject(@Query("subjectId") long subjectId, @Query("page") int page);

    @GET("subjectProfessor")
    Observable<SearchModel> getSubjectFromProfessor(@Query("professorId") long professorId);

}
