package com.univreview.model;

import com.google.gson.annotations.Expose;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class User {
    @Expose
    public int id;
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

}
