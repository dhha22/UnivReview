package com.univreview.model;

/**
 * Created by DavidHa on 2017. 2. 19..
 */
public class Setting extends com.univreview.model.model_kotlin.AbstractDataProvider {
    public long id;
    public String title;
    public String previewStr;

    public Setting(long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public void setName(String s) {
        this.title = s;
    }
}
