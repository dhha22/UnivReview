package com.univreview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 2. 13..
 */
public class SearchModel  implements Serializable {
    @SerializedName("university")
    public List<University> universities;
    @SerializedName("major")
    public List<Major> majors;
    @SerializedName("department")
    public List<Department> departments;
    @SerializedName("professor")
    public List<Professor> professors;
    @SerializedName("subject")
    public List<Subject> subjects;



}
