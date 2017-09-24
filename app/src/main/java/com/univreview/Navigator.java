package com.univreview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.univreview.activity.LoginActivity;
import com.univreview.activity.MainActivity;
import com.univreview.activity.NavigationActivity;
import com.univreview.activity.PermissionCheckerActivity;
import com.univreview.fragment.PointListFragment;
import com.univreview.fragment.SearchFragment;
import com.univreview.fragment.login.CheckUserPhotoFragment;
import com.univreview.fragment.login.RegisterUnivInfoFragment;
import com.univreview.fragment.login.RegisterUserIdentityFragment;
import com.univreview.fragment.login.RegisterUserInfoFragment;
import com.univreview.fragment.login.UserAuthCompletedFragment;
import com.univreview.fragment.review.ReviewDetailFragment;
import com.univreview.fragment.review.ReviewListFragment;
import com.univreview.fragment.setting.SettingFragment;
import com.univreview.fragment.upload.UploadReviewDetailFragment;
import com.univreview.fragment.upload.UploadReviewFragment;
import com.univreview.model.enumeration.ReviewSearchType;
import com.univreview.model.Review;
import com.univreview.model.User;
import com.univreview.util.ImageUtil;

import java.io.File;

/**
 * Created by DavidHa on 2017. 1. 11..
 */
public class Navigator {
    public static final int SEARCH = 636;
    public static final int CAMERA = 387;
    public static final int ALBUM = 549;
    public static final int PERMISSION_CHECKER = 405;

    public static void goLogin(Context context) {
        App.userLogout();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((Activity) context).onBackPressed();
    }

    //register
    public static void goRegisterUserInfo(Context context, User register) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(RegisterUserInfoFragment.newInstance(register));
        context.startActivity(intent);
    }

    public static void goRegisterUnivInfo(Context context, User register) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(RegisterUnivInfoFragment.getInstance(register));
        context.startActivity(intent);
    }

    public static void goRegisterUserIdentity(Context context) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(RegisterUserIdentityFragment.newInstance());
        context.startActivity(intent);
    }

    public static void goUserAuthCompleted(Context context) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(UserAuthCompletedFragment.newInstance());
        context.startActivity(intent);
    }

    public static void goCamera(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File filePath = new File(ImageUtil.IMAGE_PATH);
        if (!filePath.isDirectory()) {
            filePath.mkdirs();
        }
        File file = new File(ImageUtil.IMAGE_PATH + "tmp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, "com.univreview.provider", file));
        ((Activity) context).startActivityForResult(intent, CAMERA);
    }

    public static void goAlbum(Context context) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        ((Activity) context).startActivityForResult(intent, ALBUM);
    }

    public static void goPermissionChecker(Context context, String type) {
        Intent intent = new Intent(context, PermissionCheckerActivity.class);
        intent.putExtra("type", type);
        ((Activity) context).startActivityForResult(intent, PERMISSION_CHECKER);
    }

    //upload

    public static void goUploadReview(Context context) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(UploadReviewFragment.getInstance());
        context.startActivity(intent);
    }

    public static void goUploadReviewDetail(Context context, Review review) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(UploadReviewDetailFragment.getInstance(review, false));
        context.startActivity(intent);
    }

    public static void goUploadReviewDetail(Context context, Review review, boolean isFirst) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(UploadReviewDetailFragment.getInstance(review, isFirst));
        context.startActivity(intent);
    }


    //main

    public static void goMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    //review
    public static void goReviewList(Context context, ReviewSearchType type, long id, String name) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(ReviewListFragment.getInstance(type, id, name));
        (context).startActivity(intent);
    }

    public static void goReviewDetail(Context context, Review data) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(ReviewDetailFragment.getInstance(data));
        (context).startActivity(intent);
    }



    //search

    public static void goSearch(Context context, ReviewSearchType type) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(SearchFragment.getInstance(type, 0));
        ((Activity) context).startActivityForResult(intent, SEARCH);
    }


    public static void goSearch(Context context, ReviewSearchType type, Long id) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(SearchFragment.getInstance(type, id));
        ((Activity) context).startActivityForResult(intent, SEARCH);
    }


    //

    public static void goCheckUserPhoto(Context context, String type, String path) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(CheckUserPhotoFragment.newInstance(type, path));
        context.startActivity(intent);
    }

    //mypage
    public static void goPointList(Context context, int point) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(PointListFragment.getInstance(point));
        context.startActivity(intent);
    }

    public static void goSetting(Context context) {
        Intent intent = new Intent(context, NavigationActivity.class);
        NavigationActivity.setFragment(SettingFragment.getInstance());
        context.startActivity(intent);
    }

    public static void goAppSetting(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
        // activity.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
    }

    public static void goGooglePlayStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.univreview"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        App.getCurrentActivity().startActivity(intent);
        (App.getCurrentActivity()).finish();
    }
}
