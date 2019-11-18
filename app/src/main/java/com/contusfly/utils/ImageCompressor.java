/*
 * @category ContusMessanger
 * @package com.time.activities
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2016 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */

package com.contusfly.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.polls.polls.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


/**
 * This class contains image compression operations
 */
public class ImageCompressor {

    private ImageCompressor() {
    }

    public static String getCompressImageFilePath(Context context) {
        String path = Environment.getExternalStorageDirectory() + "/"
                + context.getString(R.string.app_name) + "/" + MediaPaths.MEDIA_PATH_IMAGE;
        Calendar todayDate = Calendar.getInstance();
        String dateToday = todayDate.get(Calendar.DAY_OF_MONTH) + "-" + ((todayDate.get(Calendar.MONTH)) + 1) + "-"
                + todayDate.get(Calendar.YEAR) + " " + todayDate.get(Calendar.HOUR) + "-"
                + todayDate.get(Calendar.MINUTE) + "-" + todayDate.get(Calendar.SECOND);

        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File photo = new File(folder, dateToday + ".jpg");
        return photo.getAbsolutePath();
    }

    /**
     * Compresses the image file and return new bitmap
     *
     * @param filePath image file path
     * @return compressed bitmap image
     */
    public static Bitmap compressImage(String filePath, String outFilePath, boolean saveFile) {
        return compressImageWithSize(filePath, 816.0f, 612.0f, outFilePath, saveFile);
    }

    /**
     * Compresses the image in required size
     *
     * @param filePath  file path of the image
     * @param maxHeight max height of output image
     * @param maxWidth  max width of output image
     * @param saveFile  indicates need to save the result bitmap in the same file path or not
     * @return compressed bitmap image
     */
    public static Bitmap compressImageWithSize(String filePath, float maxHeight, float maxWidth,
                                               String outFilePath, boolean saveFile) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * by setting this field as true, the actual bitmap pixels are not going to
         * load in to the memory. Just the bounds are going to load.
         */
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        /**
         * max Height and width values of the compressed image is taken as 816x612
         */

        float imgRatio = actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        /**
         * width and height values are set maintaining the aspect ratio of the image
         */

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio > maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio < maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        /**
         * setting inSampleSize value allows to load a scaled down version of the original image
         */

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        /**
         * inJustDecodeBounds set to false to load the actual bitmap
         */
        options.inJustDecodeBounds = false;
        options.inTargetDensity= DisplayMetrics.DENSITY_MEDIUM;

        /**
         * this options allow android to claim the bitmap memory if it runs low on memory
         */
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
            /**
             * load the bitmap from its path
             */
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            LogMessage.e(exception);
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            LogMessage.e(exception);
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

        scaledBitmap = getRotatedBitmap(scaledBitmap, filePath);
        if (saveFile)
            saveBitmap(scaledBitmap, outFilePath);
        return scaledBitmap;
    }

    /**
     * Saves the
     *
     * @param bmp
     */
    private static void saveBitmap(Bitmap bmp, String outFilePath) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outFilePath);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            LogMessage.e(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LogMessage.e(e);
            }
        }
    }

    /**
     * Calculates sample size of image
     *
     * @param options   bitmap factory options
     * @param reqWidth  max width
     * @param reqHeight max height
     * @return sample size of image
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = (float) width * height;
        final float totalReqPixelsCap = (float) reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    /**
     * Returns the rotated bitmap based rotation settings in the image
     *
     * @param sourceBitmap bitmap image to rotate
     * @param picturePath  file path of the image
     * @return rotated bitmap
     */
    private static Bitmap getRotatedBitmap(Bitmap sourceBitmap, String picturePath) {

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(picturePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            return Bitmap.createBitmap(sourceBitmap, 0, 0,
                    sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            LogMessage.e(e);
        }
        return sourceBitmap;
    }
}
