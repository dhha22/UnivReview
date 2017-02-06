package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class Professor extends AbstractDataProvider implements Serializable{
    @Expose
    public long id;
    @Expose
    public String name;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
