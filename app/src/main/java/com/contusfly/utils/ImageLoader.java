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
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.polls.polls.R;

import java.io.File;

/**
 * This class will contain all the image based operations
 */
public class ImageLoader {

    private ImageLoader() {
    }

    /**
     * Load image in view.
     *
     * @param imageView the image view
     * @param path      the path
     * @param base64    the base64
     */
    public static void loadImageInView(ImageView imageView, String path, String base64) {
        if (!TextUtils.isEmpty(base64)) {
            ImageLoader.loadBase64(imageView, base64);
        }
        if (!path.isEmpty()) {
            File file = new File(path);
            if (file.exists()) {
                ImageLoader.loadImageWithGlide(imageView.getContext(), file, imageView,
                        R.drawable.abc_user_placeholder);
            }
        }
    }

    /**
     * Load image in view.
     *
     * @param imageView the image view
     * @param path      the path
     * @param base64    the base64
     */
    public static void loadImageInView(ImageView imageView, String path, String base64, int placeHolder) {
        if (!TextUtils.isEmpty(base64)) {
            ImageLoader.loadBase64(imageView, base64);
        } else {
            imageView.setImageResource(placeHolder);
        }
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                ImageLoader.loadImageWithGlide(imageView.getContext(), file, imageView,
                        placeHolder);
            }
        }
    }

    /**
     * Load base64.
     *
     * @param imageView the image view
     * @param base64    the base64
     */

    public static void loadBase64(ImageView imageView, String base64) {
        byte[] array = Base64.decode(base64, Base64.DEFAULT);
        Bitmap mBitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
        if (mBitmap != null) {
            imageView.setImageBitmap(mBitmap);
        }
    }

    /**
     * Load image with glide.
     *
     * @param context  the context
     * @param imgUrl   the img url
     * @param imgView  the img view
     * @param errorImg the error img
     */
    public static void loadImageWithGlide(Context context, String imgUrl, ImageView imgView, int errorImg) {
        if (imgUrl != null && !imgUrl.isEmpty())
            Glide.with(context).load(imgUrl).error(errorImg).placeholder(errorImg).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgView);
        else
            imgView.setImageResource(errorImg);
    }


    /**
     * Load image with glide.
     *
     * @param context  the context
     * @param imgUrl   the img url
     * @param imgView  the img view
     * @param errorImg the error img
     */
    public static void loadBroadcastImageWithGlide(Context context, String imgUrl, ImageView imgView, int errorImg) {
        if (imgUrl != null && !imgUrl.isEmpty())
            Glide.with(context).load(imgUrl).error(errorImg).placeholder(errorImg).centerCrop().into(imgView);
        else
            imgView.setImageResource(errorImg);
    }

    /**
     * Load image with glide.
     *
     * @param context  the context
     * @param imgFile  the img file
     * @param imgView  the img view
     * @param errorImg the error img
     */
    public static void loadImageWithGlide(Context context, File imgFile, ImageView imgView, int errorImg) {
        Glide.with(context).load(imgFile).centerCrop().error(errorImg).placeholder(errorImg).into(imgView);
    }
}
