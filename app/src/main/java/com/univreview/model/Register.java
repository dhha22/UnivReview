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
    public int universityId;
    public int departmentId;
    public int majorId;

    public Register(String userType, String userId, String accessToken, String nickName, String profileUrl) {
        this.userType = userType;
        this.userId = userId;
        this.accessToken = accessToken;
        this.nickName = nickName;
        this.profileUrl = profileUrl;
    }

}
