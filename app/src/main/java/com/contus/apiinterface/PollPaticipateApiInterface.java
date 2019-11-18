package com.contus.apiinterface;

import com.contus.responsemodel.PollParticipateResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 10/23/2015.
 */
public interface PollPaticipateApiInterface {
    // annotation used to post the data
    @Multipart
    @POST("/pollParticipate")//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Participating the poll
    void postParticipateApi(@Part("action") String action, @Part("user_id") String userId, @Part("poll_id") String pollId, @Part("poll_answer_id") String pollAnswerId, @Part("poll_answer") String pollAnswer, @Part("poll_answer_option") String pollAnwerOption, @Part("status") String status, Callback<PollParticipateResponseModel> callback);
}
