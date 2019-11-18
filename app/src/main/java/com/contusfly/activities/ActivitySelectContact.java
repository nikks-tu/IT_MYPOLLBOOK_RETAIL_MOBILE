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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.contusfly.adapters.AdapterContacts;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.utils.Constants;
import com.contusfly.utils.Utils;
import com.polls.polls.R;

import java.util.List;

/**
 * The Class ActivitySelectContact.
 */
public class ActivitySelectContact extends AppCompatActivity {

    /** The adapter contacts. */
    private AdapterContacts adapterContacts;

    /**
     * On create.
     *
     * @param savedInstanceState
     *            the saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
    }

    /**
     * On post create.
     *
     * @param savedInstanceState
     *            the saved instance state
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utils.setUpToolBar(this, toolbar, getSupportActionBar(),
                getString(R.string.text_select_contact));
        ListView listContacts = (ListView) findViewById(R.id.list_contacts);
        TextView emptyView = (TextView) findViewById(R.id.empty_view);
        emptyView.setText(getString(R.string.txt_no_results));
        listContacts.setEmptyView(emptyView);
        TextView txtGroupTitle = (TextView) findViewById(R.id.toolbar_title);
        txtGroupTitle.setText("Contacts");
        adapterContacts = new AdapterContacts(this);
        final List<Rosters> rostersList = MDatabaseHelper.getRosterInfo(
                Constants.EMPTY_STRING, String.valueOf(Constants.COUNT_ZERO));

        for(int i=0;i<rostersList.size();i++) {
            if(rostersList.get(i).getRosterName().isEmpty()) {
                rostersList.remove(i);
            }
        }
        adapterContacts.setData(rostersList);
      listContacts.setAdapter(adapterContacts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (rostersList.get(position).getRosterAvailability()
                        .equalsIgnoreCase(String.valueOf(Constants.ROSTER_NOT_ACTIVE)))
                    Utils.sendEmail(ActivitySelectContact.this,
                            getString(R.string.text_contus_msg_invite),
                            getString(R.string.text_email_subject),
                            new String[] { rostersList.get(position)
                                    .getRosterID() + "@contus.in" });
                else {
                    startActivity(new Intent( ActivitySelectContact.this,ActivityChatView.class)
                            .putExtra(Constants.ROSTER_USER_ID, rostersList.get(position)
                                    .getRosterID())
                            .putExtra( Constants.CHAT_TYPE,String.valueOf(Constants.CHAT_TYPE_SINGLE)));
                    finish();
                }
            }
        });
    }

    /**
     * On create options menu.
     *
     * @param menu
     *            the menu
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
}
