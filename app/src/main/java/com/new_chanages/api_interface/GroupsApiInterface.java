package com.new_chanages.api_interface;

import com.contus.app.Constants;
import com.google.gson.JsonElement;
import com.new_chanages.models.AddContactsToGroup;
import com.new_chanages.models.AppVersionPostParameters;
import com.new_chanages.models.GroupPollsMainObject;
import com.new_chanages.models.RemoveContactPostParameter;
import com.new_chanages.postParameters.GetGroupsPostParameters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GroupsApiInterface {

    @POST(Constants.GET_GROUPS)
    Call<JsonElement> getAllGroups(@Body GetGroupsPostParameters postParameter);

    @POST(Constants.CREATE_GROUP)
    Call<JsonElement> createGroup(@Body GetGroupsPostParameters postParameter);

    @POST(Constants.CHECK_CONTACTS)
    Call<JsonElement> checkExistingContacts(@Body GetGroupsPostParameters postParameter);

    @POST(Constants.GET_GROUP_POLLS)
    Call<GroupPollsMainObject> getGroupPolls(@Body GetGroupsPostParameters postParameter);

    @POST(Constants.GET_SINGLE_GROUP)
    Call<JsonElement> getSingleGroup(@Body AppVersionPostParameters postParameter);

    @POST(Constants.REMOVE_CONTACTS_FROM_GROUP)
    Call<JsonElement> removeContactsFromGroup(@Body RemoveContactPostParameter postParameter);

    @POST(Constants.ADD_CONTACTS_TO_GROUP)
    Call<JsonElement> addcontactstogroup(@Body AddContactsToGroup postParameter);


}
