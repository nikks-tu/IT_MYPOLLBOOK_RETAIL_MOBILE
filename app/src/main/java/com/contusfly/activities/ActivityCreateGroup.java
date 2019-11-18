/**
 * @category ContusMessanger
 * @package com.contusfly.activities
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.activities;

import android.content.Intent;
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

import com.contus.chatlib.utils.ContusConstantValues;
import com.contusfly.MApplication;
import com.contusfly.adapters.AdapterContacts1;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.service.ChatService;
import com.contusfly.utils.Constants;
import com.contusfly.utils.ItemClickSupport;
import com.contusfly.views.CustomRecyclerView;
import com.contusfly.views.CustomToast;
import com.contusfly.views.DoProgressDialog;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ActivityCreateGroup.
 */
public class ActivityCreateGroup extends ActivityBase implements
        View.OnClickListener, AdapterContacts1.OnContactDeleteListener {

    private static String result;
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
    private List<Rosters> rosterList, filteredData;


    /**
     * The adapter contacts.
     */
    private AdapterContacts1 adapterContacts;

    /**
     * The group id.
     */
    private String groupName, groupImage, toUsers = "", groupId;

    /**
     * The progress dialog.
     */
    private DoProgressDialog progressDialog;
    public View checkBox;


    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_selection);
    }

    /**
     * On post create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mApplication = (MApplication) getApplication();
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_complete_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        TextView txtNext = (TextView) findViewById(R.id.txt_create);
        txtNext.setText(getString(R.string.text_create));
        TextView txtTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        txtTitle.setText("Contacts");
        txtNext.setOnClickListener(this);
        setControls();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * Sets the controls.
     */
    private void setControls() {
        try {
            autoCompleteTextView.setThreshold(1);
            listMembers = (CustomRecyclerView) findViewById(R.id.list_selected_mem);
            listMembers.setLayoutManager(new LinearLayoutManager(this));
            findViewById(R.id.img_plus).setOnClickListener(this);
            rosterList = MDatabaseHelper.getOnlineContacts();
            for (int i = 0; i < rosterList.size(); i++) {
                if (rosterList.get(i).getRosterName().isEmpty()) {
                    rosterList.remove(i);
                }
            }
            adapterContacts = new AdapterContacts1(this);
            filteredData = new ArrayList<>();
            adapterContacts.setData(rosterList);
            adapterContacts.setOnDeleteClickListener(this);
            adapterContacts.setIsMultiSelect(true);
            listMembers.setAdapter(adapterContacts);
            listMembers.setEmptyView(findViewById(android.R.id.empty));
            ItemClickSupport.addTo(listMembers).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    RadioButton checkBox = (RadioButton) v.findViewById(R.id.check_selection);
                    if (position != -1) {
                        Rosters mContacts = adapterContacts.getItem(position);
                        if (checkBox.isChecked()) {
                            checkBox.setChecked(false);
                            if (filteredData.contains(mContacts) && checkIfAdded(mContacts.getRosterID())) {
                                mContacts.setIsSelected(Constants.COUNT_ZERO);
                                changeState(mContacts.getRosterID(), false);
                                int answeredPosition = filteredData.indexOf(mContacts);
                                filteredData.remove(answeredPosition);
                                adapterContacts.notifyDataSetChanged();
                            }
                        } else {
                            checkBox.setChecked(true);
                            if (!filteredData.contains(mContacts)
                                    && !checkIfAdded(mContacts.getRosterID())) {
                                mContacts.setIsSelected(Constants.COUNT_ONE);
                                filteredData.add(mContacts);
                                changeState(mContacts.getRosterID(), true);
                            }
                        }
                    }
                }
            });

            Intent data = getIntent();
            if (data != null) {
                groupName = data.getStringExtra(Constants.USER_PROFILE_NAME);
                groupImage = data.getStringExtra(Constants.USER_IMAGE);
            }
            progressDialog = new DoProgressDialog(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if added.
     *
     * @param id the id
     *
     * @return true, if successful
     */
    private boolean checkIfAdded(String id) {
        for (Rosters item : filteredData) {
            if (item.getRosterID().equalsIgnoreCase(id))
                return true;
        }
        return false;
    }

    /**
     * Change state.
     *
     * @param id    the id
     * @param state the state
     */
    private void changeState(String id, boolean state) {
        for (int i = 0, size = rosterList.size(); i < size; i++) {
            Rosters item = rosterList.get(i);
            if (id.equalsIgnoreCase(item.getRosterID())) {
                if (state)
                    item.setIsSelected(1);
                else
                    item.setIsSelected(0);
                rosterList.set(i, item);
            }
        }
    }

    /**
     * Group created.
     *
     * @param groupId the group id
     */
    @Override
    public void groupCreated(String groupId) {
        Log.e("groupadded", groupId + "");
        if (groupId != null && groupId.length() > 0) {
            Log.e("Test", "test");
            this.groupId = groupId;
            toUsers = toUsers.substring(0, toUsers.length() - 1).trim();
            Log.e("Test", toUsers + "");
            startService(new Intent(this, ChatService.class)
                    .putExtra("groupId", groupId)
                    .putExtra("groupMember", toUsers)
                    .setAction(
                            ContusConstantValues.CONTUS_XMPP_ADD_GROUP_MEMBER));
        } else {
            progressDialog.dismiss();
            CustomToast.showToast(this, getString(R.string.text_error_occured));
        }
    }

    @Override
    protected void groupMemberAdded(String result, String type) {
        startService(new Intent(this, ChatService.class)
                .putExtra("groupId", this.groupId)
                .setAction(ContusConstantValues.CONTUS_XMPP_GET_GROUP_INFO));
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
     * New group created.
     */
    @Override
    protected void newGroupCreated() {
        super.newGroupCreated();
        progressDialog.dismiss();
        setResult(RESULT_OK);
        finish();
    }

    /**
     * On create options menu.
     *
     * @param menu the menu
     *
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
                validateAndCreate();
                break;
            /*case R.id.imageView3:
                finish();
                break;*/
            default:
                break;
        }
    }

    /**
     * Validate and create.
     */
    private void validateAndCreate() {
        Log.e("filter", filteredData.size() + "");
        if (filteredData.size() < 2) {
            CustomToast.showToast(this,
                    getString(R.string.txt_add_two_contacts));
        } else if (!mApplication.isNetConnected(this))
            CustomToast.showToast(this, getString(R.string.text_no_internet));
        else {
            progressDialog.showProgress(getString(R.string.text_loading));
            progressDialog.show();
            for (int i = 0, size = filteredData.size(); i < size; i++) {
                toUsers += filteredData.get(i).getRosterID().concat(",");
            }
            startService(new Intent(this, ChatService.class)
                    .putExtra("groupName", groupName)
                    .putExtra("groupimage", groupImage)
                    .setAction(ContusConstantValues.CONTUS_XMPP_CREATE_GROUP));
        }
    }

    /**
     * On delete clicked.
     *
     * @param position the position
     */
    @Override
    public void onDeleteClicked(int position) {
        changeState(filteredData.get(position).getRosterID(), false);
        filteredData.remove(position);
        adapterContacts.notifyDataSetChanged();
    }


}
