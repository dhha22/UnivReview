package com.univreview.listener;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by DavidHa on 2017. 4. 11..
 */
public class BaseBackPressedListener implements OnBackPressedListener {
    private final Activity activity;

    public BaseBackPressedListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void doBack() {
        ((AppCompatActivity)activity).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
