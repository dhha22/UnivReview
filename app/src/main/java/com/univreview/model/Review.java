package com.univreview.model;

import com.google.gson.annotations.Expose;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class Review {
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

}
