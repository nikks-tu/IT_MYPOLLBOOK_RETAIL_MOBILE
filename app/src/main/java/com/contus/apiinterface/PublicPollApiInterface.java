package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.PublicPollResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 9/8/2015.
 */
public interface PublicPollApiInterface {
    // annotation used to post the data
    @Multipart
    @POST(Constants.PUBLICPOLLS_API_PATH)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for the campaign poll details
    void postPublicApi(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.PAGE) int page, @Part(Constants.LIMIT_STRING) String limit, Callback<PublicPollResponseModel> callback);
}
