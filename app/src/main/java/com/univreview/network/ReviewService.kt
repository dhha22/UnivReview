package com.univreview.network

import com.univreview.model.*
import retrofit2.http.*
import rx.Observable

/**
 * Created by DavidHa on 2017. 11. 6..
 */
interface ReviewService {

    // 메인 최신 리뷰
    @GET(Retro.VERSION + "reviews/recent")
    fun callRecentReview(@HeaderMap headers: Map<String, String>): Observable<DataModel<RecentRvListModel>>

    // 리뷰 작성 (간편 리뷰)
    @POST(Retro.VERSION + "reviews")
    fun callPostReview(@HeaderMap headers: Map<String, String>, @Body review: Review): Observable<ReviewResponse>

    // 리뷰 수정 및 상세리뷰 작성
    @PUT(Retro.VERSION + "reviews/{reviewId}")
    fun callPutReview(@HeaderMap headers: Map<String, String>, @Path("reviewId") reviewId: Long, @Body body: ReviewDetail): Observable<ReviewResponse>

    // 사용자가 기존에 리뷰를 썼는지 확인하는 API
    @GET(Retro.VERSION + "courses/{courseId}/review/exists")
    fun callCheckReviewForCourseId(@HeaderMap headers: Map<String, String>, @Path("courseId") courseId: Long): Observable<ReviewState>

    // 리뷰 좋아요
    @POST(Retro.VERSION + "reviews/{reviewId}/likes")
    fun callReviewLike(@HeaderMap headers: Map<String, String>, @Path("reviewId") reviewId: Long): Observable<ResultModel>

    // 내 리뷰 리스트
    @GET(Retro.VERSION + "reviews/mine")
    fun callMyReview(@HeaderMap headers: Map<String, String>, @Query("page") page: Int): Observable<DataModel<ReviewListModel>>

    // 교수에 관한 리뷰 리스트
    @GET(Retro.VERSION + "professors/{professorId}/reviews")
    fun callReviewListByProfessor(@HeaderMap headers: Map<String, String>, @Path("professorId") professorId: Long, @Query("page") page: Int): Observable<DataModel<ReviewListModel>>

    // 과목에 관한 리뷰 리스트
    @GET(Retro.VERSION + "subjects/{subjectId}/reviews")
    fun callReviewListBySubject(@HeaderMap headers: Map<String, String>, @Path("subjectId") subjectId: Long, @Query("page") page: Int): Observable<DataModel<ReviewListModel>>

    // 과목, 교수에 관한 리뷰 리스트
    @GET(Retro.VERSION + "subjects/{subjectId}/professors/{professorId}/reviews")
    fun callReviewListBySubjAndProf(@HeaderMap headers: Map<String, String>, @Path("subjectId") subjectId: Long, @Path("professorId") professorId: Long, @Query("page") page: Int): Observable<DataModel<ReviewListModel>>

    // 리뷰 단일 조회
    @GET(Retro.VERSION + "reviews/{reviewId}")
    fun callReviewSingle(@HeaderMap headers: Map<String, String>, @Path("reviewId") reviewId: Long): Observable<DataModel<Review>>

    // 리뷰 댓글 조회
    @GET(Retro.VERSION + "reviews/{reviewId}/comments")
    fun callReviewComments(@HeaderMap headers: Map<String, String>, @Path("reviewId") reviewId: Long, @Query("page") page: Int): Observable<DataListModel<RvComment>>

    // 리뷰 댓글 등록
    @POST(Retro.VERSION + "reviews/{reviewId}/comments")
    fun postReviewComment(@HeaderMap headers: Map<String, String>, @Path("reviewId") reviewId: Long, @Body body: RvComment): Observable<DataModel<RvComment>>

    // 리뷰 댓글 삭제
    @DELETE(Retro.VERSION + "reviews/{reviewId}/comments/{commentId}")
    fun deleteReviewComment(@HeaderMap headers: Map<String, String>, @Path("reviewId") reviewId: Long, @Path("commentId") commentId: Long): Observable<ResultModel>

    // 리뷰 신고
    @POST(Retro.VERSION + "reviews/{reviewId}/reports")
    fun reviewReport(@HeaderMap headers: Map<String, String>, @Path("reviewId") reviewId: Long, @Body body: RvReport): Observable<ResultModel>


}