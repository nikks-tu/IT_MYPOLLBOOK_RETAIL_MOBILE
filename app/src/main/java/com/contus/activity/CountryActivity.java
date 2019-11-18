/**
 * CountryActivity.java
 * <p/>
 * Getting the country and code from response and loading into the iist view
 *
 * @category Contus
 * @package com.contus.activity
 * @version 1.0
 * @author Contus Team <developers@contus.in>
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */

package com.contus.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.responsemodel.CountryResponseModel;
import com.contus.restclient.CountryRestClient;
import com.contus.restclient.SearchCountryRestClient;
import com.contusfly.MApplication;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.polls.polls.R;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 8/3/2015.
 */
public class CountryActivity extends Activity {
    /**Country adapter**/
    private CountryAdapter adapter;
    /*8Activity**/
    private CountryActivity mCountryActivity;
    /**List position**/
    private String listPosition;
    /**Phone number code**/
    private String phonenumberCode;
    /**List view**/
    private ListView list;
    /**Edit text**/
    private EditText editSearch;
    /**Smooth progress bar**/
    private SmoothProgressBar googleNow;
    /**Array list**/
    private List<CountryResponseModel.UserDetails> arrayList;
    /**Textview**/
    private TextView noSearchResults;
    /**String country id**/
    private String countryId;
    /**String country countrycode**/
    private String countryCode;
    /**String country searchkey**/
    private String searchKey;
    //Relative list
    private RelativeLayout relativeList;
    //Internet connection
    private RelativeLayout internetConnection;
    //Floating ACTION button
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_countrylist_dialog);
        /*View are creating when the activity is started**/
        init();
    }

    /**
     * Instantiate the method
     */
    private void init() {
        /*Initializing the activity**/
        mCountryActivity = this;
        /*Initializing the list view**/
        list =  findViewById(R.id.component_list);
        editSearch =  findViewById(R.id.editSearch);
        googleNow =  findViewById(R.id.google_now);
        noSearchResults =  findViewById(R.id.noSearchResults);
        internetConnection =  findViewById(R.id.internetConnection);
        relativeList =  findViewById(R.id.list);
        fab =  findViewById(R.id.fab);
        //For SAMSUNG s3 the design parameters variees to fix the design issues.
        String model = android.os.Build.MODEL;
        if (Constants.SAMSUNG_S3.equals(model)) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 20, 10, 20);
            list.setLayoutParams(params);
            params.setMargins(20, 20, 10, 20);
            editSearch.setLayoutParams(params);
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        editSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {    /*If search icon is clicked in the keypad the condition goes inside**/
                    searchKey = editSearch.getText().toString().trim();   /*Is search key value is not empty**/
                    if (!searchKey.isEmpty()) {
                        searchcountrydetailsload();   /*Request and response method is called**/
                    } else {
                        MApplication.hideKeyboard(mCountryActivity);       /*Hiding the keyborad**/
                        Toast.makeText(CountryActivity.this, getResources().getString(R.string.enter_valid_country_name), Toast.LENGTH_SHORT).show();   /*Toast message wthen search key is wrong**/
                        editSearch.requestFocus(); /*Requesting focus**/
                    }
                    return true;
                }
                return false;
            }

        });
        //Interface definition for a callback to be invoked when a view is clicked.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKey = editSearch.getText().toString().trim();//value from the edittext is stored in the string
                if (!searchKey.isEmpty()) {/*Is search key value is not empty**/
                    searchcountrydetailsload();  /*Request and response method is called**/
                } else {
                    MApplication.hideKeyboard(mCountryActivity); /*Hiding the keyborad**/
                    Toast.makeText(CountryActivity.this, getResources().getString(R.string.enter_valid_country_name), Toast.LENGTH_SHORT).show();  /*Toast message wthen search key is wrong**/
                    editSearch.requestFocus();/*Requesting focus**/
                }
            }
        });
    }

    /**
     * This method is called when the user want search the country list
     */
    @SuppressLint("RestrictedApi")
    private void searchcountrydetailsload() {
        /*If internet connection is available**/
        if (MApplication.isNetConnected(mCountryActivity)) {
            //View gone
            internetConnection.setVisibility(View.GONE);
            //View visible
            relativeList.setVisibility(View.VISIBLE);
            //Fab visible
            fab.setVisibility(View.VISIBLE);
            /*Progree bar visiblity**/
            googleNow.setVisibility(View.VISIBLE);
            /*Progress bar start**/
            googleNow.progressiveStart();
            /*Request and response in retrofit**/
            SearchCountryRestClient.getInstance().postLoginData("countrySearchApi", searchKey
                    , new Callback<CountryResponseModel>() {
                @Override
                public void success(CountryResponseModel searchCountryResponseModel, Response response) {
                    /*Hides the keyborad**/
                    MApplication.hideKeyboard(mCountryActivity);
                    /*on success response this method is clled to get the value from the model class**/
                    responseCountryList(searchCountryResponseModel);
                }
                @Override
                public void failure(RetrofitError retrofitError) {
                    //Error message popups when the user cannot able to coonect with the server
                    MApplication.errorMessage(retrofitError, mCountryActivity);
                }
            });
        } else {
            //This layout will be visible when the internet connection is not available
            internetConnection.setVisibility(View.VISIBLE);
            //View invlisible
            relativeList.setVisibility(View.GONE);
            //View invisible
            fab.setVisibility(View.GONE);
        }
        /*Progress stop**/
        googleNow.progressiveStop();
        /*progress visiblity gone**/
        googleNow.setVisibility(View.GONE);
    }

    /**
     * This method is called when the user want load country list in listview from request
     */
    @SuppressLint("RestrictedApi")
    private void countryDetailsLoad() {
        if (MApplication.isNetConnected(mCountryActivity)) {
            //View invisible
            internetConnection.setVisibility(View.GONE);
            //View visible
            relativeList.setVisibility(View.VISIBLE);
            //View visible
            fab.setVisibility(View.VISIBLE);
            /*Progres bar visibility**/
            googleNow.setVisibility(View.VISIBLE);
            /*Progress bar start**/
            googleNow.progressiveStart();
            CountryRestClient.getInstance().postCountryData("countryapi"
                    , new Callback<CountryResponseModel>() {
                @Override
                public void success(CountryResponseModel countryResponseModel, Response response) {
                /*on success response this method is clled to get the value from response**/
                    responseCountryList(countryResponseModel);
                }
                @Override
                public void failure(RetrofitError retrofitError) {
                    //Error message popups when the user cannot able to coonect with the server
                    MApplication.errorMessage(retrofitError, mCountryActivity);
                }
            });
        } else {
            internetConnection.setVisibility(View.VISIBLE);
            relativeList.setVisibility(View.GONE);
        }
        //Progressbar stops
        googleNow.progressiveStop();
        //Progressbar became invisible
        googleNow.setVisibility(View.GONE);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getWindow().getCurrentFocus()).getWindowToken(), 0);
            }
        }
        return ret;
    }
    /**
     * Country list response binding tinto the adapter
     * @param countryResponseModel
     */

    private void responseCountryList(final CountryResponseModel countryResponseModel) {
        /*Value from the response is stored in array list**/
        arrayList = countryResponseModel.getData();
        /*If array list is not empty**/
        if (!arrayList.isEmpty()) {
            /*List visiblity**/
            list.setVisibility(View.VISIBLE);
            /*invisible view **/
            noSearchResults.setVisibility(View.INVISIBLE);
            /*Custom dialog adapter**/
            adapter = new CountryAdapter(this, arrayList, R.layout.custom_countrylist_singleview,countryResponseModel);
             //Setting the adapter
            list.setAdapter(adapter);
            editSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                     Log.e("1", "1");
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.e("s", s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = editSearch.getText().toString().toLowerCase(Locale.getDefault());
                    adapter.filter(text);
                }
            });
        } else {
            //View visible
            noSearchResults.setVisibility(View.VISIBLE);
           // List invisible
            list.setVisibility(View.INVISIBLE);

        }
        /*List on click listner**/
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*List position**/
                listPosition = String.valueOf(i);
                /*Setting  the list position in string**/
                MApplication.setString(mCountryActivity, Constants.DIALOG_POSITION, listPosition);
                /*Setting  the list position in string**/
                MApplication.setString(mCountryActivity, Constants.COUNTRY, countryResponseModel.getData().get(i).getCountryName());
                /* phone number code**/
                phonenumberCode = countryResponseModel.getData().get(i).getCountryPhonecode();
                //Country Id from the response
                countryId = countryResponseModel.getData().get(i).getCountryId();
                //Country code from the response
                countryCode = countryResponseModel.getData().get(i).getCountryCode();
                /*Setting the value in shared prefernce**/
                MApplication.setString(mCountryActivity, Constants.COUNTRY_ID, countryId);
                /*Setting the countryCode in shared prefernce**/
                MApplication.setString(mCountryActivity, Constants.COUNTRY_CODE, countryCode);
                /*Setting the phonenumberCode in shared prefernce**/
                MApplication.setString(mCountryActivity, Constants.PHONE_NUMBER_CODE, phonenumberCode);
                /*notify change**/
                adapter.notifyDataSetChanged();
                /*Finishing the activity**/
                finish();
            }
        });
    }
    /**
     * Calling this method from xml file when performing the click on the ACTION
     * @param clickedView
     */
    public void onClick(final View clickedView) {
        if(clickedView.getId()==R.id.imgBackArrow){
            /*Finish the activity**/
            this.finish();
        }else if(clickedView.getId()==R.id.internetRetry){
            /*Finish the activity**/
            countryDetailsLoad();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Hiding the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        /*Request and response  during resuming the activity**/
        countryDetailsLoad();
    }
}
