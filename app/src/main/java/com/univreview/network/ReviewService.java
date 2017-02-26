package com.univreview.network;

import com.univreview.model.Review;
import com.univreview.model.ReviewDetail;
import com.univreview.model.ReviewListModel;
import com.univreview.model.ReviewSingleModel;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 2. 6..
 */
public interface ReviewService {
    @GET("review")
    Observable<ReviewListModel> getReviews(@Query("subjectId") Long subjectId, @Query("professorId") Long professorId, @Query("page") int page);

    @POST("review")
    Observable<ReviewSingleModel> postSimpleReview(@Body Review body);

    @POST("reviewDetail")
    Observable<ReviewSingleModel> postDetailReview(@Body ReviewDetail body);
}
