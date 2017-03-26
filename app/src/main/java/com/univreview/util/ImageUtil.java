package com.univreview.util;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.univreview.App;
import com.univreview.log.Logger;

import java.io.File;

/**
 * Created by DavidHa on 2017. 1. 31..
 */
public class ImageUtil {
    public static final Uri TEMP_IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp.jpg"));
    private static final String[] projection = {MediaStore.Images.Media.DATA};

    public static String getPath(Uri uri) {
        String path = null;
        try {
            if (new File(uri.getPath()).exists()) {
                return uri.getPath();
            }
            Cursor cursor = App.context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);
                cursor.close();
            }
        } catch (Throwable e) {
            Logger.e(e);
        }
        return path;
    }
}
