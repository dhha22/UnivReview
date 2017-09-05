package com.univreview.network;

import com.univreview.model.ReviewComment;
import com.univreview.model.ReviewReport;
import com.univreview.model.model_kotlin.DataListModel;
import com.univreview.model.model_kotlin.DataModel;
import com.univreview.model.model_kotlin.RecentRvListModel;
import com.univreview.model.model_kotlin.ResultModel;
import com.univreview.model.model_kotlin.Review;
import com.univreview.model.model_kotlin.ReviewListModel;
import com.univreview.model.model_kotlin.ReviewResponse;
import com.univreview.model.model_kotlin.ReviewState;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by DavidHa on 2017. 2. 6..
 */
public interface ReviewService {

    // 메인 최신 리뷰
    @GET(Retro.VERSION + "reviews/recent")
    Observable<DataModel<RecentRvListModel>> callRecentReview(@HeaderMap Map<String, String> headers);

    // 리뷰 작성 (간편 리뷰)
    @POST(Retro.VERSION + "reviews")
    Observable<ReviewResponse> callPostReview(@HeaderMap Map<String, String> headers, @Body Review review);

    // 리뷰 수정 및 상세리뷰 작성
    @PUT(Retro.VERSION + "reviews/{reviewId}")
    Observable<ReviewResponse> callPutReview(@HeaderMap Map<String, String> headers, @Path("reviewId") long reviewId, @Body com.univreview.model.model_kotlin.ReviewDetail body);

    // 사용자가 기존에 리뷰를 썼는지 확인하는 API
    @GET(Retro.VERSION + "courses/{courseId}/review/exists")
    Observable<ReviewState> callCheckReviewForCourseId(@HeaderMap Map<String, String> headers, @Path("courseId") long courseId);

    // 리뷰 좋아요
    @POST(Retro.VERSION + "reviews/{reviewId}/likes")
    Observable<ResultModel> callReviewLike(@HeaderMap Map<String, String> headers, @Path("reviewId") long reviewId);

    // 내 리뷰 리스트
    @GET(Retro.VERSION + "reviews/mine")
    Observable<DataModel<ReviewListModel>> callMyReview(@HeaderMap Map<String, String> headers);

    // 교수에 관한 리뷰 리스트
    @GET(Retro.VERSION + "professors/{professorId}/reviews")
    Observable<DataModel<ReviewListModel>> callReviewListByProfessor(@HeaderMap Map<String, String> headers, @Path("professorId") long professorId);

    // 과목에 관한 리뷰 리스트
    @GET(Retro.VERSION + "subjects/{subjectId}/reviews")
    Observable<DataModel<ReviewListModel>> callReviewListBySubject(@HeaderMap Map<String, String> headers, @Path("subjectId") long subjectId);

    // 과목, 교수에 관한 리뷰 리스트
    @GET(Retro.VERSION + "subjects/{subjectId}/professors/{professorId}/reviews")
    Observable<DataModel<ReviewListModel>> callReviewListBySubjAndProf(@HeaderMap Map<String, String> headers, @Path("subjectId") long subjectId, @Path("professorId") long professorId);

    // 리뷰 단일 조회
    @GET(Retro.VERSION + "reviews/{reviewId}")
    Observable<DataModel<Review>> callReviewSingle(@HeaderMap Map<String, String> headers, @Path("reviewId") long reviewId);

    // 리뷰 댓글 조회
    @GET(Retro.VERSION + "reviews/{reviewId}/review_comments")
    Observable<DataListModel> callReviewComments(@HeaderMap Map<String, String> headers, @Path("reviewId") long reviewId);

    // 리뷰 댓글 등록
    @POST(Retro.VERSION + "reviews/{reviewId}/review_comments")
    Observable<DataModel> postReviewComment(@HeaderMap Map<String, String> headers, @Path("reviewId") long reviewId);



    @POST("report")
    Observable<ReviewReport> postReviewReport(@HeaderMap Map<String, String> headers, @Body ReviewReport body);


    @POST("comment/review")
    Observable<ReviewComment> postReviewComment(@HeaderMap Map<String, String> headers, @Body ReviewComment body);

    @DELETE("comment/review/{commentId}")
    Observable<ResponseBody> deleteComment(@HeaderMap Map<String, String> headers, @Path("commentId") long id);


}
