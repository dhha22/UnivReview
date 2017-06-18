package com.univreview.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 6. 5..
 */

public class ReviewCommentListModel {
    @SerializedName("comment")
    public List<ReviewComment> comments = new ArrayList<>();

    @Override
    public String toString() {
        return "ReviewCommentListModel{" +
                "comments=" + comments +
                '}';
    }
}
