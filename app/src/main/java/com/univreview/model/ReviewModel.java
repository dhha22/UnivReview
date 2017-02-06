package com.univreview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 2. 6..
 */
public class ReviewModel implements Serializable {
    @SerializedName("review")
    public List<Review> reviews = new ArrayList<>();

    @Override
    public String toString() {
        return "ReviewModel{" +
                "reviews=" + reviews +
                '}';
    }
}
