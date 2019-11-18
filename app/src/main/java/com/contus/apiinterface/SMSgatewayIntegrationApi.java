package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.SMSgatewayResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 9/18/2015.
 */
public interface SMSgatewayIntegrationApi {
    // annotation used to post the data
    @Multipart
    @POST(Constants.SMS_API)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //SMS gateway request
    void postSMSgateway(@Part(Constants.ACTION) String email, @Part(Constants.PHONE_NUM) String phoneNumber, Callback<SMSgatewayResponseModel> callback);
}
