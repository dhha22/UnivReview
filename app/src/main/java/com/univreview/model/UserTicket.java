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
    public String startDate;
    @Expose
    public String expiryDate;
    @Expose
    public long userId;
    @Expose
    public long ticketId = 2;

    @Override
    public String toString() {
        return "UserTicket{" +
                "id=" + id +
                ", ticketType='" + ticketType + '\'' +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                ", ticketId=" + ticketId +
                '}';
    }
}
