package com.univreview.model;

import com.squareup.otto.Bus;

/**
 * Created by DavidHa on 2017. 1. 22..
 */
public class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus newInstance() {
        return BUS;
    }

    private BusProvider() {
    }

}
