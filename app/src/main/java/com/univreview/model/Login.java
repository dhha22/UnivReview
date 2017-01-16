package com.univreview.model;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 1. 14..
 */
public class Login implements Serializable {
    public String userType;
    public String userId;
    public String accessToken;

    public Login(String userType, String userId, String accessToken) {
        this.userType = userType;
        this.userId = userId;
        this.accessToken = accessToken;
    }


}
