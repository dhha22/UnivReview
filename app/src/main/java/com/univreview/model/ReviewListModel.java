package com.univreview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 2. 6..
 */
public class ReviewListModel implements Serializable {
    @SerializedName("review")
    public List<Review> reviews = new ArrayList<>();
    @SerializedName("reviewAvg")
    public ReviewAverage reviewAverage = new ReviewAverage();

    public Review getReview() {
        if (reviews.size() > 0) {
            return reviews.get(0);
        }
        return null;
    }

    @Override
    public String toString() {
        return "ReviewListModel{" +
                "reviews=" + reviews +
                '}';
    }
}
