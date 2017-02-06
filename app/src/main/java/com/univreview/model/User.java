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

}
