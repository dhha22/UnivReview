package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class Review extends AbstractDataProvider implements Serializable{
    @Expose
    public long id;
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
    public ReviewDetail reviewDetail;
    @Expose
    public long subjectDetailId;
    @Expose
    public Long subjectId;
    @Expose
    public long userId;
    @Expose
    public long professorId;
    @Expose
    public String professorName;
    @Expose
    public String subjectName;

    @Expose
    public User user = new User();
    @Expose
    public String userName;
    @Expose
    public Boolean authenticated;

    @Expose
    public SubjectDetail subjectDetail = new SubjectDetail();


    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return null;
    }

    public String getDifficultyRateMessage() {
        switch (Math.round(difficultyRate)) {
            case 1:
                return "아주 쉬움";
            case 2:
                return "쉬움";
            case 3:
                return "보통";
            case 4:
                return "어려움";
            case 5:
                return "매우 어려움";
            default:
                return "";
        }
    }

    public String getAssignmentRateMessage() {
        switch (Math.round(assignmentRate)) {
            case 1:
                return "아주 적음";
            case 2:
                return "적음";
            case 3:
                return "보통";
            case 4:
                return "많음";
            case 5:
                return "매우 많음";
            default:
                return "";
        }
    }

    public String getAttendanceRateMessage() {
        switch (Math.round(attendanceRate)) {
            case 1:
                return "거의 드묾";
            case 2:
                return "드묾";
            case 3:
                return "보통";
            case 4:
                return "잦음";
            case 5:
                return "너무 잦음";
            default:
                return "";
        }
    }

    public String getGradeRateMessage() {
        switch (Math.round(gradeRate)) {
            case 1:
                return "아주 쉬움";
            case 2:
                return "쉬움";
            case 3:
                return "보통";
            case 4:
                return "어려움";
            case 5:
                return "매우 어려움";
            default:
                return "";
        }
    }

    public String getAchievementRateMessage() {
        switch (Math.round(achievementRate)) {
            case 1:
                return "매우 불만족";
            case 2:
                return "불만족";
            case 3:
                return "보통";
            case 4:
                return "만족";
            case 5:
                return "매우 만족";
            default:
                return "";
        }
    }


    public String getAlertMessage() {
        if (subjectId == null) {
            return "과목을 입력해주세요.";
        } else if (professorId == 0) {
            return "교수명을 입력해주세요.";
        } else if (difficultyRate == 0) {
            return "난이도를 평가해주세요.";
        } else if (assignmentRate == 0) {
            return "과제량을 평가해주세요.";
        } else if (attendanceRate == 0) {
            return "출석체크를 평가해주세요.";
        } else if (gradeRate == 0) {
            return "학점을 평가해주세요.";
        } else if (achievementRate == 0) {
            return "성취감을 평가해주세요.";
        } else {
            return null;
        }
    }



    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", difficultyRate=" + difficultyRate +
                ", assignmentRate=" + assignmentRate +
                ", attendanceRate=" + attendanceRate +
                ", gradeRate=" + gradeRate +
                ", achievementRate=" + achievementRate +
                ", reviewDetail='" + reviewDetail + '\'' +
                ", subjectDetailId=" + subjectDetailId +
                '}';
    }
}
