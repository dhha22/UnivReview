package com.univreview.model;

import com.google.gson.annotations.Expose;

/**
 * Created by DavidHa on 2017. 6. 29..
 */

public class ReviewLike {
    @Expose
    public long reviewId;

    public ReviewLike(long reviewId) {
        this.reviewId = reviewId;
    }
}
