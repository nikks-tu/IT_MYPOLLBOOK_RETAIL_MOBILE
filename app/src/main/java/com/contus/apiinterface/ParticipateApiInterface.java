package com.contus.apiinterface;

import com.contus.responsemodel.ParticipateResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 10/30/2015.
 */
public interface ParticipateApiInterface {
    // annotation used to post the data
    @Multipart
    @POST("/pollsParticipateUsers")//An object can be specified for use as an HTTP request body with the @Body annotation.
    //User participate details
    void postParticipate(@Part("action") String action, @Part("poll_id") String pollId, @Part("page") int page, Callback<ParticipateResponseModel> callback);
}
