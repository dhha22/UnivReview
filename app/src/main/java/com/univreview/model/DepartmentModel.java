package com.univreview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 1. 19..
 */
public class DepartmentModel implements Serializable {
    @SerializedName("department")
    public List<Department> departments = new ArrayList<>();

    @Override
    public String toString() {
        return "DepartmentModel{" +
                "departments=" + departments +
                '}';
    }
}
