package com.univreview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.univreview.R;
import com.univreview.log.Logger;

/**
 * Created by DavidHa on 2017. 1. 9..
 */
public class NavigationActivity extends BaseActivity {
    protected static Fragment fragment;

    public static void setFragment(Fragment fragment) {
        NavigationActivity.fragment = fragment;
    }

    public static Fragment getFragment() {
        return fragment;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Logger.v("navigation activity onCreate");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commitNow();
            fragment = null;
        }
    }

    @Override
    public void finish() {
        super.finish();
        Logger.v("finish Navigation Activity");

    }
}

