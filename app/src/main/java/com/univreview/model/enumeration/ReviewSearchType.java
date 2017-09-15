package com.univreview.model.enumeration;

/**
 * Created by DavidHa on 2017. 7. 14..
 */

public enum ReviewSearchType {
    UNIVERSITY("university"),
    SUBJECT("subject"),
    DEPARTMENT("department"),
    MAJOR("major"),
    PROFESSOR("professor"),
    MY_REVIEW("myReview"),
    PROF_FROM_SUBJ("profFromSubj"),
    SUBJ_FROM_PROF("subjFromProf");

    private String typeName;

    ReviewSearchType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
