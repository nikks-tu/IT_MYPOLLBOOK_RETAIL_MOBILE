/**
 * @category ContusMessanger
 * @package com.contusfly.activities
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.contusfly.MApplication;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.utils.Constants;
import com.contusfly.utils.Utils;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.polls.polls.R;

import java.util.List;

/**
 * The Class ActivityUserProfile.
 */
public class ActivityUserProfile extends AppCompatActivity implements
        ObservableScrollViewCallbacks {

    /**
     * The m overlay view.
     */
    private View mOverlayView;

    /**
     * The m scroll view.
     */
    private ObservableScrollView mScrollView;

    /**
     * The m action bar size.
     */
    private int mActionBarSize;

    /**
     * The m flexible space image height.
     */
    private int mFlexibleSpaceImageHeight;

    /**
     * The toolbar.
     */
    private Toolbar toolbar;

    /**
     * The image pic.
     */
    private ImageView imagePic;

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlechat_info);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(
                R.color.color_transparent_bg));
        setSupportActionBar(toolbar);
        Utils.setUpToolBar(this, toolbar, getSupportActionBar(),
                Constants.EMPTY_STRING);
        imagePic = (ImageView) findViewById(R.id.image);
        List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(getIntent().getStringExtra(Constants.ROSTER_USER_ID),
                Constants.EMPTY_STRING);
        final Rosters item = rosterInfo.get(0);

        Utils.loadImageWithGlide(this, item.getRosterImage(), imagePic, R.drawable.abc_user_placeholder);
        toolbar.setTitle(MApplication.getDecodedString(item.getRosterName()));
        TextView txtStatus = (TextView) findViewById(R.id.txt_user_status);

        if (MApplication.returnEmptyStringIfNull(item.getRosterStatus()).isEmpty())
            txtStatus.setText(getResources().getString(R.string.default_status));
        else txtStatus.setText(MApplication.getDecodedString(item.getRosterStatus()));

        imagePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityUserProfile.this,
                        ActivityImageView.class).putExtra(Constants.MEDIA_URL,
                        item.getRosterImage()));
            }
        });

        MApplication.setBoolean(getApplicationContext(),"myprofile",true);

        toolbar.setBackgroundColor(getResources().getColor(
                android.R.color.transparent));
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(
                R.dimen.flexible_space_image_height);
        mActionBarSize = getSupportActionBar().getHeight();

        mOverlayView = findViewById(R.id.overlay);
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
    }

    /**
     * On post create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    /**
     * On scroll changed.
     *
     * @param scrollY     the scroll y
     * @param firstScroll the first scroll
     * @param dragging    the dragging
     */
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll,
                                boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView,
                ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(imagePic,
                ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView,
                ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        int imageHeight = (int) imagePic.getY();
        if (imageHeight < -100)
            toolbar.setBackgroundColor(getResources().getColor(
                    R.color.colorPrimary));
        else
            toolbar.setBackgroundColor(getResources().getColor(
                    android.R.color.transparent));
    }

    /**
     * On down motion event.
     */
    @Override
    public void onDownMotionEvent() {
    }

    /**
     * On up or cancel motion event.
     *
     * @param scrollState the scroll state
     */
    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

}
