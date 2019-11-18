package com.contus.apiinterface;

import com.contus.responsemodel.PersonalInfoModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 8/5/2015.
 */
public interface PersonalInfoApiInterface {
    // annotation used to post the data
    @Multipart
    @POST("/appuser")//An object can be specified for use as an HTTP request body with the @Body annotation.
    //request for updating personal info
    void postProfileInfo(@Part("action") String action, @Part("username") String phoneNumber,
                         @Part("name") String name,
                         @Part("gender") String gender, @Part("country_id") String countryId,
                         @Part("country_code") String countryCode, @Part("device_token") String deviceToken,
                         @Part("mobile_no") String phone, @Part("country") String country,
                         @Part("is_active") String status, @Part("image") String profileImg,
                         @Part("latitude") String latitude, @Part("longtitude") String longitude,
                         @Part("os_version") String osversion, @Part("os_name") String osName,
                         @Part("location") String location, Callback<PersonalInfoModel> callback);

}
