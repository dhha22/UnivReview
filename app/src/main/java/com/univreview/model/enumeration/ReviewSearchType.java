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
    MY_REVIEW("myReview");


    private String typeName;

    ReviewSearchType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
