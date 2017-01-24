package com.univreview.model;

import com.google.gson.annotations.Expose;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class Major extends AbstractDataProvider{
    @Expose
    public int id;
    @Expose
    public String name;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
