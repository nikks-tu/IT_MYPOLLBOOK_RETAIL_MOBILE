package com.new_chanages.api_interface;

import com.contus.app.Constants;
import com.new_chanages.models.AppVersionMainObject;
import com.new_chanages.models.AppVersionPostParameters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface AppVersionDataInterface {

    @POST(Constants.APP_VERSION_CHECK)
   // Call<CurrentDataMainObject> fetchCurrentData(@Part("deviceId") String deviceId, @Part("userId") String userId);

    Call<AppVersionMainObject> getStringScalar(@Body AppVersionPostParameters postParameter);

}
