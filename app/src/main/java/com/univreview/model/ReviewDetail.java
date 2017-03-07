package com.univreview.model;

/**
 * Created by DavidHa on 2017. 2. 6..
 */
public class ReviewDetail {
    public long reviewId;
    public String reviewDetail;

    public String getAlertMessage() {
        if(reviewId == 0){
            return "비 정상적인 접근입니다.";
        }else if(reviewDetail == null){
            return "리뷰를 적어주시길 바랍니다.";
        }else if(reviewDetail.length() < 5){
            return "최소 5글자 이상 적어주시길 바랍니다.";
        }else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "ReviewDetail{" +
                "reviewId=" + reviewId +
                ", reviewDetail='" + reviewDetail + '\'' +
                '}';
    }
}
