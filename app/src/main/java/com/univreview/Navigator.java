package com.univreview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

import com.univreview.activity.MainActivity;
import com.univreview.activity.NavigationActivity;
import com.univreview.fragment.login.CheckUserPhotoFragment;
import com.univreview.fragment.login.RegisterUnivInfoFragment;
import com.univreview.fragment.login.RegisterUserIdentityFragment;
import com.univreview.fragment.login.RegisterUserInfoFragment;
import com.univreview.fragment.search.SearchFragment;
import com.univreview.fragment.upload.UploadReviewDetailFragment;
import com.univreview.fragment.upload.UploadReviewFragment;
import com.univreview.model.Register;
import com.univreview.model.Review;
import com.univreview.util.ImageUtil;

/**
 * Created by DavidHa on 2017. 1. 11..
 */
public class Navigator {
    public static final int SEARCH = 636;
    public static final int CAMERA = 387;
    public static final int ALBUM = 549;

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

    public static void goCamera(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtil.TEMP_IMAGE_URI);
        ((Activity) context).startActivityForResult(intent, CAMERA);
    }

    public static void goAlbum(Context context) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        ((Activity) context).startActivityForResult(intent, ALBUM);
    }

    public static void goUploadReview(Context context){
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(UploadReviewFragment.newInstance());
        context.startActivity(intent);
    }

    public static void goUploadReviewDetail(Context context, Review review) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(UploadReviewDetailFragment.newInstance(review));
        context.startActivity(intent);
    }

    public static void goMain(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void goSearch(Context context, String type, String name){
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(SearchFragment.newInstance(type, 0));
        ((Activity)context).startActivityForResult(intent, SEARCH);
    }


    public static void goSearch(Context context, String type, long id, String name){
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(SearchFragment.newInstance(type, id));
        ((Activity)context).startActivityForResult(intent, SEARCH);
    }

    public static void goCheckUserPhoto(Context context, String type, String path) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(CheckUserPhotoFragment.newInstance(type, path));
        context.startActivity(intent);
    }
}
