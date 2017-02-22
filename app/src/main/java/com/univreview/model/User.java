package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class User implements Serializable{
    @Expose
    public long id;
    @Expose
    public String email;
    @Expose
    public String name;
    @Expose
    public String studentId;
    @Expose
    public String studentImageUrl;
    @Expose
    public int point;
    @Expose
    public boolean authenticated;

    @Expose
    public University university = new University();
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
                ", studentImageUrl='" + studentImageUrl + '\'' +
                ", point=" + point +
                ", authenticated=" + authenticated +
                '}';
    }
}
