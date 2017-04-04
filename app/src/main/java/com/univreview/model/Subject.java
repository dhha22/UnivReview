package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class Subject extends AbstractDataProvider  implements Serializable{
    @Expose
    public Long id;
    @Expose
    public String name;
    @Expose
    public String year; //학년
    @Expose
    public int point;   //학점

    @Expose
    public String subjectName;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
