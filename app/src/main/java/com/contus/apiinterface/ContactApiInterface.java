package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.ContactResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by Prem on 10-12-2015.
 */
public interface ContactApiInterface {
    // annotation used to post the data
    @Multipart
    @POST(Constants.CONTACT_DETAILS)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Requesting for the countact us
    void postContactDetails(@Part(Constants.ACTION) String email,@Part(Constants.USER_ID) String userid,@Part("mobile_number") String mobileNumber, Callback<ContactResponseModel> callback);
}
