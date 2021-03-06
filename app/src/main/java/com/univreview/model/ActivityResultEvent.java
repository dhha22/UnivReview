package com.univreview.model;

import android.content.Intent;

import com.univreview.log.Logger;

/**
 * Created by DavidHa on 2017. 1. 22..
 */
public class ActivityResultEvent {

    private int requestCode;
    private int resultCode;
    private Intent intent;

    public ActivityResultEvent(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.intent = data;
        Logger.v("ActivityResultEvent");
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent data) {
        this.intent = data;
    }
}



