package com.univreview.model;

import com.google.gson.annotations.Expose;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class Review extends AbstractDataProvider{
    @Expose
    public int id;
    @Expose
    public int difficultyRate;
    @Expose
    public int assignmentRate;
    @Expose
    public int attendanceRate;
    @Expose
    public int gradeRate;
    @Expose
    public int achievementRate;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return null;
    }
}
