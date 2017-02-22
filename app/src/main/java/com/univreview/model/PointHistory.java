package com.univreview.model;

import com.google.gson.annotations.Expose;

/**
 * Created by DavidHa on 2017. 2. 22..
 */
public class PointHistory extends AbstractDataProvider {
    @Expose
    public long id;
    @Expose
    public String point;
    @Expose
    public String historyDate;
    @Expose
    private String pointType;

    public boolean getPointType() {
        if ("add".equals(pointType)) {
            return true;
        }
        return false;
    }


    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return point;
    }
}

