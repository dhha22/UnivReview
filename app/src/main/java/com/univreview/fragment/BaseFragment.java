/*
package com.univreview.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.univreview.util.Util;
import com.univreview.view.Toolbar;

*/
/**
 * Created by DavidHa on 2017. 1. 13..
 *//*

public class BaseFragment extends Fragment {
    private static final String HOME_FRAGMENT = "com.univreview.fragment.HomeFragment";
    protected LinearLayout rootLayout;
    protected Toolbar toolbar;
    protected Context context;
    protected Activity activity;
    protected ProgressDialog progressDialog;

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
        progressDialog = Util.progressDialog(context);
        rootLayout = new LinearLayout(context);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        toolbar = new Toolbar(context);
        toolbar.setBackgroundColor(Util.getColor(context, R.color.colorPrimary));
        rootLayout.addView(toolbar);
        return rootLayout;
    }

    protected void setToolbarTransparent(){
        if(toolbar != null){
            toolbar.setBackgroundColor(Util.getColor(context, R.color.transparent));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!this.getClass().getName().equals(HOME_FRAGMENT)) {

            BusProvider.newInstance().register(this);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if(!this.getClass().getName().equals(HOME_FRAGMENT)) {
            Logger.v("on pause: " + this.getClass().getName());
            BusProvider.newInstance().unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(progressDialog != null){
            progressDialog.dismiss();
        }
        Logger.v("on destroy: " + this.getClass().getName());
    }

    public void showProgress(){
        if(progressDialog != null){
            progressDialog.show();
        }
    }

    public void dismissProgress(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

}
*/
