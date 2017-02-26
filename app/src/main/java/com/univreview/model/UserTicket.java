package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 2. 24..
 */
public class UserTicket implements Serializable {
    @Expose
    public long userId;
    @Expose
    public long ticketId = 2;
}
