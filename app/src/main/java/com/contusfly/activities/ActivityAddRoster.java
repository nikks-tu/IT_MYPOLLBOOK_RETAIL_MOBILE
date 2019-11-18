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
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.contus.chatlib.utils.ContusConstantValues;
import com.contusfly.MApplication;
import com.contusfly.adapters.AdapterContacts;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.service.ChatService;
import com.contusfly.utils.Constants;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.Utils;
import com.contusfly.views.CommonAlertDialog;
import com.contusfly.views.CustomToast;
import com.contusfly.views.DoProgressDialog;
import com.contusfly.views.TimerProgressDialog;
import com.polls.polls.R;

import java.util.List;

/**
 * The Class ActivityAddRoster.
 */
public class ActivityAddRoster extends ActivityBase {

    /**
     * The adapter users.
     */
    private AdapterContacts adapterContactRoster;

    /**
     * The last known position.
     */
    private int lastKnownPosition;

    private Rosters lastKnownRoster;

    /**
     * The m application.
     */
    private MApplication mApplication;

    /**
     * The group users.
     */
    private String groupId, /**
     * The Group users.
     */
    groupUsers;

    /**
     * The progress dialog.
     */
    private DoProgressDialog progressDialog;

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
/*
     * (non-Javadoc)
	 *
	 * @see ActivityBase#onCreate(android.os.Bundle)
	 */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
    }

    /**
     * On post create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            /* Setting ToolBar */
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Utils.setUpToolBar(this, toolbar, getSupportActionBar(), getString(R.string.text_select_contact));

			/* Setting ListView */
            final ListView listContacts = (ListView) findViewById(R.id.list_contacts);
            TextView emptyView = (TextView) findViewById(R.id.empty_view);
            emptyView.setText(getString(R.string.txt_no_results));
            listContacts.setEmptyView(emptyView);

            Intent mIntent = getIntent();

            groupId = mIntent.getStringExtra(Constants.ROSTER_USER_ID);

            /** Setting data in List */
            adapterContactRoster = new AdapterContacts(this);
            groupUsers = mIntent.getStringExtra(Constants.ROSTER_GROUP_USERS);
            final List<Rosters> rostersList = MDatabaseHelper.getRemainingRosters(groupUsers);
            adapterContactRoster.setData(rostersList);
            listContacts.setAdapter(adapterContactRoster);
            getSupportActionBar().setSubtitle(String.valueOf(rostersList.size()) + " " + getString(R.string.text_contacts));

            final CommonAlertDialog mDialog = new CommonAlertDialog(this);

            listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    lastKnownPosition = position;
                    lastKnownRoster = rostersList.get(lastKnownPosition);
                    mDialog.showAlertDialog(Constants.EMPTY_STRING,
                            getString(R.string.txt_are_you_want_add)
                                    + Utils.getDecodedString(rostersList.get(lastKnownPosition).getRosterName()),
                            getString(R.string.text_yes), getString(R.string.text_no),
                            CommonAlertDialog.DIALOG_TYPE.DIALOG_DUAL);
                }
            });
            mDialog.setOnDialogCloseListener(new CommonAlertDialog.CommonDialogClosedListener() {
                @Override
                public void onDialogClosed(CommonAlertDialog.DIALOG_TYPE dialogType, boolean isSuccess) {
                    if (isSuccess) {
                        String rosterId = rostersList.get(lastKnownPosition).getRosterID();
                            addGroupMember(rosterId);
                    }
                }

                @Override
                public void listOptionSelected(int position) {
                    // Code will not be added
                }
            });
            mApplication = (MApplication) getApplication();
        } catch (Exception e) {
            LogMessage.e(e);
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
        SearchView mSearchViewRoster = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        mSearchViewRoster.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterContactRoster.filter(s);
                adapterContactRoster.notifyDataSetChanged();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Adds the group member.
     *
     * @param rosterId the roster id
     */
    private void addGroupMember(String rosterId) {
        if (mApplication.isNetConnected(this)) {
            showDialog();
            Intent mIntent = new Intent(this, ChatService.class);
            mIntent.putExtra("groupId", groupId);
            mIntent.putExtra("groupMember", rosterId);
            mIntent.setAction(ContusConstantValues.CONTUS_XMPP_ADD_GROUP_MEMBER);
            startService(mIntent);
        } else
            CustomToast.showToast(this, getString(R.string.text_no_internet));
    }

    /**
     * Group member added.
     *
     * @param result the result
     */

    @Override
    protected void groupMemberAdded(String result, String type) {
        startService(new Intent(this, ChatService.class).putExtra("groupId", groupId)
                .setAction(ContusConstantValues.CONTUS_XMPP_GET_GROUP_INFO));

        closeDialog();
        Intent intent = new Intent();
        intent.putExtra("add_user", lastKnownRoster);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * New group created.
     */
/*
     * (non-Javadoc)
	 *
	 * @see ActivityBase#newGroupCreated()
	 */
    @Override
    protected void newGroupCreated() {
        closeDialog();
        Intent intent = new Intent();
        intent.putExtra("add_user", lastKnownRoster);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Show dialog.
     */
    private void showDialog() {
        progressDialog = new TimerProgressDialog(this);
        progressDialog.showProgress();
    }

    /**
     * Close dialog.
     */
    private void closeDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
