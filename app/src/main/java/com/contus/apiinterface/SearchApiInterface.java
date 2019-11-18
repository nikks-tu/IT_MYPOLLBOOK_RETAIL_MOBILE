package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.UserPollResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 10/9/2015.
 */
public interface SearchApiInterface {
    // annotation used to post the data
    @Multipart
    @POST(Constants.SEARCH_API_PATH)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for searching the poll
    void searchUserPOll(@Part(Constants.ACTION) String action, @Part(Constants.SEARCH) String search, @Part(Constants.PAGE) int page,@Part(Constants.USER_ID) String userId, Callback<UserPollResponseModel> callback);
}
