/**
 * @category   ContusMessanger
 * @package    com.contusfly.activities
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.contusfly.MApplication;
import com.contusfly.utils.Constants;
import com.contusfly.utils.Utils;
import com.contusfly.views.CustomToast;
import com.polls.polls.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.contusfly.views.CustomEditText;
import com.contusfly.emoji.EmojiconsPopup;

/**
 * The Class ActivityCommonEditor.
 */
public class ActivityCommonEditor extends AppCompatActivity implements
        View.OnClickListener {

    /** The m application. */
    private MApplication mApplication;

    /** The edit text. */
    private CustomEditText editText;

    /** The img smiley. */
    private ImageView imgSmiley;

    /** The emojicons popup. */
    private EmojiconsPopup emojiconsPopup;

    /** The type. */
    private int type;

    /** The intent. */
    private Intent intent;

    /**
     * On create.
     *
     * @param savedInstanceState
     *            the saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_editor);
    }

    /**
     * On post create.
     *
     * @param savedInstanceState
     *            the saved instance state
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utils.setUpToolBar(this, toolbar, getSupportActionBar(),
                Constants.EMPTY_STRING);
        mApplication = (MApplication) getApplication();
        intent = getIntent();
        getSupportActionBar().setTitle(
                mApplication.returnEmptyStringIfNull(intent
                        .getStringExtra(Constants.TITLE)));
        findViewById(R.id.txt_cancel).setOnClickListener(this);
        findViewById(R.id.txt_ok).setOnClickListener(this);
        editText = (CustomEditText) findViewById(R.id.edt_editor);
        imgSmiley = (ImageView) findViewById(R.id.img_smiley);
        imgSmiley.setOnClickListener(this);
        emojiconsPopup = new EmojiconsPopup(findViewById(R.id.view_header),
                this);
        Utils.setUpKeyboard(emojiconsPopup, imgSmiley, editText);
        type = intent.getIntExtra(Constants.TITLE_TYPE, 0);
        Log.e("intent",intent.getStringExtra(Constants.MSG_TYPE_TEXT)+"");
        setMsg();
    }

    /**
     * Sets the msg.
     */
    private void setMsg() {
        try {
            String status = URLDecoder.decode(
                    intent.getStringExtra(Constants.MSG_TYPE_TEXT), "UTF-8")
                    .replaceAll("%", "%25");
            editText.setText(status);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.txt_cancel:
            setResult(Activity.RESULT_CANCELED);
            finish();
            break;
        case R.id.txt_ok:
            validateAndFinish();
            break;
        case R.id.img_smiley:
            popupKeyBoard();
            break;
        default:
            break;
        }
    }

    /**
     * Validate and finish.
     */
    private void validateAndFinish() {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            if (type == Constants.STATUS_UPDATE)
                CustomToast.showToast(this,
                        getString(R.string.text_status_not_empty));
            else if (type == Constants.GROUP_NAME_UPDATE)
                CustomToast.showToast(this,
                        getString(R.string.txt_enter_valid_name));
        } else {
            setResult(Activity.RESULT_OK,
                    new Intent().putExtra(Constants.TITLE, text));
            finish();
        }
    }

    /**
     * Popup key board.
     */
    private void popupKeyBoard() {
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText,
                InputMethodManager.SHOW_IMPLICIT);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!emojiconsPopup.isShowing()) {
                    emojiconsPopup.showAtBottomPending();
                } else {
                    emojiconsPopup.dismiss();
                }
            }
        }, 100);
    }

}
