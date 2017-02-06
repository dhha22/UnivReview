package com.univreview.network;

import com.univreview.model.Review;
import com.univreview.model.ReviewDetail;
import com.univreview.model.ReviewModel;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 2. 6..
 */
public interface ReviewService {
    @GET("review")
    Observable<ReviewModel> getReviews(@Query("subjectId") int subjectId);

    @POST("review")
    Observable<ReviewModel> postSimpleReview(@Body Review body);

    @POST("reviewdetail")
    Observable<ReviewModel> postDetailReview(@Body ReviewDetail body);
}
