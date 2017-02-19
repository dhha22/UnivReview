package com.univreview;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.kakao.auth.KakaoSDK;
import com.squareup.picasso.Picasso;
import com.univreview.adapter.KakaoSDKAdapter;
import com.univreview.log.Logger;
import com.univreview.util.SecurityUtil;
import com.univreview.util.SharedPreferencesActivity;
import com.univreview.util.TimeUtil;

import org.bouncycastle.util.encoders.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DavidHa on 2016. 12. 25..
 */
public class App extends Application {
    private static App instance;
    private static volatile Activity currentActivity = null;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static Context context;
    public static SharedPreferencesActivity pref;
    public static final Gson gson = new Gson();
    public static Picasso picasso;
    public static long UNIVERSITY_ID = 1;
    public static String userToken;

    @Override
    public void onCreate() {
        super.onCreate();
        App.context = this;
        instance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE))
                .build();

        pref = new SharedPreferencesActivity(this);
        Picasso.setSingletonInstance(picasso);

        SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = getResources().getDisplayMetrics().heightPixels;

        Logger.v("app_key_hash: " + SecurityUtil.getKeyHash(context));

        init();
    }

    public static Activity getCurrentActivity() {
        Logger.d("++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        App.currentActivity = currentActivity;
    }

    public static App getGlobalApplicationContext() {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    public static void setUserToken(String token){
        Logger.v("set user token: " + token);
        pref.savePreferences("userToken", token);
    }

    public static void init() {
        userToken = pref.getPreferences("userToken", null);
        Logger.v("user token: " + userToken);
    }

    public static Map<String, String> setAuthHeader(String token) {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", App.makeApiToken(token, App.makeApiSignature()));
        return map;
    }

    public static String makeApiSignature(){
        String saltKey = "e7b35739643f6f595e1a3231666138ca";
        String timeStamp = new TimeUtil().getTimeStamp();
        String appVersion = context.getResources().getString(R.string.app_version);
        String apiSignature = SecurityUtil.cryptoSHA3(appVersion + ';' + timeStamp + ';' + saltKey);
        Logger.v("signamture time stamp: " + timeStamp);
        Logger.v("api signature: " + apiSignature);
        return apiSignature;
    }

    public static String makeApiToken(String token, String apiSignature) {
        String timeStamp = new TimeUtil().getTimeStamp();
        String appVersion = context.getResources().getString(R.string.app_version);
        String apiToken;
        try {
            apiToken = URLEncoder.encode(Base64.toBase64String((token + ";" + appVersion + ";" +
                    timeStamp + ";" + apiSignature).getBytes()), "UTF-8");
            Logger.v("api userToken: " + apiToken);
            return apiToken;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}

