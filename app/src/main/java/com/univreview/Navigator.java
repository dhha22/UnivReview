package com.univreview;

import android.content.Context;
import android.content.Intent;

import com.univreview.activity.MainActivity;
import com.univreview.activity.NavigationActivity;
import com.univreview.fragment.login.RegisterUnivInfoFragment;
import com.univreview.fragment.login.RegisterUserInfoFragment;

/**
 * Created by DavidHa on 2017. 1. 11..
 */
public class Navigator {
    public static void goRegisterUserInfo(Context context){
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(RegisterUserInfoFragment.newInstance());
        context.startActivity(intent);
    }

    public static void goRegisterUnivInfo(Context context){
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(RegisterUnivInfoFragment.newInstance());
        context.startActivity(intent);
    }

    public static void goMain(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
