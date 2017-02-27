package com.univreview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 2. 27..
 */
public class PointHistoryModel implements Serializable {
    @SerializedName("pointHistory")
    public List<PointHistory> pointHistories = new ArrayList<>();

    @Override
    public String toString() {
        return "PointHistoryModel{" +
                "pointHistories=" + pointHistories +
                '}';
    }
}
