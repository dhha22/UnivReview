package com.univreview.util;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jcajce.provider.digest.SHA3;

import java.io.UnsupportedEncodingException;

/**
 * Created by DavidHa on 2017. 1. 4..
 */
public class Util {
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
}
