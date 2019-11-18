/**
* @category   ContusMessanger
* @package    com.contusfly.utils
* @version 1.0
* @author ContusTeam <developers@contus.in>
* @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
* @license    http://www.apache.org/licenses/LICENSE-2.0
*/
package com.contusfly.utils;

import android.util.Log;

import java.util.Arrays;

/**
 * The Class LogMessage.
 */
/**
 * This class is used to print log message and toast message for development and
 * testing purpose *
 */

public class LogMessage {

    /** The Constant IS_DEBUG_ENABLED. */
    public static final boolean IS_DEBUG_ENABLED = true;

    private static final String tag = "TIME::";

    /**
     * Instantiates a new log message.
     */
    private LogMessage() {
        // Un Used Constructor. Added for SONAR Dependency
    }

    /**
     * I.
     *
     * @param tag the tag
     * @param message the message
     */
    public static void i(String tag, String message) {
        if (IS_DEBUG_ENABLED) {
            Log.i(tag, message);
        }
    }

    /**
     * V.
     *
     * @param tag the tag
     * @param message the message
     */
    public static void v(String tag, String message) {
        if (IS_DEBUG_ENABLED) {
            Log.v(tag, message);
        }
    }

    /**
     * E.
     *
     * @param tag the tag
     * @param message the message
     */
    public static void e(String tag, String message) {
        if (IS_DEBUG_ENABLED) {
            Log.e(tag, message);
        }
    }

    /**
     * D.
     *
     * @param tag the tag
     * @param message the message
     */
    public static void d(String tag, String message) {
        if (IS_DEBUG_ENABLED) {
            Log.d(tag, message);
        }
    }
    /**
     * E.
     *
     * @param tag
     *            the tag
     * @param error
     *            the error
     */
    public static void e(String tag, Throwable error) {
        if (IS_DEBUG_ENABLED) {
            System.out.print("Error:" + error);
        }
    }

    /**
     * E.
     *
     * @param error the error
     */
    public static void e(Throwable error) {
        if (IS_DEBUG_ENABLED) {
            error.printStackTrace();
        }
    }

    public static void e(OutOfMemoryError exception) {
        if (IS_DEBUG_ENABLED) {
            Log.e(tag, Arrays.toString(exception.getStackTrace()));
        }
    }

    public static void e(Exception e) {
        if (IS_DEBUG_ENABLED) {
            Log.e(tag, Arrays.toString(e.getStackTrace()));
        }
    }

}
