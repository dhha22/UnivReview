package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 2. 21..
 */
public class ProfileModel implements Serializable {
    @Expose
    public User user = new User();
    @Expose
    public University university = new University();
    @Expose
    public Department department = new Department();
    @Expose
    public Major major = new Major();

}
