package com.contus.apiinterface;

import com.contus.app.Constants;
import com.contus.responsemodel.CategoryPollResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 9/10/2015.
 */
public interface CategoryApiInterface {
    // annotation used to post the data
    @Multipart
    @POST(Constants.CATEGORY_API)//An object can be specified for use as an HTTP request body with the @Body annotation.
    void postCategoryData(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, Callback<CategoryPollResponseModel> callback);
    // annotation used to post the data
    @Multipart
    @POST(Constants.CATEGORY_SELECT_API)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for the category save
    void saveCategoryData(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.CATEGORY_ID) String id, Callback<CategoryPollResponseModel> callback);
}
