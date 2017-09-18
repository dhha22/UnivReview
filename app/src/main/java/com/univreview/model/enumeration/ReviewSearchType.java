package com.univreview.model.enumeration;

/**
 * Created by DavidHa on 2017. 7. 14..
 */

public enum ReviewSearchType {
    UNIVERSITY("university"),                        // 학교 (로그인), 결과값 필요
    MAJOR("major"),                                 // 전공 (로그인), 결과값 필요
    SUBJECT("subject"),                             // 과목 (리뷰 리스트)
    SUBJECT_WITH_RESULT("subjectWithResult"),       // 과목 (리뷰 쓰기), 결과값 필요
    MY_REVIEW("myReview"),                          // 내 리뷰 (리뷰 리스트)
    PROF_FROM_SUBJ("profFromSubj");                 // 과목에 속한 교수 (교수 리스트), 결과값필요

    private String typeName;

    ReviewSearchType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
