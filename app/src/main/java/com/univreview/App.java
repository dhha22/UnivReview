package com.univreview;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.univreview.log.Logger;

/**
 * Created by DavidHa on 2016. 12. 25..
 */
public class App extends Application {
    private static volatile Activity currentActivity = null;
    public static Context context;
    public static final Gson gson = new Gson();
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public static Activity getCurrentActivity() {
        Logger.d("++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        App.currentActivity = currentActivity;
    }
}
