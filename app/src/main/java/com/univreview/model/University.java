package com.univreview.model;

import com.google.gson.annotations.Expose;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class University {
    @Expose
    public int id;
    @Expose
    public String name;

    @Override
    public String toString() {
        return "University{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
