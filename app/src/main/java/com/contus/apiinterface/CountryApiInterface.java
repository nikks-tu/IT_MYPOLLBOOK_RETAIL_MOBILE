package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.CountryResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 8/3/2015.
 */
public interface CountryApiInterface {
    // annotation used to post the data
    @Multipart
    @POST(Constants.COUNTRY_API)////An object can be specified for use as an HTTP request body with the @Body annotation.
    //Requesting for the country api
    void postCountryData(@Part(Constants.ACTION) String email, Callback<CountryResponseModel> callback);
}
