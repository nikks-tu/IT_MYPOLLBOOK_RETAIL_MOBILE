package com.contusfly.createpoll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contusfly.MApplication;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.polls.polls.R;

import java.util.ArrayList;

/**
 * Created by user on 7/24/2015.
 */
public class CreatePollOptions extends Activity {
    private ListView list;
    private Activity createPollOptions;
    private String[] options;
    private String[] optionsDetails;
    private Integer[] imgOptions;
    private CreatePollOptionsAdapter adapter;
    private ImageView imageUnselectCategory;
    private ImageView imageSelectCategory;
    private ArrayList<String> mCategory;
    private ArrayList<String> mArrayList;
    private ArrayList<String> listGroupid;
    private DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll_options);
        init();
    }

    public void init() {
        list = (ListView) findViewById(R.id.listView);
        createPollOptions = this;
        mCategory = new ArrayList<>();
        mArrayList = new ArrayList<>();
        listGroupid = new ArrayList<>();
        db = new DatabaseHelper(this);
        Log.e("test", MApplication.loadStringArray(createPollOptions, mArrayList, com.contus.app.Constants.ACTIVITY_CONTACT_INFO, Constants.ACTIIVTY_CONTACT_INFO_SIZE) + "");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        imgOptions = new Integer[]{R.drawable.ic_public_gray, R.drawable.ic_group_gray_2, R.drawable.ic_contact_gray_2};
        options = new String[]{getResources().getString(R.string.activity_create_poll_public), getResources().getString(R.string.activity_create_poll_groups), getResources().getString(R.string.activity_create_poll_contacts)};
        optionsDetails = new String[]{getResources().getString(R.string.activity_create_poll_public_details), getResources().getString(R.string.activity_create_poll_groups_details), getResources().getString(R.string.activity_create_poll_contacts_details)};
        adapter = new CreatePollOptionsAdapter(createPollOptions, options, optionsDetails, imgOptions);
        list.setAdapter(adapter);
        mCategory = MApplication.loadStringArray(createPollOptions, mCategory, Constants.ACTIVITY_CATEGORY_INFO, Constants.ACTIVITY_CATEGORY_INFO_SIZE);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                imageUnselectCategory = (ImageView) view.findViewById(R.id.imageUnselectCategory);
                imageSelectCategory = (ImageView) view.findViewById(R.id.imageSelectCategory);
                switch (i) {
                    case 0: {
                        if (imageSelectCategory.getVisibility() == View.VISIBLE) {
                            imageUnselectCategory.setVisibility(View.VISIBLE);
                            imageSelectCategory.setVisibility(View.INVISIBLE);
                            if (mCategory.contains("Public")) {
                                mCategory.remove("Public");
                            }
                        } else if (imageUnselectCategory.getVisibility() == View.VISIBLE) {
                            imageUnselectCategory.setVisibility(View.INVISIBLE);
                            imageSelectCategory.setVisibility(View.VISIBLE);
                            mCategory.add("Public");
                        }
                        MApplication.saveStringArray(createPollOptions, mCategory, Constants.ACTIVITY_CATEGORY_INFO, Constants.ACTIVITY_CATEGORY_INFO_SIZE);
                    }
                    break;
                    case 1: {
                        Intent a = new Intent(CreatePollOptions.this, GroupDisplay.class);
                        startActivity(a);
                    }
                    break;
                    case 2: {
                        Intent a = new Intent(CreatePollOptions.this, ContactDisplay.class);
                        startActivity(a);
                    }
                    break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void onClick(View v) {
     if (v.getId() == R.id.imagBackArrow) {
            if(!db.getAllContactsDetails(1).isEmpty()){
                if(!mCategory.contains("contact")) {
                    mCategory.add("contact");
                }
            }else{
                if(mCategory.contains("contact")) {
                    mCategory.remove("contact");
                }
            }
         if(!db.getAllGroupDetails(1).isEmpty()){
             if(!mCategory.contains("Groups")) {
                 mCategory.add("Groups");
             }
         }else{
             if(mCategory.contains("Groups")) {
                 mCategory.remove("Groups");
             }
         }
            if (mCategory.isEmpty()) {
                Toast.makeText(createPollOptions, createPollOptions.getResources().getString(R.string.select_one_category), Toast.LENGTH_SHORT).show();
            }else{
                MApplication.saveStringArray(createPollOptions, mCategory, Constants.ACTIVITY_CATEGORY_INFO, Constants.ACTIVITY_CATEGORY_INFO_SIZE);
              finish();
            }
        }
    }
    @Override
    public void onBackPressed() {
        mCategory = MApplication.loadStringArray(createPollOptions, mCategory, Constants.ACTIVITY_CATEGORY_INFO, Constants.ACTIVITY_CATEGORY_INFO_SIZE);
        if (mCategory.isEmpty()) {
            Toast.makeText(createPollOptions, createPollOptions.getResources().getString(R.string.select_one_category), Toast.LENGTH_SHORT).show();
        }else{
            MApplication.saveStringArray(createPollOptions, mCategory, Constants.ACTIVITY_CATEGORY_INFO, Constants.ACTIVITY_CATEGORY_INFO_SIZE);
            MApplication.saveStringArray(createPollOptions, mCategory, Constants.ACTIVITY_CATEGORY_COMPLETE_INFO, Constants.ACTIVITY_CATEGORY__COMPLETE_INFO_SIZE);
           super.onBackPressed();
        }
    }
}