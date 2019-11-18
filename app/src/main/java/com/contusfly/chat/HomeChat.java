package com.contusfly.chat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.SearchView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.contus.chatlib.utils.ContusConstantValues;
import com.contus.residemenu.ResideMenu;
import com.contus.responsemodel.ContactResponseModel;
import com.contus.restclient.ContactsRestClient;
import com.contusfly.MApplication;
import com.contusfly.activities.ActivityBaseFragment;
import com.contusfly.activities.ActivityNewGroup;
import com.contusfly.activities.ActivitySelectContact;
import com.contusfly.fragments.FragmentContacts;
import com.contusfly.fragments.FragmentRecentChats;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.service.ChatService;
import com.contusfly.utils.Constants;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.Utils;
import com.contusfly.views.CustomToast;
import com.contusfly.views.DoProgressDialog;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.polls.polls.R;

import retrofit.Callback;
import retrofit.RetrofitError;

public class HomeChat extends ActivityBaseFragment implements FragmentRecentChats.OnDataChangeListener, Utils.ContactResult {
    //Tablayout
    private TabLayout tabLayout;
    //Adview
    private AdView mAdView;
    //Fragment recent chats
    private FragmentRecentChats fragmentRecentChats;
    //Fragment contacts
    private FragmentContacts fragmentContacts;
    //search key
    private String searchKey = Constants.EMPTY_STRING;
    //Menu
    private Menu mMenu;
    //View pager
    private ViewPager mViewPager;
    //Reside menu
    private ResideMenu resideMenu;
    //TextView
    private TextView txtBadge;
    //Progressdialog
    private DoProgressDialog progressDialog;
    //User id
    private String userId;
    //PollFragment
    private PollsFragment pollFragment;
    //SearcView
    private SearchView mSearchView;
    //Menu item
    private MenuItem menuItem;
    private String deviceCountryCode;
    /**
     * Id to identify a contacts permission request.
     */
    private static final int REQUEST_CONTACTS = 1;

    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};
    private TextView txtChats;
    private TextView txtTitle;
    private TextView mTxtTitle;
    private Resources resources;
    private View viewPolls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.chat, null);
        mAdView = (AdView) root.findViewById(R.id.adView);
        mViewPager = (ViewPager) root.findViewById(R.id.tabanim_viewpager);
        tabLayout = (TabLayout) root.findViewById(R.id.tabanim_tabs);
     /*   Toolbar toolbar = (Toolbar) root.findViewById(R.id.mToolbar);//A Toolbar is a generalization of action bars for use within application layouts.
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Chats");*/
        resideMenu = new ResideMenu(getActivity());
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Activity.TELEPHONY_SERVICE);
        deviceCountryCode = tm.getNetworkCountryIso();
        //adrequest
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        //load the adinto the adview
        mAdView.loadAd(adRequest);
        //Adapter
        MApplication.setBoolean(getContext(), "notification click", false);
        AdapterDashboardView mAdapter = new AdapterDashboardView(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);//Sets the data behind this ListView.
        tabLayout = (TabLayout) root.findViewById(R.id.tabanim_tabs);
        tabLayout.setupWithViewPager(mViewPager);//The one-stop shop for setting up this TabLayout with a ViewPager.
        View viewChats = getActivity().getLayoutInflater().inflate(R.layout.custom_tab_chat, null);
        View viewContacts = getActivity().getLayoutInflater().inflate(R.layout.custom_tab_chat, null);
        viewPolls = getActivity().getLayoutInflater().inflate(R.layout.custom_tab_chat, null);
        txtBadge = (TextView) viewChats.findViewById(R.id.txt_count);
        //Set the action bar into custom navigation mode, supplying a view for custom navigation.
        tabLayout.getTabAt(2).setCustomView(viewPolls);
        //Set the action bar into custom navigation mode, supplying a view for custom navigation.
        tabLayout.getTabAt(1).setCustomView(viewChats);
        //Set the action bar into custom navigation mode, supplying a view for custom navigation.
        tabLayout.getTabAt(0).setCustomView(viewContacts);
        txtChats = (TextView) viewChats.findViewById(R.id.text);
        txtTitle = (TextView) viewContacts.findViewById(R.id.text);
        mTxtTitle = (TextView) viewPolls.findViewById(R.id.text);
        resources = getResources();
        //Setting the text
        txtTitle.setText(getString(R.string.text_polls));
        //Setting the text
        txtChats.setText(getString(R.string.text_chats));
        //Setting the text
        mTxtTitle.setText(getString(R.string.text_contacts));
        //Setting the text color
        mTxtTitle.setTextColor(resources.getColor(R.color.blue_color));
        //Setting the text color
        txtTitle.setTextColor(resources.getColor(R.color.blue_color));
        //Setting the text color
        txtChats.setTextColor(resources.getColor(R.color.blue_color));
        mViewPager.setCurrentItem(1);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1)
                    if (fragmentRecentChats != null)
                        fragmentRecentChats.setChatAdapter();
                if (position == 2)
                    fragmentContacts.refreshContacts();
