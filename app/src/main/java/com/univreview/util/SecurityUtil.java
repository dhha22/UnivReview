package com.univreview.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.univreview.log.Logger;

import org.bouncycastle.jcajce.provider.digest.SHA3;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Created by DavidHa on 2017. 1. 9..
 */
public class SecurityUtil {
    public static String cryptoSHA3(String key) {
        SHA3.DigestSHA3 md = new SHA3.Digest256();
        try {
            md.update(key.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        byte[] digest = md.digest();

        return org.bouncycastle.util.encoders.Base64.toBase64String(digest);
    }

    public static String getKeyHash(final Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return null;
    }

}
