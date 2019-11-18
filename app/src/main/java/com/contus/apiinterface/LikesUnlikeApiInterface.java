/**
 * @category TeasorTrailor
 * @package com.contus.apiInterface
 * @version 1.0
 * @author Contus Team <developers@contus.in>
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contus.apiinterface;

import com.contus.responsemodel.LikeUnLikeResposneModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 9/28/2015.
 */
public interface LikesUnlikeApiInterface {
    // annotation used to post the data
    @Multipart
    @POST("/campaignLikesApi")////An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for poll like
    void postLikes(@Part("action") String action, @Part("user_id") String userId, @Part("campaign_id") String campaignId, @Part("likes") String likes, Callback<LikeUnLikeResposneModel> callback);

    // annotation used to post the data
    @Multipart
    @POST("/pollLikesApi")////An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for campaign like
    void postCampaignPollLikes(@Part("action") String action, @Part("user_id") String userId, @Part("poll_id") String campaignId, @Part("likes") String likes, Callback<LikeUnLikeResposneModel> callback);
}
