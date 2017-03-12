package com.univreview.model;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 3. 11..
 */
public class Term implements Serializable {
    public String startDate;
    public String endDate;

    @Override
    public String toString() {
        return "Term{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
