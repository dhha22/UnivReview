package com.univreview.model;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 1. 15..
 */
public class Register implements Serializable {
    public String userType;
    public String userId;
    public String accessToken;
    public String nickName;
    public String profileUrl;
    public Integer universityId;
    public Long departmentId;
    public Long majorId;

    public Register(String userType, String userId, String accessToken, String nickName, String profileUrl) {
        this.userType = userType;
        this.userId = userId;
        this.accessToken = accessToken;
        this.nickName = nickName;
        this.profileUrl = profileUrl;
    }


    @Override
    public String toString() {
        return "Register{" +
                "userType='" + userType + '\'' +
                ", userId='" + userId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", nickName='" + nickName + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ", universityId=" + universityId +
                ", departmentId=" + departmentId +
                ", majorId=" + majorId +
                '}';
    }
}
