package com.univreview.model;

import com.google.gson.annotations.Expose;

/**
 * Created by DavidHa on 2017. 2. 27..
 */
public class FileUploadModel {
    @Expose
    public String location;
    @Expose
    public String fileLocation;

    @Override
    public String toString() {
        return "FileUploadModel{" +
                "fileLocation='" + fileLocation + '\'' +
                '}';
    }
}
