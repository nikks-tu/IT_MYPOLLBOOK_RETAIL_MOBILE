/**
 * @category ContusMessanger
 * @package com.contusfly
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;

import com.contusfly.utils.Constants;
import com.contusfly.utils.Utils;

/**
 * The Class NetworkStateReceiver.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    /** The Constant PENDING_REQ_DELAY. */
    private static final int PENDING_REQ_DELAY = 1000;

    /** The context. */
    private Context context;

    /*
     * (non-Javadoc)
     * 
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
     * android.content.Intent)
     */
    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context.getApplicationContext();
        MApplication mApplication = (MApplication) context
                .getApplicationContext();
        if (intent != null
                && intent.getAction().equalsIgnoreCase(
                        ConnectivityManager.CONNECTIVITY_ACTION)
                && mApplication.isNetConnected(context)) {
            if (mApplication.getBooleanFromPreference(Constants.IS_LOGGED_IN)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utils.sendPendingMessages(NetworkStateReceiver.this.context);
                    }
                }, PENDING_REQ_DELAY);
            }
        }
    }
}
