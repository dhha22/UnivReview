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
    public String reviewDetail;
    @Expose
    public long subjectId;
    @Expose
    public long userId;
    @Expose
    public long professorId;

    @Expose
    public User user = new User();

    @Expose
    public Professor professor = new Professor();

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return null;
    }


    public String getAlertMessage(){
        if(subjectId == 0){
            return "과목을 입력해주세요.";
        }else if(professorId == 0){
            return "교수명을 입력해주세요.";
        }else if(difficultyRate == 0){
            return "난이도를 평가해주세요.";
        }else if(assignmentRate == 0){
            return "과제량을 평가해주세요.";
        }else if(attendanceRate == 0){
            return "출석체크를 평가해주세요.";
        }else if(gradeRate == 0){
            return "학점을 평가해주세요.";
        }else if(achievementRate == 0){
            return "성취감을 평가해주세요.";
        }else{
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
                ", createdDate='" + createdDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", reviewDetail='" + reviewDetail + '\'' +
                ", subjectId=" + subjectId +
                ", userId=" + userId +
                ", professorId=" + professorId +
                ", user=" + user +
                ", professor=" + professor +
                '}';
    }
}
