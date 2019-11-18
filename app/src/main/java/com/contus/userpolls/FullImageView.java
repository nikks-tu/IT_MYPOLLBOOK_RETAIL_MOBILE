package com.contus.userpolls;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.contus.app.Constants;
import com.contusfly.MApplication;
import com.contusfly.frescoimagezoomable.ZoomableDraweeView;
import com.contusfly.views.RoundedCornersTransformation;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.google.android.gms.ads.AdView;
import com.polls.polls.R;

/**
 * Created by user on 7/13/2015.
 */
public class FullImageView extends Activity {
    //Image view
    ZoomableDraweeView imgFullView;
    //Textview
    TextView txtName;
    //Textview
    TextView txtTime;
    //Text category
    TextView txtCategory;
    //Simple draw view
    ImageView imgProfile;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_view);
        imgFullView =  findViewById(R.id.imgFullView);
        txtName =  findViewById(R.id.txtName);
        txtTime =  findViewById(R.id.txtTime);
        txtCategory =  findViewById(R.id.txtCategory);
        imgProfile =  findViewById(R.id.imgProfile);
        mAdView =  findViewById(R.id.adView);
        //Getting the value in intent
        String question1 = getIntent().getExtras().getString(Constants.QUESTION1);
        boolean isAdmin = getIntent().getExtras().getBoolean("is_admin");
        //Getting the value from preference
        String campaignName = MApplication.getString(FullImageView.this, Constants.CAMPAIGN_NAME);
        //Getting the value from preference
        String campaignCategory = MApplication.getString(FullImageView.this, Constants.CAMPAIGN_CATEGORY);
        //Getting the value from preference
        String campaignLogo = MApplication.getString(FullImageView.this, Constants.CAMPAIGN_LOGO);
        //Getting the value from preference
        String updatedTime = MApplication.getString(FullImageView.this, Constants.DATE_UPDATED);
        //Setting the value in text view
        txtCategory.setText(campaignCategory);
        //setting in text view
        txtName.setText(MApplication.getDecodedString(campaignName));
        //Setting in text view
        txtTime.setText(updatedTime);

        Log.i("ImageDetail",campaignLogo);
        //Setting the image uri
        //Utils.loadImageWithGlideProfileRounderCorner(FullImageView.this,"",imgProfile,R.drawable.placeholder_image);

        if(isAdmin){
            Glide.with(this).load(R.drawable.ic_logo_icon).error(R.drawable.placeholder_image).fitCenter().bitmapTransform(new RoundedCornersTransformation(this, 5, 5, 0,
                    RoundedCornersTransformation.CornerType.ALL))
                 .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                 .dontAnimate()
                 .placeholder(R.drawable.placeholder_image).into(imgProfile);
        }else {
            Glide.with(this).load(campaignLogo).error(R.drawable.placeholder_image).fitCenter().bitmapTransform(new RoundedCornersTransformation(this, 5, 5, 0,
                    RoundedCornersTransformation.CornerType.ALL))
                 .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                 .dontAnimate()
                 .placeholder(R.drawable.placeholder_image).into(imgProfile);
        }

        //The DraweeController is the class responsible for actually dealing with the un
        // derlying image loader - whether Fresco's own image pipeline, or another.
        DraweeController ctrl = Fresco.newDraweeControllerBuilder().setUri(Uri.parse(question1)).setTapToRetryEnabled(true).build();
        //This class does not do deep copies of most of the input parameters. There should be one
        // instance of the hierarchy per DraweeView, so that each hierarchy has a unique set of drawables.
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        //set controller
        imgFullView.setController(ctrl);
        //set hierarchy
        imgFullView.setHierarchy(hierarchy);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Setting the status bar color
            MApplication.settingStatusBarColor(FullImageView.this, getResources().getColor(android.R.color.black));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MApplication.googleAd(mAdView);
    }

    /**
     * Calling this method from xml file when performing the click on the ACTION
     *
     * @param clickedView
     */
    public void onClick(final View clickedView) {
        if (clickedView.getId() == R.id.imgBackArrow) {
            //finishing the activity
            this.finish();
        }
    }
}
