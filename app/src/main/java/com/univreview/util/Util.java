package com.univreview.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.univreview.App;
import com.univreview.log.Logger;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jcajce.provider.digest.SHA3;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Created by DavidHa on 2017. 1. 4..
 */
public class Util {

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


}
