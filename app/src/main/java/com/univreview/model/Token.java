package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 1. 14..
 */
public class Token implements Serializable {
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "userToken='" + token + '\'' +
                '}';
    }
}
