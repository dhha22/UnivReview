package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 5. 31..
 */

public class ReviewComment extends AbstractDataProvider{
    @Expose
    public long id;
    @Expose
    public long userId;
    @Expose
    public String name;
    @Expose
    public String commentDetail;
    @Expose
    public Long reviewId;
    @Expose
    public String createdDate;
    @Expose
    public String commentType;
    @Expose
    public Long parentCommentId;
    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return commentDetail;
    }

    @Override
    public String toString() {
        return "ReviewComment{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", commentDetail='" + commentDetail + '\'' +
                ", reviewId=" + reviewId +
                ", createdDate='" + createdDate + '\'' +
                ", commentType='" + commentType + '\'' +
                '}';
    }
}
