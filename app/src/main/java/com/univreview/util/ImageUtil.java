package com.univreview.util;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.univreview.App;
import com.univreview.log.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by DavidHa on 2017. 1. 31..
 */
public class ImageUtil {
    private static final float MAX_HEIGHT = 1280.0f;
    private static final float MAX_WIDTH = 1280.0f;
    public static final Uri TEMP_IMAGE_URI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp.jpg"));
    private static final String EXTRA_STORAGE = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String FOLDER_NAME = "/" + "UnivReview" + "/";
    public static final String IMAGE_PATH = EXTRA_STORAGE + FOLDER_NAME;
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

    public static void compressImage(String imagePath) {
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = MAX_WIDTH / MAX_HEIGHT;

        if (actualHeight > MAX_HEIGHT || actualWidth > MAX_WIDTH) {
            if (imgRatio < maxRatio) {
                imgRatio = MAX_HEIGHT / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) MAX_HEIGHT;
            } else if (imgRatio > maxRatio) {
                imgRatio = MAX_WIDTH / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) MAX_WIDTH;
            } else {
                actualHeight = (int) MAX_HEIGHT;
                actualWidth = (int) MAX_WIDTH;
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            File filePath = new File(IMAGE_PATH);
            if (!filePath.isDirectory()) {
                filePath.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(IMAGE_PATH + "tmp.jpg");
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
            out.close();
        } catch (Exception e) {
            Logger.e(e);
        }
        Logger.v("save image");
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
