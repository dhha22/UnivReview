package com.univreview.model;

import com.google.gson.annotations.Expose;

/**
 * Created by DavidHa on 2017. 1. 3..
 */
public class ResponseModel<T> {
    @Expose
    public T data;
}
