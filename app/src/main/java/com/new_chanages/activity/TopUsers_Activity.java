package com.new_chanages.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.new_chanages.AppConstents;
import com.new_chanages.adapters.Top10UsersAdapter;
import com.new_chanages.models.TopUsersModel;
import com.polls.polls.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TopUsers_Activity extends AppCompatActivity {

    RecyclerView topusers_list;
    ProgressDialog progressDialog;
    ArrayList<TopUsersModel> users_list;
    ImageView back_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_users__activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        back_iv    = findViewById(R.id.back_iv);

        service_call();

        topusers_list = findViewById(R.id.topusers_list);
        topusers_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }




    private void service_call() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        JSONObject jsonObj = new JSONObject();
        String session_id = null;

        try {

            jsonObj.put("action", "topusersapi");

        } catch (JSONException e) {

        }
        Log.v("JSONObject", jsonObj.toString());
        JsonObjectRequest req = new JsonObjectRequest(AppConstents.TOPUSERS, jsonObj,
                new Response.Listener<JSONObject>() {

                    String fName, lName, phoneNumber, zipCode, emailId;

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            progressDialog.dismiss();
                            Log.v("Response:%n %s", response.toString());

                            users_list = new ArrayList<>();

                            String succss = response.optString("success");
                            String msg = response.optString("msg");
                            JSONArray results =response.optJSONArray("results");


                            if (succss.equalsIgnoreCase("1"))
                            {

                                for(int i =0; i<results.length();i++)
                                {
                                    TopUsersModel obj = new TopUsersModel();
                                    JSONObject jobj =results.optJSONObject(i);
                                    String points =jobj.optString("points");
                                    String user_id =jobj.optString("user_id");
                                    String name =jobj.optJSONObject("user_infos").optString("name");
                                    String image =jobj.optJSONObject("user_infos").optString("image");
                                    obj.setImageurl(image);
                                    obj.setPoints(points);
                                    obj.setUserid(user_id);
                                    obj.setUsername(name);

                                    users_list.add(obj);
                                }

                                topusers_list.setAdapter(new Top10UsersAdapter(users_list));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.e("Error: ", error.getMessage());
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        req.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(req);
    }

}
