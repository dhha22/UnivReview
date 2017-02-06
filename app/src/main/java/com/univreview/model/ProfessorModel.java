package com.univreview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 2. 6..
 */
public class ProfessorModel implements Serializable {
    @SerializedName("professor")
    public List<Professor> professors = new ArrayList<>();

    @Override
    public String toString() {
        return "ProfessorModel{" +
                "professors=" + professors +
                '}';
    }
}
