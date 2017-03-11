package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 3. 11..
 */
public class ReviewAverage implements Serializable {
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

    @Override
    public String toString() {
        return "ReviewAverage{" +
                "difficultyRate=" + difficultyRate +
                ", assignmentRate=" + assignmentRate +
                ", attendanceRate=" + attendanceRate +
                ", gradeRate=" + gradeRate +
                ", achievementRate=" + achievementRate +
                '}';
    }
}
