package com.univreview.network;

import com.univreview.model.MajorSubjectModel;
import com.univreview.model.SearchModel;

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
    Observable<SearchModel> getMajors(@Query("universityId") long universityId, @Query("departmentId") Long departmentId, @Query("name") String majorName, @Query("page") int page);

    @GET("subject")
    Observable<SearchModel> getSubjects(@Query("universityId") long universityId, @Query("majorId") Long majorId, @Query("name") String subjectName, @Query("page") int page);

    @GET("professor")
    Observable<SearchModel> getProfessors(@Query("universityId") long universityId, @Query("departmentId") Long departmentId, @Query("name") String professorName, @Query("page") int page);

    @GET("majorSubject")
    Observable<MajorSubjectModel> getMajorSubject(@Query("universityId") long universityId);

    @GET("professorSubject")
    Observable<SearchModel> getProfessorSubject(@Query("subjectId") long subjectId);

    @GET("subjectProfessor")
    Observable<SearchModel> getSubjectProfessor(@Query("professorId") long professorId);

}
