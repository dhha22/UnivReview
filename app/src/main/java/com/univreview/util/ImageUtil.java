package com.univreview.util;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.univreview.log.Logger;

import java.io.File;

/**
 * Created by DavidHa on 2017. 1. 31..
 */
public class ImageUtil {
    private Context context;
    public static final Uri TEMP_IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp.jpg"));

    public ImageUtil(Context context) {
        this.context = context;
    }

    public String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            Logger.v("cursor not null");
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
