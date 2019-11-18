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
public interface CommentsDisplayApiInterface {
    // annotation used to post the data
    @Multipart
    @POST(Constants.GET_POLL_COMMENTS_API)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for getting poll COMMENTS details
    void postPollCommentsDetsils(@Part(Constants.ACTION) String action, @Part(Constants.POLL_ID) String campaignId, @Part(Constants.PAGE_LIMIT) String limit, @Part(Constants.PAGE) int page, Callback<CommentDisplayResponseModel> callback);
    // annotation used to post the data
    @Multipart
    @POST(Constants.GET_CAMPAIGN_COMMENTS_API)////An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for getting campaign COMMENTS details
    void postCommentsDetsils(@Part(Constants.ACTION) String action, @Part(Constants.CAMPAIGN_UNIQUE_ID) String campaignId, @Part(Constants.PAGE_LIMIT) String limit, @Part(Constants.PAGE) int page, Callback<CommentDisplayResponseModel> callback);
}
