package com.univreview.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.univreview.R;
import com.univreview.log.Logger;
import com.univreview.model.BusProvider;
import com.univreview.view.Toolbar;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class BaseFragment extends Fragment {
    protected LinearLayout rootLayout;
    protected Toolbar toolbar;
    protected Context context;
    protected Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.v("on create view: " + this.getClass().getName());
        this.context = getContext();
        this.activity = getActivity();
        rootLayout = new LinearLayout(context);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        toolbar = new Toolbar(context);
        rootLayout.addView(toolbar);
        return rootLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.newInstance().register(this);
        Logger.v("on resume: " + this.getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.newInstance().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.v("on destroy: " + this.getClass().getName());
    }


}
