package com.univreview.network;

import com.univreview.model.LikeResponse;
import com.univreview.model.RecentReviewModel;
import com.univreview.model.Review;
import com.univreview.model.ReviewComment;
import com.univreview.model.ReviewCommentListModel;
import com.univreview.model.ReviewDetail;
import com.univreview.model.ReviewExist;
import com.univreview.model.ReviewLike;
import com.univreview.model.ReviewListModel;
import com.univreview.model.ReviewModel;
import com.univreview.model.ReviewReport;
import com.univreview.model.ReviewSingleModel;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @GET("review/{id}")
    Observable<ReviewModel> getReview(@HeaderMap Map<String, String> headers, @Path("id") long reviewId);

    @POST("review")
    Observable<ReviewSingleModel> postSimpleReview(@HeaderMap Map<String, String> headers, @Body Review body);

    @POST("reviewDetail")
    Observable<ReviewSingleModel> postDetailReview(@HeaderMap Map<String, String> headers, @Body ReviewDetail body);

    // 리뷰 수정
    @PUT("reviewDetail/{id}")
    Observable<ResponseBody> putReviewDetail(@HeaderMap Map<String, String> headers, @Path("id") long reviewDetailId, @Body ReviewDetail body);

    //  리뷰 신고
    @POST("report")
    Observable<ReviewReport> postReviewReport(@HeaderMap Map<String, String> headers, @Body ReviewReport body);

    @GET("recent/main")
    Observable<RecentReviewModel> getRecentReview(@HeaderMap Map<String, String> headers);

    @GET("recent/user")
    Observable<ReviewListModel> getMyReviews(@HeaderMap Map<String, String> headers, @Query("page") int page);

    @GET("review/exist")
    Observable<ReviewExist> getReviewExist(@HeaderMap Map<String, String> headers, @Query("subjectId") long subjectId);

    //  리뷰 댓글
    @GET("comment/review")
    Observable<ReviewCommentListModel> getReviewComment(@HeaderMap Map<String, String> headers, @Query("reviewId") long reviewId, @Query("page") int page);

    @POST("comment/review")
    Observable<ReviewComment> postReviewComment(@HeaderMap Map<String, String> headers, @Body ReviewComment body);

    @DELETE("comment/review/{commentId}")
    Observable<ResponseBody> deleteComment(@HeaderMap Map<String, String> headers,  @Path("commentId") long id);

    // 리뷰 좋아요
    @POST("like/review")
    Observable<LikeResponse> likeReview(@HeaderMap Map<String, String> headers, @Body ReviewLike body);

}
