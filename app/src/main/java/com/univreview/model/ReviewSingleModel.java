package com.univreview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 2. 18..
 */
public class ReviewSingleModel implements Serializable {
    @SerializedName("review")
    public Review review = new Review();

    @Override
    public String toString() {
        return "ReviewSingleModel{" +
                "review=" + review +
                '}';
    }
}
