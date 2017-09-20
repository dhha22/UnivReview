package com.univreview;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.kakao.auth.KakaoSDK;
import com.squareup.picasso.Picasso;
import com.univreview.adapter.KakaoSDKAdapter;
import com.univreview.log.Logger;
import com.univreview.util.SharedPreferencesActivity;
import com.univreview.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DavidHa on 2016. 12. 25..
 */
public class App extends MultiDexApplication {
    private static App instance;
    private static volatile Activity currentActivity = null;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static int dp1;
    public static int dp2;
    public static int dp3;
    public static int dp4;
    public static int dp5;
    public static int dp12;
    public static int dp15;
    public static int dp56;
    public static Context context;
    public static SharedPreferencesActivity pref;
    public static final Gson gson = new Gson();
    public static Picasso picasso;
    public static Long universityId;
    public static Long uid;
    public static Long userId;
    public static String userToken;
    public static String client;
    public static String registrationId;
    public static int point;

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
        dp1 = (int) Util.dpToPx(context, 1);
        dp2 = (int) Util.dpToPx(context, 2);
        dp3 = (int) Util.dpToPx(context, 3);
        dp4 = (int) Util.dpToPx(context, 4);
        dp5 = (int) Util.dpToPx(context, 5);
        dp12 = (int) Util.dpToPx(context, 12);
        dp15 = (int) Util.dpToPx(context, 15);
        dp56 = (int) Util.dpToPx(context, 56);
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
        App.userToken = token;
        pref.savePreferences("userToken", token);
    }

    public static void setUid(long uid) {
        Logger.v("set uid: " + uid);
        App.uid = uid;
        pref.savePreferences("uid", uid);
    }

    public static void setUserId(long userId){
        Logger.v("set user id: " + userId);
        App.userId = userId;
        pref.savePreferences("userId", userId);
    }

    public static void setPoint(int point) {
        Logger.v("set point: " + point);
        App.point = point;
        pref.savePreferences("point", point);
    }

    public static void setClient(String client){
        Logger.v("set client: " + client);
        App.client = client;
        pref.savePreferences("client", client);
    }

    public static void setUniversityId(long universityId) {
        Logger.v("set universityId id: " + universityId);
        App.universityId =  universityId;
        pref.savePreferences("universityId", App.universityId);
    }

    public static void init() {
        userToken = pref.getPreferences("userToken", null);
        userId = pref.getPreferences("userId", 0l);
        uid = pref.getPreferences("uid", 0l);
        universityId = pref.getPreferences("universityId", 0l);
        client = pref.getPreferences("client", null);
        Logger.v("user token: " + userToken);
        Logger.v("user id: " + userId);
        Logger.v("uid: " + uid);
        Logger.v("university id: " + universityId);
        Logger.v("client: " + client);
    }

    public static void userLogout() {
        setUserId(0);
        setUid(0);
        setFCMToken(null);
        setUserToken(null);
        setUniversityId(0);
        setClient(null);
    }


    public static void initializeFCMToken() {
        setFCMToken(FirebaseInstanceId.getInstance().getToken());
        Logger.v("registration_id: " + registrationId);
    }

    private static void setFCMToken(String registrationId) {
        pref.savePreferences("registration_id", registrationId);
        App.registrationId = pref.getPreferences("registration_id", "");

    }


    public static Map<String, String> setHeader(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", String.valueOf(App.uid));
        map.put("client", App.client);
        map.put("access-token", App.userToken);
        return map;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}

