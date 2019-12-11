package com.contus.MyProfileNew;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.contus.app.Constants;
import com.contus.myprofile.EditProfile;
import com.contus.responsemodel.MyPersonalInfoModel;
import com.contus.restclient.MyPersonalInfoRestClient;
import com.contusfly.MApplication;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.contusfly.utils.Utils;
import com.new_chanages.AppConstents;
import com.polls.polls.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyProfileNew extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView point_summary_tv;
    String total_points="0",todays_points="0";

    Activity myProfileFragment;
    private View parentView;
    //userid
    private String userId;

    //User name
    private String userName;
    //Text view user name
    private TextView txtUserName;
    //Location
    private TextView location;
    private TextView _gender;
    ImageView overlapImage,edit_profile_iv;
    private DatabaseHelper db;
    ProgressDialog progressDialog;
    TextView  today_points_tv, total_points_tv;
    ImageView back_iv;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnNewMyProfile mListener;

    public MyProfileNew() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileNew.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileNew newInstance(String param1, String param2) {
        MyProfileNew fragment = new MyProfileNew();
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
        // Inflate the layout for this fragment
        parentView =  inflater.inflate(R.layout.fragment_my_profile_new, container, false);

        point_summary_tv   = parentView.findViewById(R.id.point_summary_tv);
        today_points_tv     = parentView.findViewById(R.id.today_points_tv);
        total_points_tv = parentView.findViewById(R.id.total_points_tv);
        back_iv     = parentView.findViewById(R.id.back_iv);

        myProfileFragment = getActivity();
        //Setting the status bar color for version more than lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Setting the status bar color
            MApplication.settingStatusBarColor(myProfileFragment, getResources().getColor(android.R.color.black));
        }

        txtUserName = parentView.findViewById(R.id.userName);
        location =  parentView.findViewById(R.id.location);
        _gender =  parentView.findViewById(R.id.gender);
        overlapImage = parentView.findViewById(R.id.overlapImage);

        edit_profile_iv = parentView.findViewById(R.id.edit_profile_iv);
        db = new DatabaseHelper(getActivity());
        MApplication.setBoolean(getActivity(),"myprofile",true);

        TextView donetext_view =getActivity().findViewById(R.id.txtDone);
        donetext_view.setVisibility(View.GONE);


        //Getting the user id from the preference
        userId = MApplication.getString(getActivity(), Constants.USER_ID);
        //Interface definition for a callback to be invoked when a view is clicked.
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // finish();
            }
        });

        //Commented by Nikita
       /* point_summary_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Rewards_Info_Activity.class);
                i.putExtra("total_points",total_points);
                i.putExtra("todays_points",todays_points);
                startActivity(i);
            }
        });*/

        edit_profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intents are objects of the android.content.Intent type. Your code can send them to the Android system defining the components you are targeting.
                // For example, via the startActivity() method you can define that the intent should be used to start an activity.
                Intent a = new Intent(getActivity(), EditProfile.class);
                startActivity(a);
            }
        });

        service_call();
        return parentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            myProfileFragment = (Activity) context;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNewMyProfile {
        // TODO: Update argument type and name
        void onNewMyProfile(String type, String id);
    }

    private void service_call() {

       /* progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();*/


        JSONObject jsonObj = new JSONObject();
        String session_id = null;

        try {

            jsonObj.put("action", "rewardsapi");
            jsonObj.put("user_id", userId);


        } catch (JSONException e) {

        }
        Log.v("JSONObject", jsonObj.toString());
        JsonObjectRequest req = new JsonObjectRequest(AppConstents.TOTAL_POINTS, jsonObj,
                new com.android.volley.Response.Listener<JSONObject>() {

                    String fName, lName, phoneNumber, zipCode, emailId;

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //progressDialog.dismiss();
                            Log.v("Response:%n %s", response.toString());


                            String success = response.optString("success");
                            String msg = response.optString("msg");
                            JSONArray results =response.optJSONArray("results");


                            if (success.equalsIgnoreCase("1"))
                            {

                                total_points= results.optJSONObject(0).optString("total_points");
                                todays_points= results.optJSONObject(0).optString("todays_points");

                                if(!TextUtils.isEmpty(total_points)&&!total_points.equalsIgnoreCase("null"))
                                {
                                    total_points_tv.setText(total_points);
                                }
                                else
                                {
                                    total_points_tv.setText("0");
                                    total_points="0";
                                }
                                if(!TextUtils.isEmpty(todays_points)&&!todays_points.equalsIgnoreCase("null"))
                                {
                                    today_points_tv.setText(todays_points);
                                }
                                else
                                {
                                    today_points_tv.setText("0");
                                    todays_points="0";
                                }

                            }
                            else
                            {
                                Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.e("Error: ", error.getMessage());
                //progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        req.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(req);
    }

    private void webRequest() {
        Log.e("userId", userId + "");
        /*If internet connection is available**/
        if (MApplication.isNetConnected(myProfileFragment)) {
            //Visibility gone
            //internetConnection.setVisibility(View.GONE);
            //View visible
            //mainView.setVisibility(View.VISIBLE);
            MApplication.materialdesignDialogStart(myProfileFragment);
            /* Calling the material design custom dialog **/
            MyPersonalInfoRestClient.getInstance().postPersonalInfo("userProfileapi", userId
                    , new Callback<MyPersonalInfoModel>() {
                        @Override
                        public void success(MyPersonalInfoModel personalInfoResponseModel, Response response) {
                            /*If response is success this method is called**/
                            personalResponse(personalInfoResponseModel);
                            MApplication.materialdesignDialogStop();
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            //Error message popups when the user cannot able to coonect with the server
                            MApplication.errorMessage(retrofitError, myProfileFragment);
                            MApplication.materialdesignDialogStop();
                        }

                    });
        } else {
            //View visible
            //internetConnection.setVisibility(View.VISIBLE);
            //View gone
            //mainView.setVisibility(View.GONE);
        }

    }

    private void personalResponse(MyPersonalInfoModel personalInfoResponseModel) {
        //Success message
        String success = personalInfoResponseModel.getSuccess();
        //Isf the success message equals 1
        if (("1").equals(success)) {
            //User name from there response
            userName = personalInfoResponseModel.getResults().getUserName();
            //Setting the string the username
            MApplication.setString(myProfileFragment, Constants.PROFILE_USER_NAME, userName);
            //Get the profile image from the response
            String profileImage = personalInfoResponseModel.getResults().getProfileImg().replaceAll(" ", "%20");
            //Get the user location from the response
            String city = personalInfoResponseModel.getResults().getUserLocation();
            //Get the user gender from the response
            String gender = personalInfoResponseModel.getResults().getUserGender();
           // Toast.makeText(myProfileFragment, "Gen" + personalInfoResponseModel.getResults().getUserGender(), Toast.LENGTH_SHORT).show();

            //Get the country id from the response
            String countryId = personalInfoResponseModel.getResults().getCountryId();
            //Getting the user status from teh response
            String status = personalInfoResponseModel.getResults().getUserStatus();
            //access token
            String accessToken = personalInfoResponseModel.getResults().getAccessToken();
            String friendCount = personalInfoResponseModel.getResults().getFriendsCount();
            //If the status is empty set the default status
            //else set the status from the response
            if (("").equals(status)) {
                //Setting the value in preference
                MApplication.setString(myProfileFragment, Constants.STATUS, getResources().getString(R.string.status_empty));
            } else {
                //Setting the value in preference
                MApplication.setString(myProfileFragment, Constants.STATUS, status);
            }
            //saving in preference
            MApplication.setString(myProfileFragment, Constants.ACCESS_TOKEN, accessToken);
            //Setting the profile image in preference
            MApplication.setString(myProfileFragment, Constants.PROFILE_IMAGE, profileImage);
            //Setting the country id in preference
            MApplication.setString(myProfileFragment, Constants.COUNTRY_ID, countryId);
            //Setting the gender in preference
            MApplication.setString(myProfileFragment, Constants.USER_GENDER, gender);
            String gen = MApplication.getString(myProfileFragment, Constants.USER_GENDER);
            //Toast.makeText(myProfileFragment, "Gender "+ gen, Toast.LENGTH_SHORT).show();
            //Setting the city in preference
            MApplication.setString(myProfileFragment, Constants.CITY, city);
            //Setting the latitude in preference
            MApplication.setString(myProfileFragment, Constants.LATITUDE, personalInfoResponseModel.getResults().getLatitude());
            //Setting the longitude in preference
            MApplication.setString(myProfileFragment, Constants.LONGITUDE, personalInfoResponseModel.getResults().getLongtitude());
            //Setting the username
            txtUserName.setText(MApplication.getDecodedString(userName));
            //Setting the location
            location.setText(city);
            if (gender.equals("0")) {
              _gender.setText(R.string.male);
            } else{
                _gender.setText(R.string.female);
            }

            try {
                Bitmap image = db.getImageCache("profile_pic");
                if (image != null) {
                    overlapImage.setImageBitmap(image);
                } else {
                    String imageUrl = MApplication.getString(myProfileFragment, Constants.PROFILE_IMAGE);
                    Utils.loadImageWithGlideSingleImageRounderCorner(myProfileFragment, imageUrl, overlapImage, R.drawable.img_ic_user);
                }
            }
            catch (Exception ae)
            {
                ae.printStackTrace();
            }


            //connectionsCount.setText(friendCount);
            //If status is not empty decode the string in text view
            if (!status.isEmpty()) {
                // statusDetails.setText(MApplication.getDecodedString(status));
            } else {
                //Setting the default text
                // statusDetails.setText(getResources().getString(R.string.status_empty));
            }

            //Progressive bar stops

        }
    }

    @Override
    public void onResume() {
        super.onResume();
       // Toast.makeText(myProfileFragment,  MApplication.getString(myProfileFragment, Constants.PHONE_NUMBER), Toast.LENGTH_SHORT).show();
        webRequest();
    }
}
