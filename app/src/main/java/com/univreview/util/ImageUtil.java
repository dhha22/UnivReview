package com.univreview.util;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by DavidHa on 2017. 1. 31..
 */
public class ImageUtil {
    public static final Uri TEMP_IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp.jpg"));

    public static String getPath(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ((Activity) context).managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }
}
