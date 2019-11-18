package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.TermsAndConditionModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 8/4/2015.
 */
public interface TermApiInterface {
    // annotation used to post the data
    @Multipart
    @POST(Constants.TERMS_API)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Tearms and Condition api condition
    void postTermsData(@Part(Constants.ACTION) String email, Callback<TermsAndConditionModel> callback);

    // annotation used to post the data
    @Multipart
    @POST(Constants.ABOUTSUS_API)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //About us apir request
    void postAboutData(@Part(Constants.ACTION) String email, Callback<TermsAndConditionModel> callback);
}
