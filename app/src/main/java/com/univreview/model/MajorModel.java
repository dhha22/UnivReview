package com.univreview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 1. 19..
 */
public class MajorModel implements Serializable {
    @SerializedName("major")
    public List<Major> majors = new ArrayList<>();

    @Override
    public String toString() {
        return "MajorModel{" +
                "majors=" + majors +
                '}';
    }
}
