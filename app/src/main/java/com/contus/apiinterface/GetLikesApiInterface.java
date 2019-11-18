/**
 * @category TeasorTrailor
 * @package com.contus.apiInterface
 * @version 1.0
 * @author Contus Team <developers@contus.in>
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contus.apiinterface;

import com.contus.responsemodel.LikesResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 9/28/2015.
 */
public interface GetLikesApiInterface {
    // annotation used to post the data
    @Multipart
    @POST("/getCampaignLikesApi")////An object can be specified for use as an HTTP request body with the @Body annotation.
    //Getting the like details for the campiagn
    void getLikesDetailsCampaign(@Part("action") String action, @Part("campaign_id") String campaignId, @Part("limit") String limit, @Part("page") int page, Callback<LikesResponseModel> callback);
    // annotation used to post the data
    @Multipart
    @POST("/getPollLikesApi")////An object can be specified for use as an HTTP request body with the @Body annotation.
    //Getting the like details for the poll
    void getLikesDetailsCampaignPoll(@Part("action") String action, @Part("poll_id") String campaignId, @Part("limit") String limit, @Part("page") int page, Callback<LikesResponseModel> callback);


}
