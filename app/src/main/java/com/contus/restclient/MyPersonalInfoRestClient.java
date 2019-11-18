package com.contus.restclient;

import com.contus.apiinterface.MyPersonalInfoInterface;
import com.contus.app.Constants;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by user on 8/7/2015.
 */
public class MyPersonalInfoRestClient {
        private static MyPersonalInfoInterface sPersonalInterface; /** The s settings api interface. */
        private static String root = Constants.LIVE_BASE_URL+"api/v1"; /** The root. */
        static {
            setupRestClient();
        }

        /**
         * Instantiates a new settings rest client.
         */
        private MyPersonalInfoRestClient() {

        }

        /**
         * Gets the.
         *
         * @return the settings api interface
         */
        public static MyPersonalInfoInterface getInstance() {
            return sPersonalInterface;
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
            sPersonalInterface = restAdapter.create(MyPersonalInfoInterface.class);
        }
}
