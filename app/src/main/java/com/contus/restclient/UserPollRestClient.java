package com.contus.restclient;

import com.contus.apiinterface.UserPollApiInterface;
import com.contus.app.Constants;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by user on 10/6/2015.
 */
public class UserPollRestClient {
    private static UserPollApiInterface sWelcomeApiInterface;/** The s settings api interface. */
    private static String root = Constants.LIVE_BASE_URL+"api/v1";/** The root. */
    static {
        setupRestClient();
    }
    /**
     * Instantiates a new settings rest client.
     */
    private UserPollRestClient() {

    }
    /**
     * Gets the.
     *
     * @return the settings api interface
     */
    public static UserPollApiInterface getInstance() {
        return sWelcomeApiInterface;
    }
    /**
     * Setup rest client.
     */
    private static void setupRestClient() {
        // create client
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(180, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(180, TimeUnit.SECONDS);
        //HTTP is the way modern applications network. It�s how we exchange data & media.
        // Doing HTTP efficiently makes your stuff load faster and saves bandwidth.
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(root)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(client));
        //The REST adapter allows your store to communicate with an HTTP server by transmitting JSON via XHR.
        // Most Ember.js apps that consume a JSON API should use the REST adapter.
        RestAdapter restAdapter = builder.build();
        //create the rest adapter
        sWelcomeApiInterface = restAdapter.create(UserPollApiInterface.class);
    }

}
