package com.univreview.model;

/**
 * Created by DavidHa on 2017. 4. 8..
 */
public class ReviewReport {
    public long id;
    public String message;
    public long reviewDetailId;

    public ReviewReport() {
    }

    public ReviewReport(long reviewDetailId, String message) {
        this.message = message;
        this.reviewDetailId = reviewDetailId;
    }
}
