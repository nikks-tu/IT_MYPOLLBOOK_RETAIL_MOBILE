package com.contus.category;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.residemenu.MenuActivity;
import com.contus.responsemodel.CategoryPollResponseModel;
import com.contus.restclient.CategoryRestClient;
import com.contusfly.MApplication;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.google.android.gms.ads.AdView;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/17/2015.
 */
public class Category extends Fragment {
    //Parent view
    private View parentView;
    //List
    private ListView list;
    //Data from thresponse is stored as alist
    private List<CategoryPollResponseModel.Results> dataResults;
    //Category Adapter
    private CategoryAdapter categoryAdapter;
    //Internet connection layout
    private RelativeLayout internetConnection;
    //Relative list
    private RelativeLayout relativeList;
    //TextView

    private Button txtDone;
    TextView btnRetry;
    //SmoothProgress bar
    private SmoothProgressBar googleNow;
    //CategoryArray
    private ArrayList<String> categoryArray = new ArrayList<String>();
    //User id
    private String userId;
    //Preference category save array
    ArrayList<String> prefernceCategorySaveArray;
    //category selected id
    String mCatgorySelected;
    //Adview
    private AdView mAdView;
    private Activity categoryFragment;
    private MenuActivity menuActivity;
    Button bDone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_category, container, false);
        //list view
        list =  parentView.findViewById(R.id.listView);
        //Google ad
        mAdView =  parentView.findViewById(R.id.adView);
        //Custom progress bar
        googleNow =  parentView.findViewById(R.id.google_now);
        //internet connection
        internetConnection =  parentView.findViewById(R.id.internetConnection);
        //relative list layout
        relativeList =  parentView.findViewById(R.id.list);
        //internet retry
        btnRetry =  parentView.findViewById(R.id.internetRetry);
        categoryFragment = getActivity();
        //user id from the preference
        userId = MApplication.getString(categoryFragment, Constants.USER_ID);
        //Interface definition for a callback to be invoked when a view is clicked.
        bDone = this.getActivity().findViewById(R.id.txtDone);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request to the server
                categoryPollRequest();
            }
        });

        //returning the views
        return parentView;
    }

    /**
     * This method is used to display the category in list view.
     */
    private void categoryPollRequest() {
        //Checks weather internet connection is available
        if (MApplication.isNetConnected(categoryFragment)) {
            //View visibility gone
            internetConnection.setVisibility(View.GONE);
            //View visible
            relativeList.setVisibility(View.VISIBLE);
            /*Progress bar visibility**/
            googleNow.setVisibility(View.VISIBLE);
            /*Progress bar start**/
            googleNow.progressiveStart();
            /*  Requesting the response from the given base url**/
            CategoryRestClient.getInstance().postCategoryData("categoryapi_list", userId
                    , new Callback<CategoryPollResponseModel>() {
                        @Override
                        public void success(CategoryPollResponseModel categoryPollResponseModel, Response response) {
                            //If the response is success and the value is 1 then the data are binded into the adapter
                            if (("1").equals(categoryPollResponseModel.getSuccess())) {
                                //Response binding method
                                bDone.setVisibility(View.VISIBLE);
                                categoryResponse(categoryPollResponseModel);
                            }
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            //Error message popups when the user cannot able to coonect with the server
                            MApplication.errorMessage(retrofitError, categoryFragment);
                            //Progressive bar stops
                            googleNow.progressiveStop();
                            //Visisblity gone
                            googleNow.setVisibility(View.GONE);
                        }
                    });
        } else {
            //Internet connection Layout visible
            internetConnection.setVisibility(View.VISIBLE);
            //List Layout gone
            relativeList.setVisibility(View.GONE);
        }
    }

    /**
     * This method is used to bind the data into the adapter class.
     *
     * @param categoryPollResponseModel-datas are stored in model class
     */
    private void categoryResponse(CategoryPollResponseModel categoryPollResponseModel) {
        //Getting the details from the response
        dataResults = categoryPollResponseModel.getResults();
        categoryArray.clear();
        //If data results is not empty,checks wether the user has selected any category before
        //If it is selected the category id is save in the prefernce as arraylist
        if (!dataResults.isEmpty()) {
            for (int i = 0; i < dataResults.size(); i++) {
                if (!dataResults.get(i).getUserCategory().isEmpty()) {
                    categoryArray.add(dataResults.get(i).getUserCategory().get(0).getCategoryId());
                    MApplication.saveStringArray(categoryFragment, categoryArray, Constants.CATEGORY_COUNT_ARRAY, Constants.CATEGORY_COUNT_SIZE);
                }
            }
            //Setting the boolean false
            MApplication.setBoolean(categoryFragment, Constants.YES_OR_NO, false);
            //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
            // The Adapter provides access to the data items.
            // The Adapter is also responsible for making a View for each item in the data set.
            categoryAdapter = new CategoryAdapter(categoryFragment, dataResults);
            //Sets the data behind this ListView.
            list.setAdapter(categoryAdapter);
        }
        //Progressive bar stops
        googleNow.progressiveStop();
        //Visibility gone
        googleNow.setVisibility(View.GONE);
    }


    @Override
    public void onResume() {
        super.onResume();
        //Google ad method
        MApplication.googleAd(mAdView);
        //Request for the category display
        categoryPollRequest();
    }

    /**
     * This method is used to save the category by request
     *
     * @param activity-activity
     */
    public void categoryPollSave(final Activity activity) {
        //User id from the pprefernce
        userId = MApplication.getString(activity, Constants.USER_ID);
        menuActivity = (MenuActivity) activity;



            //Loading the array list from the prefernce which contains category ids
            prefernceCategorySaveArray = MApplication
                    .loadStringArray(activity, categoryArray, Constants.CATEGORY_COUNT_ARRAY, Constants.CATEGORY_COUNT_SIZE);

            //If arraylist is not empty,replace ths string by comma
            //Otherwise set it as empty
            if (!prefernceCategorySaveArray.isEmpty()) {
                mCatgorySelected = prefernceCategorySaveArray.toString().replaceAll("[\\s\\[\\]]", "");

                //Checks the internet connection
                if (MApplication.isNetConnected(activity)) {

                    Log.e("category", mCatgorySelected + "");
                    //Material dialog start
                    MApplication.materialdesignDialogStart(activity);
                    /*  Requesting the response from the given base url**/
                    CategoryRestClient.getInstance()
                                      .saveCategoryData("user_category", userId, mCatgorySelected
                                              , new Callback<CategoryPollResponseModel>() {
                                                  @Override
                                                  public void success(CategoryPollResponseModel welcomeResponseModel, Response response) {
                                                      MApplication.setBoolean(activity, "Category click", true);
                                                      //Message from the response is diaplyed as toast
                                                      Toast.makeText(activity, "Categories updated successfully",
                                                              Toast.LENGTH_SHORT).show();
                                                      //Progressive bar stops
                                                      MApplication.materialdesignDialogStop();
                                                      menuActivity.replaceFragment();
                                                  }

                                                  @Override
                                                  public void failure(RetrofitError retrofitError) {
                                                      //Error message popups when the user cannot able to coonect with
                                                      // the server
                                                      MApplication.errorMessage(retrofitError, activity);
                                                      //Progressive bar stops
                                                      MApplication.materialdesignDialogStop();
                                                  }
                                              });
                } else {
                    //Toast message will display if the internet connection is not available
                    Toast.makeText(activity, activity.getResources().getString(R.string.check_internet_connection),
                            Toast.LENGTH_SHORT).show();
                }

            } else {
                mCatgorySelected = "";

                //Toast message will display if the internet connection is not available
                Toast.makeText(activity, "Please Select atleast one category",
                        Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            categoryFragment = (Activity) context;
        }

    }

}