//                    tabLayout.getTabAt(2).setCustomView(viewPolls);
            }

            @Override
            public void onPageSelected(int position) {
                getActivity().supportInvalidateOptionsMenu();
                //Filtering the data when typed the text in search view
                filterData(searchKey, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        NotificationManagerCompat.from(getActivity()).cancel(Constants.NOTIFICATION_ID);
        MDatabaseHelper.deleteRecord(Constants.TABLE_PENDING_CHATS, null, null);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUnReadCount();
    }

    /**
     * Sets the un read count.
     */
    private void setUnReadCount() {
        //Unread count
        int count = MDatabaseHelper.getUnreadChatsCount();
        if (count > 0) {
            txtBadge.setText(String.valueOf(count));
            txtBadge.setVisibility(View.VISIBLE);
        } else
            txtBadge.setVisibility(View.GONE);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.contusfly.activities.ActivityBase#receivedChatMsg(android.content
     * .Intent)
     */
    @Override
    protected void receivedChatMsg(Intent chatData) {
        //Refreshing the view
        refreshView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUnReadCount();
            }
        }, 700);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.contusfly.activities.ActivityBase#msgSentToServer(android.content
     * .Intent)
     */
    @Override
    protected void msgSentToServer(Intent chatData) {
        refreshView();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.contusfly.activities.ActivityBase#msgRecieptCallBack(android.content
     * .Intent)
     */
    @Override
    protected void msgRecieptCallBack(Intent chatData) {
        refreshView();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.contusfly.activities.ActivityBase#msgSeenCallBack(android.content
     * .Intent)
     */
    @Override
    protected void msgSeenCallBack(Intent chatData) {
        refreshView();
    }


    /**
     * Refresh view.
     */
    private void refreshView() {
        if (fragmentRecentChats != null)
            fragmentRecentChats.setChatAdapter();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.contusfly.activities.ActivityBase#newGroupCreated()
     */
    @Override
    protected void newGroupCreated() {
        super.newGroupCreated();
        //Refresh view
        refreshView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenu = menu;
        inflater.inflate(R.menu.main, menu);
        menuItem = menu.findItem(R.id.action_search);
        //Returns the currently set action view for this menu item.
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        //Sets a listener for user actions within the SearchView.
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterData(s, true);
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
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.e("d", requestCode + "");
        if (requestCode == REQUEST_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                progressDialog = new DoProgressDialog(getActivity());
                progressDialog.showProgress();
                Utils.getMobileContacts(getActivity(), this, deviceCountryCode);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * On options item selected.
     *
     * @param item the item
     *
     * @return true, if successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.action_group_chat:
                    startActivityForResult(new Intent(getActivity(), ActivityNewGroup.class), Constants.ACTIVITY_REQ_CODE);//Start the group chat
                    break;
                case R.id.action_new:
                    if (mViewPager.getCurrentItem() == 2)
                        refreshContacts();//Refresh contacts
                    else if (mViewPager.getCurrentItem() == 1)
                        startActivityForResult(new Intent(getActivity(), ActivitySelectContact.class), Constants.ACTIVITY_REQ_CODE);//Select contact
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        int pageNum = mViewPager.getCurrentItem();
        if (mMenu != null) {
            MenuItem item = mMenu.findItem(R.id.action_new);
            MenuItem search = mMenu.findItem(R.id.action_search);
            //visible
            search.setVisible(true);
            //visible
            item.setVisible(true);
            //visible
            item.setIcon(R.drawable.icon_compose);
            //visible
            item.setTitle(getString(R.string.text_new_chat));
            if (pageNum == 1) {
                //visible
                search.setVisible(true);
                //visible
                item.setVisible(true);
                //visible
                item.setIcon(R.drawable.icon_compose);
                //visible
                item.setTitle(getString(R.string.text_new_chat));
                //Setting the text color
                txtChats.setTextColor(resources.getColor(R.color.blue_color));
                //Setting the text color
                txtTitle.setTextColor(resources.getColor(android.R.color.black));
                //Setting the text color
                mTxtTitle.setTextColor(resources.getColor(android.R.color.black));
            } else if (pageNum == 2) {
                //Visible
                search.setVisible(true);
                //Visible
                item.setVisible(true);
                //Setting the icon
                item.setIcon(R.drawable.ic_action_refresh);
                //Setting the title refresh
                item.setTitle(getString(R.string.text_refresh));
                //Setting the text color
                mTxtTitle.setTextColor(resources.getColor(R.color.blue_color));
                //Setting the text color
                txtChats.setTextColor(resources.getColor(android.R.color.black));
                //Setting the text color
                txtTitle.setTextColor(resources.getColor(android.R.color.black));
            } else if (pageNum == 0) {
                search.setVisible(false);
                item.setVisible(false);
                //Setting the text color
                mTxtTitle.setTextColor(resources.getColor(android.R.color.black));//Setting the text color
                txtChats.setTextColor(resources.getColor(android.R.color.black));
                //Setting the text color
                txtTitle.setTextColor(resources.getColor(R.color.blue_color));
            }
        }

    }

    /**
     * The Class AdapterDashboardView.
     */
    private class AdapterDashboardView extends CacheFragmentStatePagerAdapter {
        //mTitle
        private String[] mTitle = new String[]{
                getString(R.string.text_polls), getString(R.string.text_chats), getString(R.string.text_contacts)};

        /**
         * Instantiates a new adapter dashboard view.
         *
         * @param fm the fm
         */
        public AdapterDashboardView(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            if (position == Constants.COUNT_ZERO) {
                //Polls fragmentonR
                pollFragment = new PollsFragment();
                return pollFragment;
            } else if (position == Constants.COUNT_ONE) {
                //Recent chats
                fragmentRecentChats = new FragmentRecentChats();
                fragmentRecentChats.setOnDataChangeListener(HomeChat.this);
                return fragmentRecentChats;
            } else {
                //Fragment conatcts
                fragmentContacts = new FragmentContacts();
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
            return Constants.COUNT_THREE;
        }

        /**
         * Gets the page title.
         *
         * @param position the position
         *
         * @return the page title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle[position];
        }
    }


    /**
     * Filter data.
     *
     * @param key the key
     */
    private void filterData(String key, boolean isFilter) {
        searchKey = key.toLowerCase();
        if (!searchKey.isEmpty() && !isFilter) {
            filter(searchKey);
        } else if (isFilter) {
            filter(searchKey);
        }
    }

    private void filter(String key) {
        if (mViewPager.getCurrentItem() == Constants.COUNT_ONE) {
            if (fragmentRecentChats != null)
                fragmentRecentChats.filterList(key);
        } else if (mViewPager.getCurrentItem() == Constants.COUNT_TWO) {
            if (fragmentContacts != null)
                fragmentContacts.filterList(key);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.contusfly.fragments.FragmentRecentChats.OnDataChangeListener#
     * onDataChanged()
     */
    @Override
    public void onDataChanged() {
        setUnReadCount();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.contusfly.activities.ActivityBase#onTypingResult(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    protected void onTypingResult(String userName, String status, String userID) {
        fragmentRecentChats.onTypingResult(userName, status, userID);
    }

    /**
     * Refresh contacts.
     */
    private void refreshContacts() {
        if (getActivity() != null) {
            MApplication.materialdesignDialogStart(getActivity());
            Utils.getMobileContacts(getActivity(), this, deviceCountryCode);
        }
    }


    /**
     * Close progress.
     */
    private void closeProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.contusfly.utils.Utils.ContactResult#OnContactResult(java.lang.String)
     */
    @Override
    public void OnContactResult(String mobileNo) {
        Log.e("mobileNo", "mobileNo" + "");
        if (mobileNo != null && !mobileNo.isEmpty()) {
            contactsLoad(mobileNo);
        } else {
            closeProgress();
            Activity activity = getActivity();
            if (activity != null) {
                CustomToast.showToast(getActivity(),
                        getString(R.string.text_no_contacts_to_sync));
            }
        }
    }

    /**
     * Request and response api method
     *
     * @param contactsString
     * @param contactsString
     */
    private void contactsLoad(String contactsString) {
        if (getActivity() != null) {
            userId = MApplication.getString(getActivity(), Constants.USER_ID);
            if (MApplication.isNetConnected(getActivity())) {
                /**  Requesting the response from the given base url**/
                ContactsRestClient.getInstance().postContactDetails(new String("contact_api"), new String(userId), new String(contactsString),
                        new Callback<ContactResponseModel>() {
                            @Override
                            public void success(ContactResponseModel contactResponseModel, retrofit.client.Response response) {
                                getActivity().startService(new Intent(getActivity(), ChatService.class).setAction(ContusConstantValues.CONTUS_XMPP_ACTION_GET_ROSTER));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        fragmentContacts.refreshContacts();
                                        closeProgress();
                                    }
                                }, 3000);
                                MApplication.materialdesignDialogStop();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                //Error message popups when the user cannot able to coonect with the server
                                MApplication.errorMessage(error, getActivity());
                                MApplication.materialdesignDialogStop();
                            }
                        });

            }
        }
    }
}



