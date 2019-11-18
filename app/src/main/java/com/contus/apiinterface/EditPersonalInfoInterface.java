package com.contus.apiinterface;

import com.contus.responsemodel.EditPersonalInfoModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 8/8/2015.
 */
public interface EditPersonalInfoInterface {
    // annotation used to post the data
    @Multipart
    @POST("/editUserProfile")////An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for the personal edit info
    void postEditPersonalInfo(@Part("action") String action, @Part("user_id") String userId, @Part("name") String username, @Part("gender") String gender, @Part("status_msg") String userStatus, @Part("country_id") String countryId, @Part("longtitude") String longitude, @Part("latitude") String latitude, @Part("image") String profile_img, @Part("location") String location,@Part("activation_key") String activationKey, Callback<EditPersonalInfoModel> callback);
}
