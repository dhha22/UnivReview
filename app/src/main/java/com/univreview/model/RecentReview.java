package com.univreview.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DavidHa on 2017. 3. 8..
 */
public class RecentReview extends AbstractDataProvider implements Serializable {
    public long id;
    public long detailId;
    public String subjectName;
    public String professorName;
    public String reviewDetail;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String toString() {
        return "RecentReview{" +
                "id=" + id +
                ", detailId=" + detailId +
                ", subjectName='" + subjectName + '\'' +
                ", professorName='" + professorName + '\'' +
                ", reviewDetail='" + reviewDetail + '\'' +
                '}';
    }
}
