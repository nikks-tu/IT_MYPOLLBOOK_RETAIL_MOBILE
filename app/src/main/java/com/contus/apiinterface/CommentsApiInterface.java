package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.CommentDisplayResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 9/24/2015.
 */
public interface CommentsApiInterface {
    // annotation used to post the data
    @Multipart
    @POST(Constants.CAMP_COMMENTS_API)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //post COMMENTS to the campaign and poll
    void postComments(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.CAMPAIGN_UNIQUE_ID) String campaignId, @Part(Constants.COMMENTS) String comments, Callback<CommentDisplayResponseModel> callback);
    // annotation used to post the data
    @Multipart
    @POST(Constants.POLL_COMMENTS_API)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //post COMMENTS to the campaign and poll
    void postPollComments(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.POLL_ID) String campaignId, @Part(Constants.COMMENTS) String comments, Callback<CommentDisplayResponseModel> callback);
}
