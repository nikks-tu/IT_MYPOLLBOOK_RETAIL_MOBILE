package com.new_chanages.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contusfly.MApplication;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.new_chanages.activity.AllContactsActivity;
import com.new_chanages.activity.GroupCreate;
import com.new_chanages.adapters.GroupsAdapter;
import com.new_chanages.api_interface.GroupsApiInterface;
import com.new_chanages.listeners.RecyclerItemClickListener;
import com.new_chanages.models.GroupsNameObject;
import com.new_chanages.postParameters.GetGroupsPostParameters;
import com.polls.polls.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GroupsFragment extends Fragment implements GroupPolls.GroupPollOnFragmentInteractionListner{

    RecyclerView groups_recyclerview;
    GroupsAdapter adapter;
    private int page;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String userId;
    private Activity groupsFragment;
    private SmoothProgressBar userPollGoogleNow;
    private RelativeLayout internetConnection;
    FrameLayout fragment2;
    TextView txtInternetConnection;
    ArrayList<GroupsNameObject> groupsList;
    String action="getgroupsapi";
    Context mContext;
    ImageView img_add_group;
    int groupId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.groups_fragment_layout,container,false);
        groups_recyclerview =view.findViewById(R.id.groups_recyclerview);
        groups_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        internetConnection =  view.findViewById(R.id.internetConnection);
        userPollGoogleNow =  view.findViewById(R.id.google_now);
        txtInternetConnection = view.findViewById(R.id.txtInternetConnection);

        pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        fragment2 = view.findViewById(R.id.fragment2);
        initialize();
        internetConnection.setVisibility(View.GONE);
        //View visible
        groups_recyclerview.setVisibility(View.VISIBLE);
        /* Progress bar visibility**/
        userPollGoogleNow.setVisibility(View.VISIBLE);
        userPollGoogleNow.progressiveStart();
        serviceCallForGroups();


        groups_recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), groups_recyclerview, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                groupId = groupsList.get(position).getGroupId();
                String groupName = groupsList.get(position).getGroupName();

               // Toast.makeText(mContext, ""+groupId, Toast.LENGTH_SHORT).show();
                MApplication.setString(mContext, Constants.GET_GROUP_POLL_ID, String.valueOf(groupId));
                MApplication.setString(mContext, Constants.GET_GROUP_NAME, String.valueOf(groupName));
                imageChangeFragment(new GroupPolls(), groupsList.get(position).getGroupId());

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        img_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAdded()){
                    getResources().getString(R.string.app_name);

                    MDatabaseHelper obj = new MDatabaseHelper(getActivity());
                    obj.deleteSelectedContacts();
                    obj.close();

                    Intent intent = new Intent(groupsFragment, AllContactsActivity.class);
                    editor.putString("group_names",""); // Storing string
                    editor.commit();
                    MApplication.setString(mContext, Constants.GET_GROUP_POLL_ID,"");
                    startActivity(intent);
                }

            }
        });

        return view;
    }

    private void imageChangeFragment(Fragment targetFragment, Integer groupId) {
      /*  //clear view list
        resideMenu.clearIgnoredViewList();
        //Visibility gone
        customFragmnet.setVisibility(View.VISIBLE);
        //set visiblity view
        mFragment.setVisibility(View.INVISIBLE);*/


        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack

        String backStateName = targetFragment.getClass().getName();

        FragmentManager manager = getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragment2, targetFragment, targetFragment.getClass().getName());
            ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
       /* if (backStateName.equals(UserPolls.class.getName())) {
            chat.setVisibility(View.INVISIBLE);
            publicPoll.setVisibility(View.INVISIBLE);
            userPoll.setVisibility(View.VISIBLE);
        } else {
            chat.setVisibility(View.INVISIBLE);
            publicPoll.setVisibility(View.INVISIBLE);
            userPoll.setVisibility(View.INVISIBLE);
        }*/
    }

    private void initialize() {
        groupsFragment = getActivity();
        mContext = getContext();
        userId = MApplication.getString(groupsFragment, Constants.USER_ID);
        page = 1;
        groupsList = new ArrayList<>();
        Toolbar toolbar = getActivity().findViewById(R.id.mToolbar);
        LinearLayout layout_top = getActivity().findViewById(R.id.layout_top);
        layout_top.setVisibility(View.GONE);
        TextView title =  toolbar.findViewById(R.id.toolbar_title);
        Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/Quicksand-Light.ttf");
        title.setText("Groups");
        title.setTypeface(face);
        title.setMaxLines(1);
        title.setEllipsize(TextUtils.TruncateAt.END);
        img_add_group = toolbar.findViewById(R.id.img_add_group);
        ImageView imgSearch = toolbar.findViewById(R.id.imgSearch);
        ImageView imgEdit = toolbar.findViewById(R.id.imgEdit);
        imgSearch.setVisibility(View.GONE);
        imgEdit.setVisibility(View.GONE);
        img_add_group.setImageDrawable(mContext.getResources().getDrawable(R.drawable.add_group));
        img_add_group.setVisibility(View.VISIBLE);
    }


    private void serviceCallForGroups() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GroupsApiInterface service = retrofit.create(GroupsApiInterface.class);
       Call<JsonElement> call = service.getAllGroups(new GetGroupsPostParameters(action, Integer.parseInt(userId)));
        call.enqueue(new Callback<JsonElement>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                //hideloader();
               if (response.body() != null) {

                   userPollGoogleNow.progressiveStop();
                   //Google progress stop
                   userPollGoogleNow.setVisibility(View.GONE);
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
                                   groupsList.add(object);
                               }
                           }
                           if(groupsList.size()>0)
                           {
                               internetConnection.setVisibility(View.GONE);
                               groups_recyclerview.setVisibility(View.VISIBLE);
                               adapter = new GroupsAdapter(getContext(), groupsList);
                               groups_recyclerview.setAdapter(adapter);
                           }
                       }
                       else {
                           internetConnection.setVisibility(View.VISIBLE);
                           groups_recyclerview.setVisibility(View.GONE);
                           userPollGoogleNow.progressiveStop();
                           userPollGoogleNow.setVisibility(View.GONE);
                           txtInternetConnection.setText("No groups found");
                       }

                    } else {
                        internetConnection.setVisibility(View.VISIBLE);
                        groups_recyclerview.setVisibility(View.GONE);
                        userPollGoogleNow.progressiveStop();
                        userPollGoogleNow.setVisibility(View.GONE);
                        txtInternetConnection.setText("No groups found");

                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        userPollGoogleNow.progressiveStart();
        serviceCallForGroups();
        super.onResume();
    }

    @Override
    public void OnUserPollFragment(String type, String id) {
        imageChangeFragment(new GroupPolls(), groupId);
    }
}
