package com.new_chanages.activity;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.new_chanages.AppConstents;
import com.polls.polls.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Rewards_Info_Activity extends AppCompatActivity {
    ImageView back_iv;
    ProgressDialog progressDialog;

    TextView adminpoll_tv,cutomer_poll_tv,linkingpoll_tv,comment_poll_tv,create_poll_tv;
    TextView  today_points_tv,totol_points_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rewards__info__activty);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        back_iv    = findViewById(R.id.back_iv);
        adminpoll_tv      = findViewById(R.id.adminpoll_tv);
        cutomer_poll_tv      = findViewById(R.id.cutomer_poll_tv);
        linkingpoll_tv      = findViewById(R.id.linkingpoll_tv);
        comment_poll_tv      = findViewById(R.id.comment_poll_tv);
        create_poll_tv      = findViewById(R.id.create_poll_tv);
        today_points_tv     = findViewById(R.id.today_points_tv);
        totol_points_tv     = findViewById(R.id.total_points_tv);

        try {
            if (getIntent().getExtras() != null) {
                String total_points = getIntent().getExtras().getString("total_points");
                String todays_points = getIntent().getExtras().getString("todays_points");
                today_points_tv.setText(todays_points);
                totol_points_tv.setText(total_points);
            }
        }catch (Exception ae)
        {
            ae.printStackTrace();
        }

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        service_call();
    }



    private void service_call() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        JSONObject jsonObj = new JSONObject();
        String session_id = null;

        try {

            jsonObj.put("action", "earningsactionsapi");

        } catch (JSONException e) {

        }
        Log.v("JSONObject", jsonObj.toString());
        JsonObjectRequest req = new JsonObjectRequest(AppConstents.REWARD_INFO, jsonObj,
                new Response.Listener<JSONObject>() {

                    String fName, lName, phoneNumber, zipCode, emailId;

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            progressDialog.dismiss();
                            Log.v("Response:%n %s", response.toString());


                            String succss = response.optString("success");
                            String msg = response.optString("msg");
                            JSONObject results =response.optJSONObject("results");


                            if (succss.equalsIgnoreCase("1"))
                            {
                               String commenting_poll= results.optString("commenting_poll");
                                String creating_poll= results.optString("creating_poll");
                                String liking_poll= results.optString("liking_poll");
                                String participating_admin_poll= results.optString("participating_admin_poll");
                                String participating_user_poll= results.optString("participating_user_poll");

                                adminpoll_tv.setText(participating_admin_poll+" Points");
                                cutomer_poll_tv.setText(participating_user_poll+" Points");
                                comment_poll_tv.setText(commenting_poll+" Points");
                                create_poll_tv.setText(creating_poll+" Points");
                                linkingpoll_tv.setText(liking_poll+" Points");


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
