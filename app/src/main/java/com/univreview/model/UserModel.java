package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 2. 21..
 */
public class UserModel implements Serializable {
    @Expose
    public User user = new User();

    @Expose
    public Token auth;


    @Override
    public String toString() {
        return "UserModel{" +
                "user=" + user +
                ", auth='" + auth + '\'' +
                '}';
    }
}
