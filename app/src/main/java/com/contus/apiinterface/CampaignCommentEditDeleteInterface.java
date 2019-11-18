package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.CampaignCommentDelete;
import com.contus.responsemodel.CommentDisplayResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 10/20/2015.
 */
public interface CampaignCommentEditDeleteInterface {
    // annotation used to post the data
    @Multipart
    @POST(Constants.DELETE_CAMP_COMMENTS)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for the campaign comment delete
    void postCampaignCommentDelete(@Part(Constants.ACTION) String action, @Part(Constants.CAMPAIGN_UNIQUE_ID) String campaignId, @Part(Constants.USER_ID) String userId, @Part(Constants.COMMENT_ID) String commentId, Callback<CampaignCommentDelete> callback);
    // annotation used to post the data
    @Multipart
    @POST(Constants.EDIT_CAMP_COMMENTS)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for the campaign comment edit
    void postCampaignCommentEdit(@Part(Constants.ACTION) String action, @Part(Constants.CAMPAIGN_UNIQUE_ID) String campaignId, @Part(Constants.USER_ID) String userId, @Part(Constants.COMMENT_ID) String commentId, @Part(Constants.COMMENTS) String comments, Callback<CommentDisplayResponseModel> callback);
    // annotation used to post the data
    @Multipart
    @POST(Constants.DELETE_CAMP_POLL_COMMENTS)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for the poll comment delete
    void postCampaignPollCommentDelete(@Part(Constants.ACTION) String action, @Part(Constants.POLL_ID) String campaignId, @Part(Constants.USER_ID) String userId, @Part(Constants.POLL_COMMENT_ID) String commentId,
                                       Callback<CampaignCommentDelete> callback);
    // annotation used to post the data
    @Multipart
    @POST(Constants.EDIT_POLL_COMMENTS)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for the poll comment edit
    void postCampaignPollCommentEdit(@Part(Constants.ACTION) String action, @Part(Constants.POLL_ID) String campaignId, @Part(Constants.USER_ID) String userId, @Part(Constants.POLL_COMMENT_ID) String commentId, @Part(Constants.POLL_COMMENTS) String comments, Callback<CommentDisplayResponseModel> callback);
}
