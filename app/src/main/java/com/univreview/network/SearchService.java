package com.univreview.network;

import com.univreview.model.Department;
import com.univreview.model.Major;
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
    Observable<List<University>> getUniversities();

    @GET("department")
    Observable<List<Department>> getDepartments(@Query("university") int university, @Query("name") String departName);

    @GET("major")
    Observable<List<Major>> getMajors(@Query("department") int departmentId, @Query("name") String majorName);
}
