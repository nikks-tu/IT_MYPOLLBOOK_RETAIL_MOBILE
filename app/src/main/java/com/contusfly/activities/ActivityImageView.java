/**
 * @category   ContusMessanger
 * @package    com.contusfly.activities
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.contusfly.utils.Constants;
import com.contusfly.utils.Utils;
import com.polls.polls.R;

/**
 * The Class ActivityImageView.
 */
public class ActivityImageView extends AppCompatActivity {

    /**
     * On create.
     *
     * @param savedInstanceState
     *            the saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView view = new ImageView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        Log.e("test",getIntent().getStringExtra(Constants.MEDIA_URL)+"");
        view.setLayoutParams(params);
        Utils.loadImageWithGlide(this,"file://"+
                getIntent().getStringExtra(Constants.MEDIA_URL), view,
                R.drawable.ic_grp_bg);
        setContentView(view);
    }
}
