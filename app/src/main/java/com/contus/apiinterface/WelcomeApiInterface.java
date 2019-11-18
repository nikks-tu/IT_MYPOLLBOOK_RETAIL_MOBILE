package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.WelcomeResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 8/3/2015.
 */
public interface WelcomeApiInterface {
    // annotation used to post the data
    @Multipart
    @POST(Constants.WELCOME_API)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for welcome api
    void postLoginData(@Part(Constants.ACTION) String email, Callback<WelcomeResponseModel> callback);
}
