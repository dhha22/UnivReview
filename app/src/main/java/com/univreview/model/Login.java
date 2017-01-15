package com.univreview.model;

/**
 * Created by DavidHa on 2017. 1. 14..
 */
public class Login {
    private String userType;
    private String userId;
    private String accessToken;

    public Login(String userType, String userId, String accessToken) {
        this.userType = userType;
        this.userId = userId;
        this.accessToken = accessToken;
    }
}
