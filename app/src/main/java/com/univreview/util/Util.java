package com.univreview.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.univreview.App;
import com.univreview.R;
import com.univreview.log.Logger;

/**
 * Created by DavidHa on 2017. 1. 4..
 */
public class Util {

    public static float dpToPx(Context context, float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static void toast(String message) {
        Toast.makeText(App.context, message, Toast.LENGTH_SHORT).show();
    }

    public static ProgressDialog progressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("잠시만 기다려주세요.");
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static void simpleMessageDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("확인", null)
                .setCancelable(false)
                .show();
    }

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static void setProfileImage(String url, ImageView imageView) {
        if (url == null || url.length() == 0) {
            App.picasso.load(R.drawable.login_profile_default)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        } else {
            App.picasso.load(url)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }
    }

    public static void hideKeyboard(Context context, EditText editText) {
        Logger.v("hide keyboard");
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void addColorSpan(Context context, SpannableStringBuilder builder, int index, int color) {
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, color)), index, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void addSizeSpan(SpannableStringBuilder builder, int index, int size) {
        builder.setSpan(new AbsoluteSizeSpan(size), index, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


}
