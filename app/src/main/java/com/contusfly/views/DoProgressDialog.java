/**
 * @category   ContusMessanger
 * @package    com.contusfly.views
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.polls.polls.R;


/**
 * The Class DoProgressDialog.
 */
public class DoProgressDialog extends Dialog {

    /** The m message. */
    private TextView mMessage;

    /** The context. */
    private Context context;

    /**
     * Instantiates a new do progress dialog.
     *
     * @param context
     *            the context
     */
    public DoProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Show progress.
     *
     * @param msg
     *            the msg
     */
    public void showProgress(String msg) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.progress_dialog, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCancelable(false);
        this.setContentView(view);
        mMessage = (TextView) view.findViewById(R.id.text);
        mMessage.setText(msg);
        this.show();
    }

    /**
     * Show progress.
     */
    public void showProgress() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.progress_dialog, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCancelable(false);
        this.setContentView(view);
        this.show();
    }

}
