package com.univreview;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.kakao.auth.KakaoSDK;
import com.squareup.picasso.Picasso;
import com.univreview.adapter.KakaoSDKAdapter;
import com.univreview.log.Logger;
import com.univreview.util.SecurityUtil;
import com.univreview.util.SharedPreferencesActivity;
import com.univreview.util.TimeUtil;
import com.univreview.util.Util;

import org.bouncycastle.util.encoders.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2016. 12. 25..
 */
public class App extends Application {
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
    public static int universityId;
    public static Long userId;
    public static String userToken;
    public static String registrationId;

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
        App.userToken = token;
        pref.savePreferences("userToken", token);
    }

    public static void setUserId(long userId){
        Logger.v("set user id: " + userId);
        App.userId = userId;
        pref.savePreferences("userId", userId);
    }

    public static void setUniversityId(long universityId) {
        Logger.v("set universityId id: " + universityId);
        App.universityId = (int) universityId;
        pref.savePreferences("universityId", App.universityId);
    }

    public static void init() {
        userToken = pref.getPreferences("userToken", null);
        userId = pref.getPreferences("userId", 0l);
        universityId = pref.getPreferences("universityId", 0);
        Logger.v("user token: " + userToken);
        Logger.v("user id: " + userId);
        Logger.v("university id: " + universityId);
    }

    private static void setFCMToken(String registrationId){
        pref.savePreferences("registration_id",registrationId);
        App.registrationId = pref.getPreferences("registration_id", "");
    }

    public static void initializeFCMToken() {
        setFCMToken(FirebaseInstanceId.getInstance().getToken());
        Logger.v("registration_id: " + registrationId);
        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", registrationId);
        clipboardManager.setPrimaryClip(clipData);
    }

    public static Map<String, String> setAuthHeader(String token) {
        Map<String, String> map = new HashMap<>();
        String timeStamp = new TimeUtil().getTimeStamp();
        map.put("Authorization", App.makeApiToken(token, timeStamp, App.makeApiSignature(token, timeStamp)));
        return map;
    }

    public static String makeApiSignature(String token, String timeStamp) {
        String saltKey = "e7b35739643f6f595e1a3231666138ca";
        String appVersion = context.getResources().getString(R.string.app_version);
        String apiSignature = SecurityUtil.cryptoSHA3(appVersion + ';' + timeStamp + ';' + saltKey + token);
        Logger.v("signature time stamp: " + timeStamp);
        Logger.v("api signature: " + apiSignature);
        return apiSignature;
    }

    public static String makeApiToken(String token, String timeStamp, String apiSignature) {
        String appVersion = context.getResources().getString(R.string.app_version);
        String apiToken;
        try {
            Logger.v("time stamp: " + timeStamp);
            Logger.v("token: " + token);
            String base64Str = (Base64.toBase64String((token + ";" + appVersion + ";" + timeStamp + ";" + apiSignature).getBytes()));
            Logger.v("base64: " + base64Str);
            apiToken = URLEncoder.encode(base64Str, "UTF-8");
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

