package com.new_chanages.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.contus.app.Constants;
import com.contusfly.MApplication;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.new_chanages.adapters.GroupCheckListAdapter;
import com.new_chanages.api_interface.GroupsApiInterface;
import com.new_chanages.models.GroupPollDataObject;
import com.new_chanages.models.GroupsNameObject;
import com.new_chanages.postParameters.GetGroupsPostParameters;
import com.polls.polls.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GroupSelection extends AppCompatActivity {

    ListView groupSelectionrecycler ;
    ArrayList<GroupsNameObject> groupsList;
    String action="getgroupsapi";
    private String userId;
    Button done;
    DatabaseHelper dbhelper;
    ImageView imagBackArrow;

    GroupCheckListAdapter adapter;
    private static Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_selection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbhelper = new DatabaseHelper(activity);
        groupsList = (ArrayList<GroupsNameObject>)  getIntent().getSerializableExtra("DATA");

        userId = MApplication.getString(getApplicationContext(), Constants.USER_ID);

        initialize();


     //   serviceCallForGroups(userId);




        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });


        imagBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

   /* private void serviceCallForGroups(String userId) {

        DatabaseHelper dbhelper = new DatabaseHelper(activity);
        dbhelper.deleteGroupList();



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GroupsApiInterface service = retrofit.create(GroupsApiInterface.class);
        Call<JsonElement> call = service.getAllGroups(new GetGroupsPostParameters(action, Integer.parseInt(userId)));
        call.enqueue(new retrofit2.Callback<JsonElement>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                //hideloader();
                if (response.body() != null) {
                    JsonObject result = response.body().getAsJsonObject();
                    if (!result.isJsonNull()) {
                        String success = result.get("success").getAsString();
                        if(success.equals("1"))
                        {
                            groupsList = new ArrayList<>();
                            JsonArray jsonArray = result.get("results").getAsJsonArray();
                            if(jsonArray.size()>0)
                            {
                                for (int i=0; i<jsonArray.size();i++)
                                {
                                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                    GroupsNameObject object = new GroupsNameObject();
                                    object.setGroupId(jsonObject.get("group_id").getAsInt());
                                    object.setGroupImage(jsonObject.get("group_image").getAsString());
                                    object.setGroupName(jsonObject.get("group_name").getAsString());
                                    object.setGroupStatus("FALSE");
                                    groupsList.add(object);
                                }
                                // Toast.makeText(mYesOrNo, "Test"+groupsList.size(), Toast.LENGTH_SHORT).show();
                            }
                             adapter = new GroupCheckListAdapter(activity, groupsList);
                             groupSelectionrecycler.setAdapter(adapter);
                          *//*  if(groupsList.size()>0)
                            {
                                txtGroup.setVisibility(View.VISIBLE);
                            }
                            else {
                                txtGroup.setVisibility(View.GONE);
                            }*//*
                        }
                        else {
                        }

                    } else {

                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getApplicationContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }*/

    private void initialize() {
        activity = this;
        imagBackArrow=findViewById(R.id.imagBackArrow);
        done=findViewById(R.id.done);
        groupSelectionrecycler=findViewById(R.id.group_selection_list_view);

        adapter = new GroupCheckListAdapter(activity, groupsList);
        groupSelectionrecycler.setAdapter(adapter);

    }

}
