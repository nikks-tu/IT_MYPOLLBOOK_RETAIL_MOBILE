package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.ParticipateResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 11/4/2015.
 */
public interface PollReviewApiInterface {
    // annotation used to post the data
    @Multipart
    @POST("/pollAnswerSurvey")//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for poll survey details
    void postPollReview(@Part(Constants.ACTION) String action, @Part("user_id") String userId, @Part("poll_id") String pollId, @Part("poll_answer_option") String pollAnswerOption, @Part("PAGE") int page, Callback<ParticipateResponseModel> callback);
}
