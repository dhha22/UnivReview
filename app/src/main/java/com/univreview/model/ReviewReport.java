package com.univreview.model;

/**
 * Created by DavidHa on 2017. 4. 8..
 */
public class ReviewReport {
    public long id;
    public String message;
    public long reviewId;

    public ReviewReport() {
    }

    public ReviewReport(long reviewId, String message) {
        this.message = message;
        this.reviewId = reviewId;
    }

    @Override
    public String toString() {
        return "ReviewReport{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", reviewId=" + reviewId +
                '}';
    }
}
