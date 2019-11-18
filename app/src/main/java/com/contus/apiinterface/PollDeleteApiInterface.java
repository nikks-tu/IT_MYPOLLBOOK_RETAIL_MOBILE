package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.PollDeleteResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 12/30/2015.
 */
public interface PollDeleteApiInterface {
    // annotation used to post the data
    @Multipart
    @POST("/deletemypolls")//An object can be specified for use as an HTTP request body with the @Body annotation.
        //Request for the campaign comment delete
    void postPollDelete(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String campaignId, @Part(Constants.POLL_ID) String userId,Callback<PollDeleteResponseModel> callback);
}
