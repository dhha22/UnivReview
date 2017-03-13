package com.univreview.model;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 3. 13..
 */
public class Ticket implements Serializable {
    public String name;
    public Term term = new Term();
}
