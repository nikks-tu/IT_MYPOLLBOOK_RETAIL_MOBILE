package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.PollPrivateResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 1/19/2016.
 */
public interface PrivatePollApiInterface {
    // annotation used to post the data
    @Multipart
    @POST("/privatePolls")//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for the campaign poll details
    void postPrivatePoll(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId,@Part("last_seen_date") String limit, Callback<PollPrivateResponseModel> callback);
}
