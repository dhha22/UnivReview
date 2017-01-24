package com.univreview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.univreview.activity.MainActivity;
import com.univreview.activity.NavigationActivity;
import com.univreview.fragment.login.RegisterUnivInfoFragment;
import com.univreview.fragment.login.RegisterUserIdentityFragment;
import com.univreview.fragment.login.RegisterUserInfoFragment;
import com.univreview.fragment.search.SearchFragment;
import com.univreview.fragment.upload.UploadReviewFragment;
import com.univreview.model.Register;

/**
 * Created by DavidHa on 2017. 1. 11..
 */
public class Navigator {
    public static final int SEARCH = 636;

    //register
    public static void goRegisterUserInfo(Context context, Register register){
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(RegisterUserInfoFragment.newInstance(register));
        context.startActivity(intent);
    }

    public static void goRegisterUnivInfo(Context context, Register register){
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(RegisterUnivInfoFragment.newInstance(register));
        context.startActivity(intent);
    }

    public static void goRegisterUserIdentity(Context context){
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(RegisterUserIdentityFragment.newInstance());
        context.startActivity(intent);
    }



    public static void goReview(Context context){
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(UploadReviewFragment.newInstance());
        context.startActivity(intent);
    }

    public static void goMain(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void goSearch(Context context, String type, int id){
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(SearchFragment.newInstance(type, id));
        context.startActivity(intent);
    }

    public static void goSearch(Activity activity, String type, int id, String name){
        Intent intent = new Intent(activity, NavigationActivity.class);
        NavigationActivity.setFragment(SearchFragment.newInstance(type, id));
        activity.startActivityForResult(intent, SEARCH);
    }
}
