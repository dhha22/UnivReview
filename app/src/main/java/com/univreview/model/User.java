package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class User implements Serializable{
    @Expose
    public Long id;
    @Expose
    public String email;
    @Expose
    public String name;
    @Expose
    public String studentId;
    @Expose
    public String studentImageUrl;  //학생증
    @Expose
    public String profileImageUrl;
    @Expose
    public Integer point;
    @Expose
    public Boolean authenticated;

    @Expose
    public Long universityId;

    @Expose
    public Department department = new Department();
    @Expose
    public Major major = new Major();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", studentId='" + studentId + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", point=" + point +
                ", authenticated=" + authenticated +
                ", department=" + department +
                ", major=" + major +
                '}';
    }
}
