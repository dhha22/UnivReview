package com.univreview.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 2. 27..
 */
public class UserTicketModel implements Serializable {
    @Expose
    public List<UserTicket> userTicket = new ArrayList<>();

    @Override
    public String toString() {
        return "UserTicketModel{" +
                "userTicket=" + userTicket +
                '}';
    }
}
