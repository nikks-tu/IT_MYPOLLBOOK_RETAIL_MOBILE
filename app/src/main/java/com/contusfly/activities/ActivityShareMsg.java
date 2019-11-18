package com.contusfly.activities;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.contusfly.fragments.FragmentSelectContact;
import com.contusfly.fragments.FragmentShareToRecent;
import com.contusfly.model.ChatMsg;
import com.contusfly.utils.Constants;
import com.contusfly.utils.Utils;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.polls.polls.R;

/**
 * Created by user on 11/13/2015.
 */
public class ActivityShareMsg extends AppCompatActivity {

    private FragmentShareToRecent fragmentRecentChats;

    private FragmentSelectContact fragmentContacts;

    private ViewPager mViewPager;

    private ChatMsg mChatMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        Utils.setUpToolBar(this, toolbar, actionBar, getString(R.string.text_select_contact));
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        AdapterView mAdapter = new AdapterView(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_chats1);
        tabs.getTabAt(1).setIcon(R.drawable.ic_contact_selected);
        mChatMsg=getIntent().getParcelableExtra(Constants.CHAT_MESSAGE);
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
        MenuItem menuItem = menu.findItem(R.id.action_search);
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
                        filterData(s);
                        return false;
                    }
                });
        MenuItemCompat.setOnActionExpandListener(menuItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        return true;
                    }
                });
        return true;
    }


    /**
     * The Class AdapterDashboardView.
     */
    private class AdapterView extends CacheFragmentStatePagerAdapter {


        /**
         * Instantiates a new adapter dashboard view.
         *
         * @param fm the fm
         */
        public AdapterView(FragmentManager fm) {
            super(fm);
        }

        /**
         * Creates the item.
         *
         * @param position the position
         * @return the fragment
         */
        @Override
        protected Fragment createItem(int position) {
            if (position == Constants.COUNT_ZERO) {
                fragmentRecentChats = FragmentShareToRecent.getInstance(mChatMsg);
                return fragmentRecentChats;
            } else {
                fragmentContacts = FragmentSelectContact.getInstance(mChatMsg);
                return fragmentContacts;
            }
        }

        /**
         * Gets the count.
         *
         * @return the count
         */
        @Override
        public int getCount() {
            return Constants.COUNT_TWO;
        }

        /**
         * Gets the page title.
         *
         * @param position the position
         * @return the page title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    /**
     * Filter data.
     *
     * @param key the key
     */
    private void filterData(String key) {
        String searchKey = key.toLowerCase();
        if (mViewPager.getCurrentItem() == Constants.COUNT_ZERO) {
            if (fragmentRecentChats != null)
                fragmentRecentChats.filterList(searchKey);
        } else {
            if (fragmentContacts != null)
                fragmentContacts.filterList(searchKey);
        }
    }
}
