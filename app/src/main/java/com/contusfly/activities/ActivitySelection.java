package com.contusfly.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
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
 * Created by user on 1/5/2016.
 */
public class ActivitySelection extends AppCompatActivity {

    /**
     * The adapter contacts.
     */
    private AdapterContacts adapterUsers;

    private List<Rosters> rostersList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utils.setUpToolBar(this, toolbar, getSupportActionBar(),
                getString(R.string.text_select_contact));
        ListView listRosters = (ListView) findViewById(R.id.list_contacts);
        TextView emptyView = (TextView) findViewById(R.id.empty_view);
        emptyView.setText(getString(R.string.txt_no_results));
        listRosters.setEmptyView(emptyView);

        Intent intent = getIntent();
        String users = intent.getStringExtra(Constants.USERNAME);
        rostersList = MDatabaseHelper.getRemainingRosters(users);
        adapterUsers = new AdapterContacts(this);
        adapterUsers.setData(rostersList);
        adapterUsers.setIsMultiSelect(true);
        listRosters.setAdapter(adapterUsers);
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
        SearchView searchKey = (SearchView) MenuItemCompat.getActionView(menu
                .findItem(R.id.action_search));
        searchKey
                .setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        adapterUsers.filter(s);
                        adapterUsers.notifyDataSetChanged();
                        return false;
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }
}
