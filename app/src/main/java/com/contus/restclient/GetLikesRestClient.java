/**
 * @category TeasorTrailor
 * @package com.contus.restclient
 * @version 1.0
 * @author Contus Team <developers@contus.in>
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contus.restclient;

import com.contus.apiinterface.GetLikesApiInterface;
import com.contus.app.Constants;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by user on 9/28/2015.
 */
public class GetLikesRestClient {
    private static GetLikesApiInterface sWelcomeApiInterface;/** The s settings api interface. */
    private static String root = Constants.LIVE_BASE_URL+"api/v1"; /** The root. */
    static {
        setupRestClient();
    }

    /**
     * Instantiates a new settings rest client.
     */
    private GetLikesRestClient() {
    }
    /**
     * Gets the.
     *
     * @return the settings api interface
     */
    public static GetLikesApiInterface getInstance() {
        return sWelcomeApiInterface;
    }
    /**
     * Setup rest client.
     */
    private static void setupRestClient() {
        //HTTP is the way modern applications network. It�s how we exchange data & media.
        // Doing HTTP efficiently makes your stuff load faster and saves bandwidth
        OkHttpClient okHttp = new OkHttpClient();
        //y to set the read timeout as well
        okHttp.setConnectTimeout(180, TimeUnit.SECONDS);
//HTTP is the way modern applications network. It�s how we exchange data & media.
        // Doing HTTP efficiently makes your stuff load faster and saves bandwidth.
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(root)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttp));
        //The REST adapter allows your store to communicate with an HTTP server by transmitting JSON via XHR.
        // Most Ember.js apps that consume a JSON API should use the REST adapter.
        RestAdapter restAdapter = builder.build();
        //create the rest adapter
        sWelcomeApiInterface = restAdapter.create(GetLikesApiInterface.class);
    }

}
