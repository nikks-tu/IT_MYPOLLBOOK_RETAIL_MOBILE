package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.PollReviewResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 10/26/2015.
 */
public interface PollReviewInterface {
    @Multipart//An object can be specified for use as an HTTP request body with the @Body annotation.
    @POST(Constants.CAMPAIGN_REVIEW_API_PATH) // annotation used to post the data
    void pollCampaignReview(@Part(Constants.ACTION) String action, @Part(Constants.POLL_ID) String pollID, @Part(Constants.TYPE) String type, Callback<PollReviewResponseModel> callback);
    @Multipart//An object can be specified for use as an HTTP request body with the @Body annotation.
    @POST("/pollsReview") // annotation used to post the data
    void pollReview(@Part(Constants.ACTION) String action, @Part(Constants.POLL_ID) String pollID, @Part(Constants.TYPE) String type, Callback<PollReviewResponseModel> callback);
}
