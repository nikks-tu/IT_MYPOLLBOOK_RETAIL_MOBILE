package com.new_chanages.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.contus.activity.CustomRequest;
import com.contus.restclient.UserPollRestClient;
import com.new_chanages.adapters.MyrewardsRecyclerViewAdapter;
import com.new_chanages.models.GetErnigsInputModel;
import com.new_chanages.models.ProductsModel;
import com.polls.polls.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RewardFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    RecyclerView list;
    ArrayList<ProductsModel> productsList;
    int NextPageNumber, PageNumber, RowsPerPage, TotalCount;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;
    MyrewardsRecyclerViewAdapter linearListAdapter;
    int pageno=1;
    private boolean loading = true;
    SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RewardFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RewardFragment newInstance(int columnCount) {
        RewardFragment fragment = new RewardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rewards_list, container, false);

        mSwipeRefreshLayout =view.findViewById(R.id.mSwipeRefreshLayout);

        list = view.findViewById(R.id.list) ;

        // Set the adapter
            Context context = view.getContext();

        getList(false);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList(false);
            }
        });


        list.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            //progressBar.setVisibility(View.VISIBLE);

                            //mAuthTask = new UserLoginTask("", "");
                            // mAuthTask.execute((Void) null);
                            pageno= pageno+1;
                           // getList(true);
                        }
                    }
                }
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==500&&resultCode==Activity.RESULT_OK)
        {
            getList(false);
        }
    }

    private void getList(final boolean more) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("action","getredeemproductsapi");

            Log.v("...", obj.toString());
        }
        catch (Exception ae)
        {

        }
        


        UserPollRestClient.getInstance().getEarnigs(new GetErnigsInputModel("getredeemproductsapi","0"), new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response1) {

                mSwipeRefreshLayout.setRefreshing(false);

                String result =CustomRequest.ResponcetoString(response1);
                try {
                    JSONObject response = new JSONObject(result);

                    int success=response.optInt("success");

                    {
                        try {


                            if(success==1)
                            {
                                JSONObject results =response.optJSONObject("results");
                                String current_page =results.optString("current_page");
                                String from =results.optString("from");
                                String last_page =results.optString("last_page");
                                String per_page =results.optString("per_page");
                                String to =results.optString("to");
                                String total =results.optString("total");
                                JSONArray data =results.optJSONArray("data");
                                //Nikita
                                if(!more) {
                                    productsList = new ArrayList<>();
                                }
                                //productsList = new ArrayList<>();
                                for(int i=0;i<data.length();i++)
                                {
                                    JSONObject obj =data.getJSONObject(i);
                                    String expiry_date= obj.optString("expiry_date");
                                    String image= obj.optString("image");
                                    String money= obj.optString("money");
                                    String points= obj.optString("points");
                                    String product_description= obj.optString("product_description");
                                    String product_id= obj.optString("product_id");
                                    String product_title= obj.optString("product_title");
                                    String quantity= obj.optString("quantity");

                                    ProductsModel obj1 = new ProductsModel();
                                    obj1.setExpiry_date(expiry_date);
                                    obj1.setImage(image);
                                    obj1.setMoney(money);
                                    obj1.setPoints(points);
                                    obj1.setProduct_description(product_description);
                                    obj1.setProduct_id(product_id);
                                    obj1.setProduct_title(product_title);
                                    obj1.setQuantity(quantity);


                                    productsList.add(obj1);
                                }


                                if(!more) {
                                    mLayoutManager = new LinearLayoutManager(getContext());
                                    list.setLayoutManager(mLayoutManager);
                                    linearListAdapter = new MyrewardsRecyclerViewAdapter(getActivity(),productsList);
                                    list.setAdapter(linearListAdapter);
                                }
                                else
                                {
                                    linearListAdapter.notifyDataSetChanged();
                                }

                                if (more) {
                                    if (list.getAdapter() != null && list.getAdapter().getItemCount() < TotalCount) {
                                        loading = true;
                                    } else {
                                        loading = false;
                                    }
                                }
                            }
                            else
                            {
                                //Commented By Nikita
                                //    Toast.makeText(getContext(),response.getString("msg"),Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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


    }

}
