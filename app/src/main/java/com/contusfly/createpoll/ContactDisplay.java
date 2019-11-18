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
import com.contusfly.adapters.AdapterContactsInfo;
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
public class ContactDisplay extends ActivityBase implements
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
    private AdapterContactsInfo adapterContacts;
    /**
     * The group id.
     */
    private String groupName, groupImage, toUsers = "", contactId;
    /**
     * The progress dialog.
     */
    private DoProgressDialog progressDialog;
    private Activity mContactDisplay;
    private ArrayList<String> mArrayList;
    private ArrayList<String> mContactName;
    private ArrayList<String> mCategory;
    private ArrayList<String> mContactTitle;
    private ArrayList<Rosters> check;
    private DatabaseHelper db;
    private TextView emptyView;
    private Menu mMenu;
    private MenuItem menuItem;


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
        mContactDisplay = this;
        db = new DatabaseHelper(mContactDisplay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mApplication = (MApplication) getApplication();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        TextView txtNext = (TextView) findViewById(R.id.txt_create);
        TextView txtGroupTitle = (TextView) findViewById(R.id.toolbar_title);
         emptyView = (TextView) findViewById(R.id.include);

        txtGroupTitle.setText("Contacts");
        txtNext.setText(getString(R.string.text_create));
        mCategory = new ArrayList<>();
        mCategory.add("Contacts");
        mContactTitle = MApplication.loadStringArray(mContactDisplay, mCategory, com.contus.app.Constants.ACTIVITY_CATEGORY_INFO, com.contus.app.Constants.ACTIVITY_CATEGORY_INFO_SIZE);
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
            rosterList = MDatabaseHelper.getOnlineContacts();
            adapterContacts = new AdapterContactsInfo(this);
            if(!rosterList.isEmpty()){

                for(int i=0;i<rosterList.size();i++) {
                    if(rosterList.get(i).getRosterName().isEmpty()) {
                        rosterList.remove(i);
                    }
                }
                if(rosterList.size()!=0) {
                    adapterContacts.setData(rosterList);
                    listMembers.setAdapter(adapterContacts);
                    adapterContacts.setIsMultiSelect(true);
                }else{
                    emptyView.setText(getString(R.string.txt_no_results));
                }
            }else{
                emptyView.setText(getString(R.string.txt_no_results));
            }
            autoCompleteData = MDatabaseHelper.getOnlineContacts();
            mArrayList = new ArrayList<>();
            mContactName = new ArrayList<>();

            mContactName = mApplication.loadStringArray(mContactDisplay, mContactName, com.contus.app.Constants.ACTIVITY_CONTACT_NAME_INFO, com.contus.app.Constants.ACTIIVTY_CONTACT_NAME_INFO_SIZE);
            mArrayList = mApplication.loadStringArray(mContactDisplay, mArrayList, com.contus.app.Constants.ACTIVITY_CONTACT_INFO, com.contus.app.Constants.ACTIIVTY_CONTACT_INFO_SIZE);
            listMembers.setEmptyView(findViewById(android.R.id.empty));
            ItemClickSupport.addTo(listMembers).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    mArrayList.clear();
                    mContactName.clear();
                    RadioButton checkBox = (RadioButton) v.findViewById(R.id.check_selection);
                    Rosters mContacts = adapterContacts.getItem(position);
                    mArrayList = mApplication.loadStringArray(mContactDisplay, mArrayList, com.contus.app.Constants.ACTIVITY_CONTACT_INFO, com.contus.app.Constants.ACTIIVTY_CONTACT_INFO_SIZE);
                    if (checkBox.isChecked()) {
                        db.update(mContacts.getRosterID(),mContacts.getRosterName(), Constants.CONTACT,0,1);
                        checkBox.setChecked(false);
                    } else {
                        Log.e("Lis31",db.getAllContacts()+"");
                            if (!db.checkEvent(mContacts.getRosterID())) {
                                Log.e("mContacts.getRosterID()", mContacts.getRosterID() + "");
                                db.addContact(mContacts.getRosterID(), mContacts.getRosterName(), Constants.CONTACT, 0, 0);
                                checkBox.setChecked(true);
                                Log.e("List1", db.getAllContacts() + "");
                            }else{
                                db.update(mContacts.getRosterID(),mContacts.getRosterName(), Constants.CONTACT,0,0);
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
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_search, menu);
        menuItem = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(menu
                .findItem(R.id.action_search));
        mSearchView.setQueryHint("Search Here");
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
        //Set an MenuItemCompat.OnActionExpandListener on this menu item to be notified
        // when the associated action view is expanded or collapsed.
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if(!db.getAllRemoveContactsDetails(0,0).isEmpty()){
                db.deleteTable();
            }
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
                if(!db.getAllRemoveContactsDetails(0,1).isEmpty()){
                   List<ChooseContactsModelClass> unCheckedId=db.getAllRemoveContactsDetails(0, 1);
                    for(int i=0;i<unCheckedId.size();i++) {
                      db.deleteContact(unCheckedId.get(i).getId());
                    }
                }
                List<ChooseContactsModelClass> checkedIds=db.getAllRemoveContactsDetails(0, 0);
                if(!checkedIds.isEmpty()) {
                    for (int i = 0; i < checkedIds.size(); i++) {
                        db.update(checkedIds.get(i).getId(),checkedIds.get(i).getName(), Constants.CONTACT, 1, 1);
                    }
                }
                if(db.getAllContactsDetails(1).isEmpty()&&!rosterList.isEmpty()){
                    Toast.makeText(ContactDisplay.this,"You have not selected any contacts",
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
        Log.e("testcheck",db.getAllRemoveContactsDetails(0,0)+"");
        if(!db.getAllRemoveContactsDetails(0,0).isEmpty()){
            db.deleteTable();
        }
        finish();
    }
}
