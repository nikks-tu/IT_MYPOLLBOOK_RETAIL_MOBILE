/**
 * @category ContusMessanger
 * @package com.contusfly.activities
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */

package com.contusfly.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.contusfly.MApplication;
import com.contusfly.utils.Constants;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.Utils;
import com.contusfly.views.CustomToast;
import com.polls.polls.R;

import java.io.File;


/**
 * ImagePreviewActivity class to show the preview of the selected image from gallery
 */
public class ImagePreviewActivity extends AppCompatActivity {

    /**
     * show the selected image
     */
    private ImageView selectedImage;

    /**
     * mediapath to get the filepath from intent
     */
    private String mediaPath;


    /**
     * The local file of the image to display in the image view
     */
    private File mFileTemp;

    private String messageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_image);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectedImage = (ImageView) findViewById(R.id.selected_image);
        VideoView selectedVideo = (VideoView) findViewById(R.id.selected_video);

        ImageView sendImage = (ImageView) findViewById(R.id.image_send);
        ImageView playVideo = (ImageView) findViewById(R.id.ic_play);

        Intent intent = getIntent();
        mediaPath = intent.getStringExtra(Constants.SELECTED_IMAGE);
        messageType = intent.getStringExtra(Constants.CHAT_TYPE);

        mFileTemp = new File(mediaPath);

        if (messageType.equalsIgnoreCase(Constants.MSG_TYPE_VIDEO)) {
            selectedImage.setVisibility(View.GONE);
            selectedVideo.setVisibility(View.VISIBLE);
            playVideo.setVisibility(View.VISIBLE);
            Utils.setUpToolBar(this, toolbar, getSupportActionBar(), getString(R.string.selected_video));
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mediaPath,
                    MediaStore.Images.Thumbnails.MINI_KIND);
            if (thumb != null) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
                selectedVideo.setBackgroundDrawable(bitmapDrawable);
            } else {
                CustomToast.showToast(this, getString(R.string.media_not_available));
                finish();
            }
        } else {
            selectedImage.setVisibility(View.VISIBLE);
            selectedVideo.setVisibility(View.GONE);
            playVideo.setVisibility(View.GONE);
            Utils.setUpToolBar(this, toolbar, getSupportActionBar(), getString(R.string.selected_image));
            showPreview(mFileTemp);
        }

        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendImageClick();
            }
        });

        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayVideoClick();
            }
        });
    }

    /**
     * On send image view click
     */
    private void onSendImageClick() {
        Intent intent = new Intent();
        intent.putExtra(Constants.SELECTED_IMAGE, mediaPath);
        intent.putExtra(Constants.CHAT_TYPE, messageType);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * On play video view click
     */
    private void onPlayVideoClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + mediaPath), "video/mp4");
        startActivity(intent);
    }

    /**
     * showing the preview of the selected image from the gallery
     * using BitmapFactory decode the string path to bitmap
     *
     * @param mediaPath path of the media
     */
    private void showPreview(File mediaPath) {

        Bitmap scaledBitmap = BitmapFactory.decodeFile(String.valueOf(mediaPath));
        if (scaledBitmap != null) {
            int bitmapHeight = (int) (scaledBitmap.getHeight() * (512.0 / scaledBitmap.getWidth()));
            scaledBitmap = Bitmap.createScaledBitmap(scaledBitmap, 512, bitmapHeight, true);
            scaledBitmap = ((MApplication) getApplication()).rotateImage(scaledBitmap, mediaPath.getPath());
            selectedImage.setImageBitmap(scaledBitmap);
        } else {
            CustomToast.showToast(this, getString(R.string.media_not_available));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CROP_IMAGE && resultCode == Activity.RESULT_OK) {
            showPreview(mFileTemp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == R.id.action_add_user) {
                cropImage();
            }
        } catch (Exception e) {
            LogMessage  .e(e);
        }
        return super.onOptionsItemSelected(item);
    }

    private void cropImage() {
        Utils.cropImage(this, mFileTemp);
    }
}