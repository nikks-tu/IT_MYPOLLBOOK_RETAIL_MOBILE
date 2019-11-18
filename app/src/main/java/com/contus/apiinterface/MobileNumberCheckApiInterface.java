package com.contus.apiinterface;

import com.contus.responsemodel.MobileNumberChange;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 8/18/2015.
 */
public interface MobileNumberCheckApiInterface {
    // annotation used to post the data
    @Multipart
    @POST("/mobilenoexist")//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request fro mobile number verification
    void postCheckMobileData(@Part("action") String actiion, @Part("mobile_no") String mobileno, Callback<MobileNumberChange> callback);
}
