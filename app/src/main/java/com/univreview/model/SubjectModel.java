package com.univreview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 2. 6..
 */
public class SubjectModel implements Serializable {
    @SerializedName("subject")
    public List<Subject> subjects = new ArrayList<>();

    @Override
    public String toString() {
        return "SubjectModel{" +
                "subjects=" + subjects +
                '}';
    }
}
