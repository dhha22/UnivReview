package com.univreview.model;

/**
 * Created by DavidHa on 2017. 2. 6..
 */
public class ReviewDetail {
    public long reviewId;
    public String reviewDetail;

    public boolean checkReviewDetail() {
        if (reviewId != 0 && reviewDetail.length() > 0) {
            return true;
        }
        return false;
    }
}
