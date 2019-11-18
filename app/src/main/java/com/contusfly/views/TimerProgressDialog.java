package com.contusfly.views;

import android.app.Activity;
import android.widget.Toast;

import com.polls.polls.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 8/9/16.
 */
public class TimerProgressDialog extends DoProgressDialog {

    private Timer mTimer;
    private Activity activity;

    /**
     * Instantiates a new do progress dialog.
     *
     * @param context the context
     */
    public TimerProgressDialog(Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    public void showProgress() {
        super.showProgress();
        TimerTask lTask = new TimerTask() {
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), R.string.text_error_occured, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
            }
        };
        this.mTimer = new Timer(activity.getClass().getName());
        this.mTimer.schedule(lTask, 5000);
    }

    @Override
    public void dismiss() {
        this.mTimer.cancel();
        super.dismiss();
    }
}
