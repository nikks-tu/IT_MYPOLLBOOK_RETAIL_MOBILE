/**
 * Logger.java
 * <p/>
 * Comment
 *
 * @category Contus
 * @package com.noorashidi.noorashidi.app
 * @version 1.0
 * @author Contus Team <developers@contus.in>
 * @copyright Copyright (C) 2014 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contus.views;

import android.os.Environment;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;


/**
 * A tool used to show toasts and track the application.
 *
 */
public class CustomToastLogger implements AnimationListener {

    /** The Constant TAG. */
    private static final String TAG = "Logger";

    /** The Constant DEBUG_MODE. */
    private static final boolean DEBUG_MODE = true;

    /** The Constant DEBUG_WRITE_MODE. */
    private static final boolean DEBUG_WRITE_MODE = true;

    /** The Constant APP_NAME. */
    private static final String APP_NAME = "MyPollBook";

    /** The Constant LOG_FILE_NAME. */
    private static final String LOG_FILE_NAME = "ThinQPoll.txt";


    /**
     * Used to track the debugs.
     *
     * @param msg
     *            - The String reference indicates message to be logged.
     */
    public static void logDebug(String msg) {
        if (DEBUG_MODE) {
            Log.d(TAG, msg);
        }
    }

    /**
     * Creates the instance of Andy.
     *
     * @param context
     *            - Environment context of current reference
     *
     */

    /**
     * Used to track the errors.
     *
     * @param msg
     *            - The String reference indicates message to be logged.
     */
    public static void logError(String msg) {
        if (DEBUG_MODE) {
            Log.e(TAG, msg);
        }
    }




    /**
     * Used to track the debugs.
     *
     * @param tag
     *            - The string reference indicates the tag.
     * @param msg
     *            - The string reference indicates message to be logged.
     */
    public static void logDebug(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.d(tag, msg);
            writeLogMessage(tag, msg, LOG_FILE_NAME);
        }
    }

    /**
     * Writes the log message into a file stored in disk.
     *
     * @param tag
     *            - The string reference indicates the TAG of the Log message.
     * @param message
     *            - The string reference indicates content of the message.
     * @param fileName
     *            - The string reference indicates log's file name.
     */
    private static void writeLogMessage(String tag, String message,
                                        String fileName) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                && DEBUG_WRITE_MODE) {
            try {
                StringBuilder logReport = new StringBuilder();
                logReport.append(tag + "\t" + message + "\n");
                logReport.append("\n");
                File root = new File(Environment
                        .getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + APP_NAME);
                // Create application directory if it does not exist.
                if (!root.exists()) {
                    root.mkdir();
                }
                String currentDateTimeString = DateFormat.getDateTimeInstance()
                        .format(new Date());
                File file = new File(root, fileName);
                BufferedWriter buf = new BufferedWriter(new FileWriter(file,
                        true));
                buf.append(currentDateTimeString + ":" + logReport.toString());
                buf.newLine();
                buf.close();
            } catch (IOException e) {
              Log.e("","",e);
            } catch (Exception e) {
                Log.e("","",e);
            }
        }

    }


    /**
     * * Animation Listener **.
     *
     * @param animation the animation
     */
    @Override
    public void onAnimationEnd(Animation animation) {
        Log.e("","");
    }

    /* (non-Javadoc)
     * @see android.view.animation.Animation.AnimationListener#onAnimationStart(android.view.animation.Animation)
     */
    @Override
    public void onAnimationStart(Animation animation) {
        Log.e("","");
    }


    /* (non-Javadoc)
     * @see android.view.animation.Animation.AnimationListener#onAnimationRepeat(android.view.animation.Animation)
     */
    @Override
    public void onAnimationRepeat(Animation animation) {
        Log.e("","");
    }


}