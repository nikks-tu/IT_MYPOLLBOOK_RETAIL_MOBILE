package com.contusfly.utils;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import com.polls.polls.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by user on 29/8/16.
 */
public class MediaPathLoader {

    public static Uri getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp);
            } catch (FileNotFoundException e) {
                LogMessage.e(e);
            } finally {
                try {
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                    LogMessage.e(e);
                }
            }
        }
        return null;
    }

    public static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String storeMedialInTempFile(Context context, Uri uri) {
        InputStream is = null;
        BufferedOutputStream outStream = null;
        FileOutputStream fileOutputStream = null;
        String filePath = null;
        if (uri.getAuthority() != null) {
            try {

                Cursor returnCursor =
                        context.getContentResolver().query(uri, null, null, null, null);
                if (returnCursor == null)
                    return uri.getPath();

                /*
                * Get the column indexes of the data in the Cursor,
                * move to the first row in the Cursor, get the data,
                * and display it.
                */
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                String name = returnCursor.getString(nameIndex);
                String extn = name.substring(name.lastIndexOf(".") + 1);

                Resources resources = context.getResources();
                Calendar todayDate = Calendar.getInstance();
                String dateToday = todayDate.get(Calendar.DAY_OF_MONTH) + "-"
                        + ((todayDate.get(Calendar.MONTH)) + 1) + "-"
                        + todayDate.get(Calendar.YEAR) + " "
                        + todayDate.get(Calendar.HOUR) + "-"
                        + todayDate.get(Calendar.MINUTE) + "-"
                        + todayDate.get(Calendar.SECOND);
                String appName = resources.getString(R.string.app_name);
                String fileNamePlaceHolder = String.format(Environment.getExternalStorageDirectory() + File.separator
                        + appName + File.separator + "video" + File.separator + "%1$s.%2$s", dateToday, extn);
                filePath = String.format(fileNamePlaceHolder, dateToday);
                ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver()
                        .openFileDescriptor(uri, "r");

                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

                is = new FileInputStream(fileDescriptor);

                BufferedInputStream reader = new BufferedInputStream(is);

                // Create an output stream to a file that you want to save to
                File file = new File(filePath);
                File parent = file.getParentFile();
                if (!parent.exists())
                    parent.mkdirs();

                if (!file.exists()) {
                    file.createNewFile();
                }
                fileOutputStream = new FileOutputStream(file);
                outStream = new BufferedOutputStream(fileOutputStream);
                byte[] buf = new byte[2048];
                int len;
                while ((len = reader.read(buf)) > 0) {
                    outStream.write(buf, 0, len);
                }
            } catch (FileNotFoundException e) {
                LogMessage.e(e);
            } catch (IOException e) {
                LogMessage.e(e);
            } finally {
                try {
                    if (is != null)
                        is.close();
                } catch (Exception e) {
                    LogMessage.e(e);
                }
                try {
                    if (fileOutputStream != null)
                        fileOutputStream.close();
                } catch (Exception e) {
                    LogMessage.e(e);
                }
                try {
                    if (outStream != null)
                        outStream.close();
                } catch (Exception e) {
                    LogMessage.e(e);
                }
            }
        }
        return filePath;
    }
}
