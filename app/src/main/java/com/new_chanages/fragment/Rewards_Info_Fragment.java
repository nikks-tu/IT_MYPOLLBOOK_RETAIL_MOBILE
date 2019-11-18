package com.new_chanages.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.activity.CustomRequest;
import com.contus.app.Constants;
import com.contus.restclient.UserPollRestClient;
import com.contusfly.MApplication;
import com.new_chanages.models.GetErnigsInputModel;
import com.polls.polls.R;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Rewards_Info_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SwipeRefreshLayout mSwipeRefreshLayout;

  TextView like_tv,comment_tv,create_poll_tv,voting_tv,today_cups_tv,today_dimands_tv,total_cups_tv,total_dimands_tv;

    public Rewards_Info_Fragment() {
        // Required empty public constructor
    }

    public static Rewards_Info_Fragment newInstance(String param1, String param2) {
        Rewards_Info_Fragment fragment = new Rewards_Info_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rewards_info_frag_layout, container, false);


        like_tv = view.findViewById(R.id.like_tv);
        comment_tv = view.findViewById(R.id.comment_tv);
        create_poll_tv= view.findViewById(R.id.create_poll_tv);
        voting_tv = view.findViewById(R.id.voting_tv);

        today_cups_tv = view.findViewById(R.id.today_cups_tv);
        today_dimands_tv = view.findViewById(R.id.today_dimands_tv);
        total_cups_tv = view.findViewById(R.id.total_cups_tv);
        total_dimands_tv= view.findViewById(R.id.total_dimands_tv);
        mSwipeRefreshLayout =view.findViewById(R.id.mSwipeRefreshLayout);

        getInfo();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInfo();
            }
        });

        return view;
    }

    private void getInfo() {
        JSONObject obj = new JSONObject();
        String   userId = MApplication.getString(getContext(), Constants.USER_ID);


        UserPollRestClient.getInstance().getEarnigs(new GetErnigsInputModel("earningsactionsapi",userId), new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response1) {

                String result =CustomRequest.ResponcetoString(response1);
                try {
                    JSONObject response = new JSONObject(result);
                    mSwipeRefreshLayout.setRefreshing(false);
                    int success=response.optInt("success");

                    if(success==1)
                    {
                        JSONObject results =response.optJSONObject("results");
                        String commenting_poll =results.optString("commenting_poll");
                        String creating_poll =results.optString("creating_poll");
                        String liking_poll =results.optString("liking_poll");
                        String participating_admin_poll =results.optString("participating_admin_poll");
                        String participating_user_poll =results.optString("participating_user_poll");
                        JSONObject total_points =results.optJSONObject("total_points");
                        JSONObject todays_points =results.optJSONObject("todays_points");
                        String tot_cups =total_points.optString("cups");
                        String tot_diamonds =total_points.optString("diamonds");

                        String today_cups =todays_points.optString("cups");
                        String today_diamonds =todays_points.optString("diamonds");

                        like_tv.setText(liking_poll);
                        comment_tv.setText(commenting_poll);
                        create_poll_tv.setText(creating_poll);
                        voting_tv.setText(participating_user_poll);
                        today_cups_tv.setText(today_cups);
                        today_dimands_tv.setText(today_diamonds);
                        total_cups_tv.setText(tot_cups);
                        total_dimands_tv.setText(tot_diamonds);

                        MApplication.setString(getActivity(),"ToTal_points",tot_cups);



                    }
                    else
                    {
                        Toast.makeText(getContext(),response.getString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void failure(RetrofitError error) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        /*CustomRequest.makeJsonObjectRequest(getActivity(), Constants.GET_EARNING,obj, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
//            dialog.dismiss();
                Log.i("onErrormessage", "message= " +message);
            }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Log.i("PCCmessage", "message " + result.getString("msg"));
                    int success=response.optInt("success");

                    if(success==1)
                    {
                        JSONObject results =response.optJSONObject("results");
                        String commenting_poll =results.optString("commenting_poll");
                        String creating_poll =results.optString("creating_poll");
                        String liking_poll =results.optString("liking_poll");
                        String participating_admin_poll =results.optString("participating_admin_poll");
                        String participating_user_poll =results.optString("participating_user_poll");
                        JSONObject total_points =results.optJSONObject("total_points");
                        JSONObject todays_points =results.optJSONObject("todays_points");
                        String tot_cups =total_points.optString("cups");
                        String tot_diamonds =total_points.optString("diamonds");

                        String today_cups =todays_points.optString("cups");
                        String today_diamonds =todays_points.optString("diamonds");

                        like_tv.setText(liking_poll);
                        comment_tv.setText(commenting_poll);
                        create_poll_tv.setText(creating_poll);
                        voting_tv.setText(participating_user_poll);
                        today_cups_tv.setText(today_cups);
                        today_dimands_tv.setText(today_diamonds);
                        total_cups_tv.setText(tot_cups);
                        total_dimands_tv.setText(tot_diamonds);



                    }
                    else
                    {
                        Toast.makeText(getContext(),response.getString("msg"),Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });*/
    }


}
