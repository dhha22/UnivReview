package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 2. 24..
 */
public class UserTicket implements Serializable {
    @Expose
    public long id;
    @Expose
    public String ticketType;
    @Expose
    public String name;
    @Expose
    public Term term = new Term();
    @Expose
    public long userId;
    @Expose
    public long ticketId = 1;

    @Override
    public String toString() {
        return "UserTicket{" +
                "id=" + id +
                ", ticketType='" + ticketType + '\'' +
                ", name='" + name + '\'' +
                ", term=" + term +
                ", userId=" + userId +
                ", ticketId=" + ticketId +
                '}';
    }
}
