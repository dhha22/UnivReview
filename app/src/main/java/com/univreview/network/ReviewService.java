package com.univreview.network;

import com.univreview.model.RecentReviewModel;
import com.univreview.model.Review;
import com.univreview.model.ReviewDetail;
import com.univreview.model.ReviewExist;
import com.univreview.model.ReviewListModel;
import com.univreview.model.ReviewSingleModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 2. 6..
 */
public interface ReviewService {
    @GET("review")
    Observable<ReviewListModel> getReviews(@HeaderMap Map<String, String> headers, @Query("subjectId") Long subjectId, @Query("professorId") Long professorId, @Query("userId") Long userId, @Query("page") int page);

    @POST("review")
    Observable<ReviewSingleModel> postSimpleReview(@HeaderMap Map<String, String> headers, @Body Review body);

    @POST("reviewDetail")
    Observable<ReviewSingleModel> postDetailReview(@HeaderMap Map<String, String> headers, @Body ReviewDetail body);

    // 리뷰 수정
    @PUT("subjectDetail/{subjectDetailId}")
    Observable<ReviewDetail> putReviewDetail(@HeaderMap Map<String, String> headers, @Path("subjectDetailId") long subjectDetailId);

    @GET("recent/main")
    Observable<RecentReviewModel> getRecentReview(@HeaderMap Map<String, String> headers);

    @GET("recent/user")
    Observable<ReviewListModel> getMyReviews(@HeaderMap Map<String, String> headers, @Query("page") int page);

    @GET("review/exist")
    Observable<ReviewExist> getReviewExist(@HeaderMap Map<String, String> headers, @Query("subjectId") long subjectId);

}
