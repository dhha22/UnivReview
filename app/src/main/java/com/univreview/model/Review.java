package com.univreview.model;

import com.google.gson.annotations.Expose;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class Review extends AbstractDataProvider{
    @Expose
    public int id;
    @Expose
    public float difficultyRate;
    @Expose
    public float assignmentRate;
    @Expose
    public float attendanceRate;
    @Expose
    public float gradeRate;
    @Expose
    public float achievementRate;
    @Expose
    public String createdDate;
    @Expose
    public String updateDate;
    @Expose
    public String reviewDetail;
    @Expose
    public int subjectId;
    @Expose
    public int userId;
    @Expose
    public int professorId;

    @Expose
    public User user = new User();

    @Expose
    public Professor professor = new Professor();

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return null;
    }

    public boolean checkReviewRating() {
        if (difficultyRate != 0 && assignmentRate != 0
                && attendanceRate != 0 && gradeRate != 0 && achievementRate != 0) {
            return true;
        }
        return false;
    }
}
