package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 3. 7..
 */
public class MajorSubjectModel implements Serializable {
    @Expose
    public List<Major> major = new ArrayList<>();

    @Override
    public String toString() {
        return "MajorSubjectModel{" +
                "major=" + major +
                '}';
    }
}
