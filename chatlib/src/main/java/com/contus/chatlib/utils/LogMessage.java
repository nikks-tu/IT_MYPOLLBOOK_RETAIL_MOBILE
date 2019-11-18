/**
 * @category ContusMessanger
 * @package xmlns:app="http://schemas.android.com/apk/res-auto".utils
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contus.chatlib.utils;

import android.util.Log;


/**
 * The Class LogMessage.
 */

/**
 * This class is used to print log message and toast message for development and
 * testing purpose *
 */

public class LogMessage {

	/**
	 * The Constant IS_DEBUG_ENABLED.
	 */
	public static final boolean IS_DEBUG_ENABLED = true;

	/**
	 * Instantiates a new log message.
	 */
	private LogMessage() {
		// Un Used Constructor. Added for SONAR Dependency
	}

	/**
	 * I.
	 *
	 * @param tag
	 *            the tag
	 * @param message
	 *            the message
	 */
	public static void i(String tag, String message) {
		if (IS_DEBUG_ENABLED) {
			Log.i(tag, message);
		}
	}

	/**
	 * V.
	 *
	 * @param tag
	 *            the tag
	 * @param message
	 *            the message
	 */
	public static void v(String tag, String message) {
		if (IS_DEBUG_ENABLED) {
			Log.v(tag, message);
		}
	}

	/**
	 * E.
	 *
	 * @param tag
	 *            the tag
	 * @param message
	 *            the message
	 */
	public static void e(String tag, String message) {
		if (IS_DEBUG_ENABLED) {
			Log.e(tag, message);
		}
	}

	/**
	 * D.
	 *
	 * @param tag
	 *            the tag
	 * @param message
	 *            the message
	 */
	public static void d(String tag, String message) {
		if (IS_DEBUG_ENABLED) {
			Log.d(tag, message);
		}
	}

	/**
	 * E.
	 * @param error
	 *            the error
	 */
	public static void e(Throwable error) {
		if (IS_DEBUG_ENABLED) {
			Log.e("ERROR:", error.getMessage() == null ? "Eorror" : error.getMessage());
		}
	}
}
