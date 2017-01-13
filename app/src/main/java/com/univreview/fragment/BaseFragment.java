package com.univreview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.log.Logger;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.v("on create view: " + this.getClass().getName());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.v("on resume: " + this.getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.v("on destroy: " + this.getClass().getName());
    }
}
