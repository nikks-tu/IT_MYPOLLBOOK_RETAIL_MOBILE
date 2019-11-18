package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.ContactUsResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 8/10/2015.
 */
public interface ContactUseApiInterface {
    // annotation used to post the data
    @Multipart
    @POST(Constants.CONTACTUS_API)////An object can be specified for use as an HTTP request body with the @Body annotation.
    //Requesting for the countact us
    void postContactUs(@Part(Constants.ACTION) String email, Callback<ContactUsResponseModel> callback);

}
