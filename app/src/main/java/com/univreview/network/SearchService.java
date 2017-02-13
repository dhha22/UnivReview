package com.univreview.network;

import com.univreview.model.Department;
import com.univreview.model.DepartmentModel;
import com.univreview.model.Major;
import com.univreview.model.MajorModel;
import com.univreview.model.ProfessorModel;
import com.univreview.model.SearchModel;
import com.univreview.model.SubjectModel;
import com.univreview.model.University;

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

    @GET("department")
    Observable<SearchModel> getDepartments(@Query("universityId") long universityId, @Query("name") String departName, @Query("page") int page);

    @GET("major")
    Observable<SearchModel> getMajors(@Query("departmentId") long departmentId, @Query("name") String majorName, @Query("page") int page);

    @GET("subject")
    Observable<SearchModel> getSubjects(@Query("majorId") long majorId, @Query("name") String subjectName, @Query("page") int page);

    @GET("professor")
    Observable<SearchModel> getProfessors(@Query("departmentId") long departmentId, @Query("name") String professorName, @Query("page") int page);
}
