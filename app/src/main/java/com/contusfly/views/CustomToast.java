/**
 * @category   ContusMessanger
 * @package    com.contusfly.views
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.views;

import android.content.Context;
import android.widget.Toast;

/**
 * The Class CustomToast.
 */
public class CustomToast {

    /**
     * Show toast.
     *
     * @param context
     *            the context
     * @param msg
     *            the msg
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
