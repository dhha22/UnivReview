package com.univreview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.squareup.otto.Produce;
import com.univreview.R;
import com.univreview.fragment.login.RegisterUnivInfoFragment;
import com.univreview.fragment.login.RegisterUserIdentityFragment;
import com.univreview.fragment.login.RegisterUserInfoFragment;
import com.univreview.fragment.search.SearchFragment;
import com.univreview.log.Logger;
import com.univreview.model.ActivityResultEvent;
import com.univreview.model.BusProvider;

/**
 * Created by DavidHa on 2017. 1. 9..
 */
public class NavigationActivity extends BaseActivity {
    protected static Fragment fragment;
    private int requestCode;
    private int resultCode;
    private Intent data;

    public static void setFragment(Fragment fragment) {
        NavigationActivity.fragment = fragment;
    }

    public static Fragment getFragment() {
        return fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.v("navigation activity onCreate");
        if (fragment != null) {
            if (fragment instanceof SearchFragment) {
                Logger.v("set translucent");
                setTranslucent();
            } else if (fragment instanceof RegisterUserInfoFragment
                    || fragment instanceof RegisterUnivInfoFragment
                    || fragment instanceof RegisterUserIdentityFragment) {
                Logger.v("set full screen");
                setFullScreen();
            }
            setContentView(R.layout.activity_navigation);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commitNow();
            fragment = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.newInstance().register(this);
    }

    @Override protected void onPause() {
        super.onPause();
        BusProvider.newInstance().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.requestCode =requestCode;
        this.resultCode = resultCode;
        this.data =data;
        Logger.v("navigation activity on activity result");
        BusProvider.newInstance().post(produceActivityResultEvent());
    }


    @Produce public ActivityResultEvent produceActivityResultEvent() {
        return new ActivityResultEvent(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        Logger.v("finish Navigation Activity");

    }


}

