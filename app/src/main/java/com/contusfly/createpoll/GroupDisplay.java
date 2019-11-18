package com.contusfly.createpoll;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.contusfly.MApplication;
import com.contusfly.activities.ActivityBase;
import com.contusfly.adapters.AdapterGroupInfo;
import com.contusfly.chooseContactsDb.ChooseContactsModelClass;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.utils.Constants;
import com.contusfly.utils.ItemClickSupport;
import com.contusfly.views.CustomRecyclerView;
import com.contusfly.views.DoProgressDialog;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/9/2016.
 */
public class GroupDisplay extends ActivityBase implements
        View.OnClickListener {
    /**
     * The m application.
     */
    private MApplication mApplication;
    /**
     * The auto complete text view.
     */
    private AutoCompleteTextView autoCompleteTextView;
    /**
     * The list members.
     */
    private CustomRecyclerView listMembers;
    /**
     * The filtered data.
     */
    private List<Rosters> rosterList, autoCompleteData, filteredData;
    /**
     * The adapter contacts.
     */
    private AdapterGroupInfo adapterContacts;
    /**
     * The group id.
     */
    private String groupName, groupImage, toUsers = "", contactId;
    /**
     * The progress dialog.
     */
    private DoProgressDialog progressDialog;
    private Activity groupInfoActivity;
    private ArrayList<String> mArrayList;
    private ArrayList<String> mContactName;
    private ArrayList<String> mCategory;
    private ArrayList<String> mContactTitle;
    private ArrayList<Rosters> check;
    private DatabaseHelper db;
    private TextView emptyView;


    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_slection1);
    }

    /**
     * On post create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        groupInfoActivity = this;
        db=new DatabaseHelper(groupInfoActivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mApplication = (MApplication) getApplication();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        TextView txtNext = (TextView) findViewById(R.id.txt_create);
        TextView txtGroupTitle = (TextView) findViewById(R.id.toolbar_title);
        emptyView = (TextView) findViewById(R.id.include);
        txtGroupTitle.setText("Groups");
        txtNext.setText(getString(R.string.text_create));
        mCategory = new ArrayList<>();
        mCategory.add("Groups");
        mContactTitle = MApplication.loadStringArray(groupInfoActivity, mCategory, com.contus.app.Constants.ACTIVITY_CATEGORY_INFO, com.contus.app.Constants.ACTIVITY_CATEGORY_INFO_SIZE);
        filteredData = new ArrayList<>();
        check = new ArrayList<>();
        txtNext.setOnClickListener(this);
        setControls();
    }

    /**
     * Sets the controls.
     */
    private void setControls() {
        try {
            listMembers = (CustomRecyclerView) findViewById(R.id.list_selected_mem);
            listMembers.setLayoutManager(new LinearLayoutManager(this));
            findViewById(R.id.img_plus).setOnClickListener(this);
            rosterList = MDatabaseHelper.getRosterInfo("",String.valueOf(Constants.COUNT_ONE));
            autoCompleteData = MDatabaseHelper.getOnlineContacts();
            mArrayList = new ArrayList<>();
            mContactName = new ArrayList<>();
            adapterContacts = new AdapterGroupInfo(this);
            if(!rosterList.isEmpty()){

                adapterContacts.setData(rosterList);
                listMembers.setAdapter(adapterContacts);
                adapterContacts.setIsMultiSelect(true);
            }else{
                emptyView.setText(getString(R.string.txt_no_groups_results));
            }
            listMembers.setEmptyView(findViewById(android.R.id.empty));
            ItemClickSupport.addTo(listMembers).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    mArrayList.clear();
                    mContactName.clear();
                    RadioButton checkBox = (RadioButton) v.findViewById(R.id.check_selection);
                    Rosters mContacts = adapterContacts.getItem(position);
                    mArrayList = mApplication.loadStringArray(groupInfoActivity, mArrayList, com.contus.app.Constants.ACTIVITY_GROUP_INFO,com.contus.app.Constants.ACTIIVTY_GROUP_INFO_SIZE);

                    if (checkBox.isChecked()) {
                        Log.e("mContacts.getRosterID()",mContacts.getRosterID()+"");
                        db.updateGroup(mContacts.getRosterID(),mContacts.getRosterName(), Constants.GROUP,0,1);
                        checkBox.setChecked(false);
                    } else {
                        Log.e("Lis31",db.getAllContacts()+"");
                        if(!db.checkEventGroup(mContacts.getRosterID())) {
                            Log.e("mContacts.getRosterID()",mContacts.getRosterID()+"");
                            db.addGroup(mContacts.getRosterID(), mContacts.getRosterName(), Constants.GROUP,0,0);
                            checkBox.setChecked(true);
                            Log.e("List1",db.getAllGroup()+"");
                        }else{
                            db.updateGroup(mContacts.getRosterID(),mContacts.getRosterName(), Constants.GROUP,0,0);
                            checkBox.setChecked(true);
                        }
                    }
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    /**
     * On create options menu.
     *
     * @param menu the menu
     * @return true, if successful
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(menu
                .findItem(R.id.action_search));
        mSearchView
                .setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String s) {
                        adapterContacts.filter(s);
                        adapterContacts.notifyDataSetChanged();
                        return false;
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * On click.
     *
     * @param v the v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_plus:
                break;
            case R.id.txt_create:
                if (!mArrayList.isEmpty()) {
                    if (!mContactTitle.contains("Groups")) {
                        mCategory.add("Groups");
                    }
                } else {
                    mCategory.remove("Groups");
                }

                if(!db.getAllRemoveGroupDetails(0,1).isEmpty()){
                    Log.e("size",db.getAllRemoveGroupDetails(0,1).size()+"");
                    List<ChooseContactsModelClass> unCheckedId=db.getAllRemoveGroupDetails(0, 1);

                    for(int i=0;i<unCheckedId.size();i++) {
                        db.deleteGroup(unCheckedId.get(i).getId());
                    }
                }
                List<ChooseContactsModelClass> checkedIds=db.getAllRemoveGroupDetails(0, 0);
                if(!checkedIds.isEmpty()) {
                    for (int i = 0; i < checkedIds.size(); i++) {
                        db.updateGroup(checkedIds.get(i).getId(),checkedIds.get(i).getName(), Constants.GROUP, 1, 1);
                    }
                    mApplication.saveStringArray(groupInfoActivity, mCategory, com.contus.app.Constants.ACTIVITY_CATEGORY_INFO, com.contus.app.Constants.ACTIVITY_CATEGORY_INFO_SIZE);
                }

                if(db.getAllGroupDetails(1).isEmpty()&&!rosterList.isEmpty()){
                    Toast.makeText(GroupDisplay.this,"You have not selected any groups",
                            Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(db.getAllGroupDetails(1).isEmpty()){
            db.deleteGroupTable();
        }
    }
}
