package com.univreview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 2. 13..
 */
public class SearchModel implements Serializable {
    @SerializedName("university")
    public List<University> universities = new ArrayList<>();
    @SerializedName("major")
    public List<Major> majors = new ArrayList<>();
    @SerializedName("department")
    public List<Department> departments = new ArrayList<>();




    @Override
    public String toString() {
        if(universities.size()>0){
            return universities.toString();
        }else if(majors.size()>0){
            return majors.toString();
        }else if(departments.size()>0){
            return departments.toString();
        }
        return super.toString();
    }
}
