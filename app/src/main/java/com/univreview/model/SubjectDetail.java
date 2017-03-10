package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 3. 7..
 */
public class SubjectDetail implements Serializable {
    @Expose
    public long id;
    @Expose
    public long subjectId;
    public Subject subject;
    public Professor professor;
}
