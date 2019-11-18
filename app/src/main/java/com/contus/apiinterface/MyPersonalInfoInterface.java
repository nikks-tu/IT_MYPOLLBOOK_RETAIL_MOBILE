package com.contus.apiinterface;


import com.contus.responsemodel.MyPersonalInfoModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 8/7/2015.
 */
public interface MyPersonalInfoInterface {
    // annotation used to post the data
    @Multipart
    @POST("/getUserProfile")//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Personal info request
    void postPersonalInfo(@Part("action") String action, @Part("user_id") String username, Callback<MyPersonalInfoModel> callback);
}
