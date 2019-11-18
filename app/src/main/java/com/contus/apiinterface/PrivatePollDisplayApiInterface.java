package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.PrivatePollResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 3/2/16.
 */
public interface PrivatePollDisplayApiInterface {
    // annotation used to post the data
    @Multipart
    @POST("/chatpolls")//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for user polls
    void postPrivatePollDisplayApi(@Part(Constants.ACTION) String action, @Part("shared_user_id") String userId,@Part("receiver_user_id") String reciverUserId,@Part("type") String type,@Part("group_id") String group_id,@Part("page") String page, Callback<PrivatePollResponseModel> callback);
}
