package com.univreview.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by DavidHa on 2015. 2. 27..
 */
public class SharedPreferencesActivity {
    Context mContext;

    public SharedPreferencesActivity(Context context) {
        mContext = context;
    }

    public void savePreferences(String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public void savePreferences(String key, int value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    public void savePreferences(String key, long value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putLong(key, value);
        editor.commit();
    }

    public void savePreferences(String key, boolean value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public <T>void savePreferences(String key, List<T> value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    }

    public String getPreferences(String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        try {
            return pref.getString(key, value);
        } catch (Exception e) {
            return value;
        }
    }

    public int getPreferences(String key, int value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        try {
            return pref.getInt(key, value);
        } catch (Exception e) {
            return value;
        }
    }

    public long getPreferences(String key, long value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        try {
            return pref.getLong(key, value);
        } catch (Exception e) {
            return value;
        }
    }

    public boolean getPreferences(String key, boolean value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        try {
            return pref.getBoolean(key, value);
        } catch (Exception e) {
            return value;
        }
    }

    public <T>List<T> getPreferences(String key) {
        List<T> value;
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        try {
            Gson gson = new Gson();
            String json = pref.getString(key, "");
            Type type = new TypeToken<List<T>>() {}.getType();
            value = gson.fromJson(json, type);
            return value;
        } catch (Exception e) {
            return null;
        }
    }
}
