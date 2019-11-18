package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.CountryResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 8/19/2015.
 */
public interface SearchCountryApi {
    // annotation used to post the data
    @Multipart
    @POST(Constants.SEARCH_COUNTRY_API)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for searching the country
    void postLoginData(@Part(Constants.ACTION) String email, @Part(Constants.SEARCH_KEY) String search, Callback<CountryResponseModel> callback);
}
