package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.UserPollResponseModel;
import com.contusfly.model.PollUnseenStatusModel;
import com.new_chanages.models.GetErnigsInputModel;
import com.new_chanages.models.GetsharedURL_InputModel;
import com.new_chanages.models.TopUserInputModel;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit2.http.PUT;

/**
 * Created by user on 10/6/2015.
 */
public interface UserPollApiInterface {
    // annotation used to post the data
    @Multipart
    @POST(Constants.USER_POLL_API)
//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for user polls
    void postUserApi(@Part(Constants.ACTION) String action, @Part(Constants.PAGE) int page, @Part(Constants.USER_ID) String userId, @Part(Constants.DEVICE_ID) String deviceId,Callback<UserPollResponseModel> callback);

    @Multipart
    @POST(Constants.POLL_UNSEEN_API)
//An object can be specified for use as an HTTP request body with the @Body annotation.
        //Request for user polls
    void postUnseenCount(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part("type") String type, Callback<PollUnseenStatusModel> callback);

    // annotation used to post the data
    @Multipart
    @POST(Constants.MYPOLL_API)
//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for my polls
    void postMyPollApi(@Part(Constants.ACTION) String action, @Part(Constants.PAGE) int page, @Part(Constants.USER_ID) String userId, Callback<UserPollResponseModel> callback);

    @POST(Constants.TOP_10_USERS_retrofit)
    void topTeanUsers(@Body TopUserInputModel body, Callback<JSONObject> callback);

    @POST(Constants.EARNINGS_retrofit)
    void getEarnigs(@Body GetErnigsInputModel body, Callback<JSONObject> callback);

    @POST(Constants.GET_SHARE_URL)
    void getShareURL(@Body GetsharedURL_InputModel body, Callback<JSONObject> callback);

    @POST(Constants.GET_REWARDS_RETROFIT)
    void getRewards(@Body GetErnigsInputModel body, Callback<JSONObject> callback);


}
