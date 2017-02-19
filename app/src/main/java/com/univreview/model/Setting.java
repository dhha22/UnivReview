package com.univreview.model;

/**
 * Created by DavidHa on 2017. 2. 19..
 */
public class Setting extends AbstractDataProvider {
    public long id;
    public String title;

    public Setting(String title) {
        this.title = title;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return title;
    }
}
