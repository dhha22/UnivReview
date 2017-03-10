package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class Professor extends AbstractDataProvider implements Serializable{
    @Expose
    public Long id;
    @Expose
    public String name;

    @Expose
    public List<SubjectDetail> subjectDetail = new ArrayList<>();

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
