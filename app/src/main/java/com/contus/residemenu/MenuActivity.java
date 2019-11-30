package com.contus.residemenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.MyProfileNew.MyProfileNew;
import com.contus.aboutus.AboutUs;
import com.contus.activity.CustomRequest;
import com.contus.app.Constants;
import com.contus.category.Category;
import com.contus.category.IFragmentReplace;
import com.contus.contactus.ContactUs;
import com.contus.mypolls.MyPolls;
import com.contus.myprofile.EditProfile;
import com.contus.publicpoll.PublicPoll;
import com.contus.restclient.UserPollRestClient;
import com.contus.search.SearchActivity;
import com.contus.settings.Settings;
import com.contus.userpolls.UserPolls;
import com.contusfly.MApplication;
import com.contusfly.chat.HomeChat;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.contusfly.createpoll.MultipleOptions;
import com.contusfly.createpoll.PhotoComparison;
import com.contusfly.createpoll.YesOrNo;
import com.contusfly.fragments.FragmentContacts;
import com.contusfly.fragments.FragmentRecentChats;
import com.contusfly.model.PollUnseenStatusModel;
import com.contusfly.utils.Utils;
import com.contusfly.views.CircularImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.new_chanages.activity.Rewards_Activty;
import com.new_chanages.fragment.EditGroupsFragment;
import com.new_chanages.fragment.GroupsFragment;
import com.new_chanages.models.GetErnigsInputModel;
import com.polls.polls.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MenuActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener, IFragmentReplace, View.OnClickListener, MyPolls.MyPollsOnInteractionListner, UserPolls.UserPollOnFragmentInteractionListner, PublicPoll.CampaignPollInteractionListner{
    //reside menu
    private ResideMenu resideMenu;
    //menu activity
    private MenuActivity mContext;
    //item home
    private ResideMenuItem itemHome;
    //item profile
    private ResideMenuItem itemProfile;

    private ResideMenuItem itemEditGroups;

    //item top10users
    private ResideMenuItem itemTop10users;
    //item categories
    private ResideMenuItem itemCategories;
    //item settings
    //private ResideMenuItem itemSettings;
    //item feedback
  //  private ResideMenuItem itemFeedback;
    //item about us
    private ResideMenuItem itemAboutUs;
    //item contact us
    private ResideMenuItem itemContactUs;
    //Linear layout
    LinearLayout userPoll, chat;
    //Linear layout
    private LinearLayout publicPoll;
    //Frame layout
    private FrameLayout customFragmnet;
    //Frame Layout
    private FrameLayout mFragment;
    //Text view
    private TextView txtTitle;
    //image view
    private ImageView imgSearch;
    //Reside menu item
    private ResideMenuItem itemMyPolls;
    private ResideMenuItem itemMyGroups;

    //Reside menu item
    private ResideMenuItem itemCampains;
    //chat
    private ImageView imageChat;
    private static TextView getChatViewUnseenCount;
    private static TextView getUserPollUnseenCountChatview;
    private static TextView getAdminPollUnseenCountChatview;
    private static TextView getPublicPollChatUnseenCount;
    private static TextView getUserPollCountPrivateView;
    private static TextView getAdminPollCountAdminView;
    private static TextView getPrivatePollChatUnseenCount;
    private static TextView getUserPollCount;
    private static TextView getAdminPollCount;
    //user
    private ImageView imageUser;
    //public
    private ImageView imagePublic;
    //image profile
    private CircularImageView imgProfile;
    //title
    private TextView title;
    //icon
    private ImageView icon;
    //divider line
    private View dividerLine;
    //relative
    private RelativeLayout relative;
    //ImageView
    String searchKey;
    private String userId;
    //Fragment
    FragmentRecentChats fragmentRecentChats;
    //Fragment
    FragmentContacts fragmentContacts;
    //Create poll
    private ImageView btnCreatePoll;
    //linear layout
    private LinearLayout layoutTop;
    private ArrayList<String> mCategory;
    private ArrayList<String> listGroupid;
    private ArrayList<String> mArrayList;
    private ArrayList<String> mContactName;
    private ArrayList<String> mGroupName;
    private TextView mTitle;
    private Button bDone;
    private Toolbar toolbar;
    private String backStateName;
    private Fragment currFrag;
    private ImageView imgMore;
    private ImageView imageAddComments;
    private ImageView imgEdit1;
    private FrameLayout frame1;
    private ImageView imgEdit2;
    private ImageView titleBarLeftMenu1;
    private ImageView titleBarLeftMenu;
    private ImageView img_add_group;
    private MApplication mApplication;
    private DatabaseHelper db;
    public String fromActivity="";
    Toast exitToast;
    Boolean doubleBackToExitPressedOnce = true;
    /**
     * TextViewCalled when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fresco initialization
        Fresco.initialize(this);
        setContentView(R.layout.main);
        exitToast = Toast.makeText(getApplicationContext(), "Press back again to exit MyPollBook", Toast.LENGTH_SHORT);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Initializing the activity
        mContext = this;
        userPoll =  findViewById(R.id.userpoll);
        imageUser =  userPoll.findViewById(R.id.imgUserPoll);
        publicPoll =  findViewById(R.id.publicpoll);
        chat =  findViewById(R.id.chat);
        imageChat =  chat.findViewById(R.id.imgChat);
        getChatViewUnseenCount =  chat.findViewById(R.id.chat_count);
        getUserPollUnseenCountChatview =  chat.findViewById(R.id.user_poll_unseen_count_chat);
        getAdminPollUnseenCountChatview =  chat.findViewById(R.id.admin_poll_count_chat_view);
        getPublicPollChatUnseenCount =  publicPoll.findViewById(R.id.unseen_public_poll);
        getUserPollCountPrivateView =  publicPoll.findViewById(R.id.user_poll_unseen_count_private_poll);
        getAdminPollCountAdminView =  publicPoll.findViewById(R.id.admin_poll_count);
        getPrivatePollChatUnseenCount =  userPoll.findViewById(R.id.get_unseen_chat_count_unseen_window);
        getUserPollCount =  userPoll.findViewById(R.id.user_poll_count);
        getAdminPollCount =  userPoll.findViewById(R.id.admin_poll_count_public_view);
        imagePublic =  publicPoll.findViewById(R.id.imgPublicPoll);
        customFragmnet =  findViewById(R.id.main_fragment);
        mFragment =  findViewById(R.id.fragment);
        txtTitle =  findViewById(R.id.txtTitle);
        imgSearch =  findViewById(R.id.imgSearch);
        imgProfile =  findViewById(R.id.imageProfile);
        btnCreatePoll =  this.findViewById(R.id.imgEdit);
        imgMore =  findViewById(R.id.imgMore);
        imageAddComments =  findViewById(R.id.imageAddComments);
        layoutTop =  findViewById(R.id.layout_top);
        toolbar =  findViewById(R.id.mToolbar);//A Toolbar is a generalization of action bars for use within application layouts.
        mTitle =  toolbar.findViewById(R.id.toolbar_title);
        db = new DatabaseHelper(this);
        userId = MApplication.getString(mContext, Constants.USER_ID);
        bDone =  findViewById(R.id.txtDone);
        imgEdit1 =  findViewById(R.id.imgEdit1);
        frame1 =  findViewById(R.id.frame1);
        imgEdit2 =  findViewById(R.id.imgEdit2);
        titleBarLeftMenu1 =  findViewById(R.id.title_bar_left_menu1);
        titleBarLeftMenu =  findViewById(R.id.title_bar_left_menu);
        img_add_group =  findViewById(R.id.img_add_group);
        //Set a Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);
        //Set whether home should be displayed as an "up" affordance.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Set whether an activity title/subtitle should be displayed.
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        try{
            fromActivity=getIntent().getStringExtra("fromActivityName");
        }catch (NullPointerException e){
            e.printStackTrace();
            fromActivity="";
        }

        //Navigation icon
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open menu to the left
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);

            }
        });

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Light.ttf");
        txtTitle.setTypeface(face);
        mTitle.setTypeface(face);

        mApplication = (MApplication) getApplicationContext();
        mCategory = new ArrayList<>();
        listGroupid = new ArrayList<>();
        mArrayList = new ArrayList<>();
        mContactName = new ArrayList<>();
        mGroupName = new ArrayList<>();
        MApplication.setBoolean(MenuActivity.this, "createpoll", false);
        //Hide system UI
        MApplication.hideSystemUI(MenuActivity.this);

        /*if(mTitle.getText().equals("Groups"))
        {
            img_add_group.setVisibility(View.VISIBLE);
        }
        else {
            img_add_group.setVisibility(View.GONE);
        }*/
        fragmentRecentChats = new FragmentRecentChats();
        fragmentContacts = new FragmentContacts();

        if (savedInstanceState == null) {
            layoutTop.setVisibility(View.GONE);
            //Setting the text
            if (MApplication.getBoolean(this, "notification click")) {
                mTitle.setText(getResources().getString(R.string.activity_add_chat));
            } else {
                mTitle.setText(getResources().getString(R.string.activity_userpoll));
            }
            //visibility gone
            imageAddComments.setVisibility(View.GONE);
        } //Set up menu
        setUpMenu();

        //Interface definition for a callback to be invoked when a view is clicked.
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intents are objects of the android.content.Intent type. Your code can send them to the Android system defining the components you are targeting.
                // For example, via the startActivity() method you can define that the intent should be used to start an activity.
                Intent search = new Intent(MenuActivity.this, SearchActivity.class);
                startActivity(search);
            }
        });

        //Interface definition for a callback to be invoked when a view is clicked.
        findViewById(R.id.txtDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This method is used to save the categories which is chosen by the user
                Category categoryfragment = new Category();
                categoryfragment.categoryPollSave(MenuActivity.this);
            }
        });
        toolbar.findViewById(R.id.imgEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSpinner(view);
            }
        });
        String getBatchCount = mApplication.getStringFromPreference(com.contus.app.Constants.GET_MESSAGE_UNREAD_COUNT);
        if (TextUtils.isEmpty(getBatchCount)) {
            MApplication.storeStringInPreference(com.contus.app.Constants.GET_MESSAGE_UNREAD_COUNT, "0");
        }
        if (mApplication.getStringFromPreference(Constants.GET_MESSAGE_UNREAD_COUNT).equals("0")) {
            getChatViewUnseenCount.setVisibility(View.GONE);
            getPublicPollChatUnseenCount.setVisibility(View.GONE);
            getPrivatePollChatUnseenCount.setVisibility(View.GONE);
        } else {

            getPublicPollChatUnseenCount.setVisibility(View.GONE);
            getChatViewUnseenCount.setText(mApplication.getStringFromPreference(Constants.GET_MESSAGE_UNREAD_COUNT));
            getPublicPollChatUnseenCount.setText(mApplication.getStringFromPreference(Constants.GET_MESSAGE_UNREAD_COUNT));
            getPrivatePollChatUnseenCount.setText(mApplication.getStringFromPreference(Constants.GET_MESSAGE_UNREAD_COUNT));
        }
    }


    /**
     * Setting up the menu
     */
    private void setUpMenu() {
        Log.e("testtimes", "testtimes");
        resideMenu = new ResideMenu(this);
        // attach to current activity
        resideMenu.attachToActivity(this);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        if (MApplication.getBoolean(this, "notification click")) {
            //chat mFragment
            imageChangeFragment(new HomeChat());
            //setting the text
            mTitle.setText(getResources().getString(R.string.activity_add_chat));
            //view gone
            imageAddComments.setVisibility(View.GONE);
            //image profile gone
            imgProfile.setVisibility(View.GONE);
            //image edit
            imgEdit1.setVisibility(View.GONE);
            //image more visiblity gone
            imgMore.setVisibility(View.GONE);
            //view visible
            frame1.setVisibility(View.GONE);
            //view visible
            //view visible
            imgSearch.setVisibility(View.GONE);
            //view gone
            bDone.setVisibility(View.GONE);
            imageChat.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click));
            //view gone
            titleBarLeftMenu1.setVisibility(View.GONE);
            layoutTop.setVisibility(View.GONE);
            //view gone
            userPoll.setVisibility(View.GONE);
            //view visible
            chat.setVisibility(View.VISIBLE);
            //view gone
            publicPoll.setVisibility(View.GONE);
        } else {
            userPoll.setVisibility(View.VISIBLE);
            //view visible
            frame1.setVisibility(View.GONE);
            imageChangeFragment(new UserPolls());
        }
        // create itemHome items
        itemHome = new ResideMenuItem(this, R.drawable.ic_home, getResources().getString(R.string.activity_home),false);
        // create itemMyPolls items
        itemMyPolls = new ResideMenuItem(this, R.drawable.ic_my_polls_gray, getResources().getString(R.string.activity_mypolls),false);
         // create itemMyPolls items
        itemMyGroups = new ResideMenuItem(this, R.drawable.ic_my_polls_gray, getResources().getString(R.string.activity_groups),false);
        // create itemProfile items
        itemProfile = new ResideMenuItem(this, R.drawable.ic_profile_gray, getResources().getString(R.string.activity_myprofile),false);
        // create edit Groups items
       itemEditGroups = new ResideMenuItem(this, R.drawable.group, getResources().getString(R.string.activity_create_poll_groups),false);

        // create itemCategories items
        itemCategories = new ResideMenuItem(this, R.drawable.ic_categories_gray, getResources().getString(R.string.activity_mycategory),false);
        // create itemSettings items
        // itemSettings = new ResideMenuItem(this, R.drawable.ic_settings_gray, getResources().getString(R.string.activity_settings));
        // create itemAboutUs items
        itemTop10users = new ResideMenuItem(this, R.drawable.rewards, getResources().getString(R.string.activity_topusers),false);

        itemAboutUs = new ResideMenuItem(this, R.drawable.ic_about_app_gray, getResources().getString(R.string.activity_about_app),true);
        // create itemFeedback items
        // itemFeedback = new ResideMenuItem(this, R.drawable.ic_feedback_gray, getResources().getString(R.string.activity_feedback),true);
        // create itemContactUs items
        itemContactUs = new ResideMenuItem(this, R.drawable.ic_contact_gray, getResources().getString(R.string.activity_contactus),true);
        itemCampains = new ResideMenuItem(this, R.drawable.campaign, "Campaigns",true);
        //divider line
        dividerLine = itemHome.findViewById(R.id.view);
        //title
        title =  itemHome.findViewById(R.id.tv_title);
        //icon
        icon =  itemHome.findViewById(R.id.iv_icon);
        //view visible
        dividerLine.setVisibility(View.VISIBLE);
        //setting the text color
        title.setTextColor(getResources().getColor(R.color.color_white));
        //Setting the icon color
        icon.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
        //Interface definition for a callback to be invoked when a view is clicked.
        itemHome.setOnClickListener(this);
        itemMyPolls.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemEditGroups.setOnClickListener(this);
        itemTop10users.setOnClickListener(this);
        itemCategories.setOnClickListener(this);
        //itemSettings.setOnClickListener(this);
        //itemFeedback.setOnClickListener(this);
        itemAboutUs.setOnClickListener(this);
        itemContactUs.setOnClickListener(this);
        itemCampains.setOnClickListener(this);

        //Adding home to the menu
        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        //Adding itemMyPolls to the menu
        resideMenu.addMenuItem(itemMyPolls, ResideMenu.DIRECTION_LEFT);
        //Adding home to the menu
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemEditGroups, ResideMenu.DIRECTION_LEFT);
        //Adding itemCategories to the menu
        resideMenu.addMenuItem(itemCategories, ResideMenu.DIRECTION_LEFT);
        //Adding itemSettings to the menu
        //resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);
        //Adding itemAboutUs to the menu
        resideMenu.addMenuItem(itemTop10users, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAboutUs, ResideMenu.DIRECTION_LEFT);
        //Adding itemFeedback to the menu
        //resideMenu.addMenuItem(itemFeedback, ResideMenu.DIRECTION_LEFT);
        //Adding itemContactUs to the menu
        resideMenu.addMenuItem(itemContactUs, ResideMenu.DIRECTION_LEFT);

        //Adding itemCampains to the menu
        resideMenu.addMenuItem(itemCampains, ResideMenu.DIRECTION_LEFT);
        // You can disable a direction by setting ->
        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open menu to the left
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        titleBarLeftMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open menu to the left
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        imgEdit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intents are objects of the android.content.Intent type. Your code can send them to the Android system defining the components you are targeting.
                // For example, via the startActivity() method you can define that the intent should be used to start an activity.
                Intent a = new Intent(MenuActivity.this, EditProfile.class);
                startActivity(a);
            }
        });

        getInfo();
    }

    /**
     * Calling this method from xml file when performing the click on the ACTION
     *
     * @param view
     */
    public void onClickSpinner(View view) {
        mCategory = MApplication.loadStringArray(MenuActivity.this, mCategory, Constants.ACTIVITY_CATEGORY_COMPLETE_INFO, Constants.ACTIVITY_CATEGORY__COMPLETE_INFO_SIZE);
        mCategory.clear();
        //  if (view.getId() == R.id.imgEdit||view.getId() == R.id.imgEdit2) {
        mCategory.add("Public");
        DatabaseHelper db = new DatabaseHelper(this);
        db.deleteTable();
        db.deleteGroupTable();

        MApplication.saveStringArray(MenuActivity.this, mCategory, Constants.ACTIVITY_CATEGORY_COMPLETE_INFO, Constants.ACTIVITY_CATEGORY__COMPLETE_INFO_SIZE);
        MApplication.saveStringArray(MenuActivity.this, mCategory, Constants.ACTIVITY_CATEGORY_INFO, Constants.ACTIVITY_CATEGORY_INFO_SIZE);
        MApplication.saveStringArray(MenuActivity.this, listGroupid, com.contus.app.Constants.ACTIVITY_GROUP_INFO, Constants.ACTIIVTY_GROUP_INFO_SIZE);
        MApplication.saveStringArray(MenuActivity.this, mArrayList, Constants.ACTIVITY_CONTACT_INFO, Constants.ACTIIVTY_CONTACT_INFO_SIZE);
        MApplication.saveStringArray(MenuActivity.this, mContactName, com.contus.app.Constants.ACTIVITY_CONTACT_NAME_INFO, com.contus.app.Constants.ACTIIVTY_CONTACT_NAME_INFO_SIZE);
        MApplication.saveStringArray(MenuActivity.this, mGroupName, com.contus.app.Constants.ACTIVITY_GROUP_NAME_INFO, com.contus.app.Constants.ACTIVITY_GROUP_NAME_INFO_SIZE);
        MApplication.setString(MenuActivity.this, "imageQuestion1", "");
        MApplication.setString(MenuActivity.this, "imageQuestion2", "");
        MApplication.setString(MenuActivity.this, "imageMultipleOptionQuestion1", "");
        MApplication.setString(MenuActivity.this, "imageMultipleOptionQuestion2", "");
        MApplication.setString(MenuActivity.this, "imagePhotoComparisionQuestion1", "");
        MApplication.setString(MenuActivity.this, "imagePhotoComparisionQuestion2", "");
        MApplication.setString(MenuActivity.this, "imageFilePathQuestion1", "");
        MApplication.setString(MenuActivity.this, "imageFilePathQuestion2", "");
        MApplication.setString(MenuActivity.this, "imageMultipleOptionFilePathQuestion1", "");
        MApplication.setString(MenuActivity.this, "imageMultipleOptionFilePathQuestion2", "");
        MApplication.setString(MenuActivity.this, "imagePhotoComparisionFilePathQuestion1", "");
        MApplication.setString(MenuActivity.this, "imagePhotoComparisionFilePathQuestion2", "");
        MApplication.setString(MenuActivity.this, "option1", "");
        MApplication.setString(MenuActivity.this, "option2", "");
        MApplication.setString(MenuActivity.this, "option3", "");
        MApplication.setString(MenuActivity.this, "option4", "");
        MApplication.setString(MenuActivity.this, "filePathOption1", "");
        MApplication.setString(MenuActivity.this, "filePathOption2", "");
        MApplication.setString(MenuActivity.this, "filePathOption3", "");
        MApplication.setString(MenuActivity.this, "filePathOption4", "");

        Context wrapper = new ContextThemeWrapper(MenuActivity.this, R.style.PopupMenu);
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(wrapper, btnCreatePoll);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_yesorNo:
                        MApplication.setBoolean(MenuActivity.this, "createpoll", true);
                        //Intents are objects of the android.content.Intent type. Your code can send them to the Android system defining the components you are targeting.
                        // For example, via the startActivity() method you can define that the intent should be used to start an activity.
                        Intent a = new Intent(MenuActivity.this, YesOrNo.class);
                        startActivity(a);
                        return true;
                    case R.id.item_multiple_options:
                        MApplication.setBoolean(MenuActivity.this, "createpoll", true);
                        //Intents are objects of the android.content.Intent type. Your code can send them to the Android system defining the components you are targeting.
                        // For example, via the startActivity() method you can define that the intent should be used to start an activity.
                        Intent b = new Intent(MenuActivity.this, MultipleOptions.class);
                        startActivity(b);
                        return true;
                    case R.id.item_photocomparison:
                        MApplication.setBoolean(MenuActivity.this, "createpoll", true);
                        //Intents are objects of the android.content.Intent type. Your code can send them to the Android system defining the components you are targeting.
                        // For example, via the startActivity() method you can define that the intent should be used to start an activity.
                        Intent c = new Intent(MenuActivity.this, PhotoComparison.class);
                        startActivity(c);
                        return true;
                }
                return false;
            }
        });
        popup.show();//s
        //}
    }

    /**
     * Calling this method from xml file when performing the click on the ACTION
     *
     * @param view
     */
    public void onClickButton(View view) {
        if (view.getId() == R.id.imgUserPoll) {
            MApplication.materialdesignDialogStart(mContext);
            UserPollRestClient.getInstance().postUnseenCount("getUserPollsSeen", userId, "user"
                    , new Callback<PollUnseenStatusModel>() {
                        @Override
                        public void success(PollUnseenStatusModel userPollResponseModel, Response response) {
                            if (userPollResponseModel.getResponseCode().equals("200")) {
                                getUserPollCount.setVisibility(View.GONE);
                                getUserPollUnseenCountChatview.setVisibility(View.GONE);
                                getUserPollCountPrivateView.setVisibility(View.GONE);
                                toolbar.setVisibility(View.VISIBLE);

                                layoutTop.setVisibility(View.GONE);
                                //setting the text
                                mTitle.setText(getResources().getString(R.string.activity_userpoll));
                                //View visible
                                btnCreatePoll.setVisibility(View.VISIBLE);
                                //View visible
                                imgMore.setVisibility(View.GONE);
                                //User polls mFragment
                                imageChangeFragment(new UserPolls());
                                //View visible
                                imgSearch.setVisibility(View.VISIBLE);
                                //view visible
                                frame1.setVisibility(View.GONE);
                                //View visible
                                //View visible
                                userPoll.setVisibility(View.VISIBLE);
                                //View gone
                                chat.setVisibility(View.GONE);
                                //View gone
                                publicPoll.setVisibility(View.GONE);
                                MApplication.materialdesignDialogStop();
                            }
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.materialdesignDialogStop();
                        }
                    });
        } else if (view.getId() == R.id.imgPublicPoll) {

            toolbar.setVisibility(View.VISIBLE);
            mTitle.setText("Groups");
            MApplication.setBoolean(mContext, Constants.IS_IN_GROUP, true);
            //btnCreatePoll.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_feedback_gray));
            //group poll
            imageChangeFragment(new GroupsFragment());
        /*    MApplication.materialdesignDialogStart(mContext);
            UserPollRestClient.getInstance().postUnseenCount("getUserPollsSeen", userId, "admin"
                    , new Callback<PollUnseenStatusModel>() {
                        @Override
                        public void success(PollUnseenStatusModel userPollResponseModel, Response response) {
                            if (userPollResponseModel.getResponseCode().equals("200")) {
                                getAdminPollCountAdminView.setVisibility(View.GONE);
                                getAdminPollUnseenCountChatview.setVisibility(View.GONE);
                                getAdminPollCount.setVisibility(View.GONE);
                                toolbar.setVisibility(View.VISIBLE);

                                layoutTop.setVisibility(View.GONE);
                                //view invisible
                                imgSearch.setVisibility(View.GONE);
                                //view invisible
                                btnCreatePoll.setVisibility(View.GONE);
                                //view invisible
                                imgMore.setVisibility(View.GONE);
                                //setting the text
                                mTitle.setText(getResources().getString(R.string.campaigns));
                                //view visible
                                frame1.setVisibility(View.GONE);
                                //public poll
                                imageChangeFragment(new PublicPoll());

                                //start animation
                                imagePublic.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click));
                                //visibility gone
                                userPoll.setVisibility(View.GONE);
                                //visibility gone
                                chat.setVisibility(View.GONE);
                                //visibility visible
                                publicPoll.setVisibility(View.VISIBLE);
                                MApplication.materialdesignDialogStop();
                            }
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.materialdesignDialogStop();
                        }
                    });*/

        } else if (view.getId() == R.id.imgChat) {
            toolbar.setVisibility(View.VISIBLE);
            //chat mFragment
            imageChangeFragment(new HomeChat());
            //setting the text
            mTitle.setText(getResources().getString(R.string.activity_add_chat));
            //visibility gone
            findViewById(R.id.imgSearch).setVisibility(View.INVISIBLE);
            //view visible
            imgMore.setVisibility(View.VISIBLE);
            //view gone
            findViewById(R.id.imgEdit).setVisibility(View.GONE);
            //start animation
            imageChat.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click));
            //view visible
            frame1.setVisibility(View.GONE);
            //view gone
            userPoll.setVisibility(View.GONE);
            //view visible
            chat.setVisibility(View.VISIBLE);
            //view gone
            publicPoll.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        //title
        title =  view.findViewById(R.id.tv_title);
        //icon
        icon =  view.findViewById(R.id.iv_icon);
        //divider line
        dividerLine = view.findViewById(R.id.view);
        //setting the text color
        title.setTextColor(getResources().getColor(R.color.color_white));
        //Setting the icon color
        icon.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
        //divider line visible
        dividerLine.setVisibility(View.VISIBLE);
        //relative layout
        relative =  view.findViewById(R.id.relative);
        //If the view matches itemHome
        if (view == itemHome) {
            MApplication.setBoolean(this, "mypollfragment", false);
            imageAddComments.setVisibility(View.GONE);
            titleBarLeftMenu1.setVisibility(View.GONE);
            imgEdit1.setVisibility(View.GONE);
            //If user poll get visibility setting the text title as user poll
            //If Chats get visibility setting the text title as Chats
            //If Public Poll get visibility setting the text title as Public Poll

            layoutTop.setVisibility(View.GONE);
            //setting the text`
            txtTitle.setText(getResources().getString(R.string.activity_userpoll));
            //View visible
            btnCreatePoll.setVisibility(View.VISIBLE);
            //View visible
            imgMore.setVisibility(View.GONE);
            //User polls mFragment
            imageChangeFragment(new UserPolls());
            //View visible
            imgSearch.setVisibility(View.VISIBLE);
            //view visible
            frame1.setVisibility(View.GONE);

            //view visible
            imgProfile.setVisibility(View.GONE);
            //View visible
            userPoll.setVisibility(View.VISIBLE);
            //View gone
            chat.setVisibility(View.GONE);
            //View gone
            publicPoll.setVisibility(View.GONE);
            bDone.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            //setting the text
            mTitle.setText(getResources().getString(R.string.activity_userpoll));

            //View visible
            userPoll.setVisibility(View.VISIBLE);
            //View gone
            chat.setVisibility(View.GONE);
            //View gone
            publicPoll.setVisibility(View.GONE);

            //If view does not equal to home
        } else if (!view.equals(itemHome)) {
            //      toolbar.setVisibility(View.GONE);
            //title
            title =  itemHome.findViewById(R.id.tv_title);
            //icon
            icon =  itemHome.findViewById(R.id.iv_icon);
            //divider line
            dividerLine = itemHome.findViewById(R.id.view);
            //visibility invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative
            relative =  itemHome.findViewById(R.id.relative);
            //title set text color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon set text color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //relative set text color
            //relative.setBackgroundColor(getResources().getColor(R.color.background_color));

        }

        if(view==itemCampains){
            /*toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);*/
            //view are initialized
            //setting the text
            //txtTitle.setText("Campaigns");
            //Default fragment
            //Intent i = new Intent(getApplicationContext(), Campain.class);
            //startActivity(i);
            userPoll.setVisibility(View.GONE);
            publicPoll.setVisibility(View.GONE);
            imageChangeFragment(new PublicPoll());
        }
        else{

        }
        //If the view matches itemMyPolls
        if (view == itemMyPolls) {
            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //visibility gone
            imgMore.setVisibility(View.GONE);
            //visibility gone
            imageAddComments.setVisibility(View.GONE);
            //visibility gone
            imgProfile.setVisibility(View.GONE);
            //visibility gone
            imgEdit1.setVisibility(View.GONE);
            //view visible
            frame1.setVisibility(View.VISIBLE);
            //view visible
            imgSearch.setVisibility(View.VISIBLE);
            //view visible
            imgEdit2.setVisibility(View.VISIBLE);
            //view gone
            imgSearch.setVisibility(View.GONE);
            //view gone
            bDone.setVisibility(View.GONE);
            //view gone
            titleBarLeftMenu1.setVisibility(View.GONE);
            //set the title as my polls
            txtTitle.setText(getResources().getString(R.string.activity_mypolls));
            //Set the boolean
            MApplication.setBoolean(this, "createpoll", false);
            //This view contains only create poll icon
            OnMyPollsFragment("", "");
        } else if (!view.equals(itemMyPolls)) {

            layoutTop.setVisibility(View.VISIBLE);
            //title
            title =  itemMyPolls.findViewById(R.id.tv_title);
            //icon
            icon =  itemMyPolls.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //divider line
            dividerLine = itemMyPolls.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative =  itemMyPolls.findViewById(R.id.relative);
            //set background color
           // relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }

        if (view == itemEditGroups) {
            toolbar.setVisibility(View.GONE);
            //visibility gone
            imgMore.setVisibility(View.GONE);
            //visibility gone
            imageAddComments.setVisibility(View.GONE);
            //visibility gone
            imgProfile.setVisibility(View.GONE);
            //visibility gone
            imgEdit1.setVisibility(View.GONE);
            //view visible
            frame1.setVisibility(View.GONE);
            //view visible
            imgSearch.setVisibility(View.GONE);
            //view visible
            imgEdit2.setVisibility(View.GONE);
            //view gone
            imgSearch.setVisibility(View.GONE);
            //view gone

            bDone.setVisibility(View.GONE);
            //view gone
            titleBarLeftMenu1.setVisibility(View.GONE);
            //set the title as my polls
            txtTitle.setText(getResources().getString(R.string.activity_create_poll_groups));
            toolbar.setVisibility(View.VISIBLE);
            //Set the boolean
            layoutTop.setVisibility(View.GONE);
            MApplication.setBoolean(this, "createpoll", false);
            //This view contains only create poll icon
            img_add_group.setVisibility(View.VISIBLE);

            imageChangeFragment(new GroupsFragment());
        } else if (!view.equals(itemEditGroups)) {
            layoutTop.setVisibility(View.VISIBLE);
            //title
            title =  itemMyPolls.findViewById(R.id.tv_title);
            //icon
            icon =  itemMyPolls.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //divider line
            dividerLine = itemEditGroups.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative =  itemMyPolls.findViewById(R.id.relative);
            //set background color
            //relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }

        if(view==itemTop10users)
        {
            Intent i = new Intent(getApplicationContext(), Rewards_Activty.class);
            startActivity(i);
        }
        else if (!view.equals(itemTop10users)) {        /*Added By Nikita*/


            layoutTop.setVisibility(View.VISIBLE);
            //title
            title =  itemTop10users.findViewById(R.id.tv_title);
            //icon
            icon =  itemTop10users.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //divider line
            dividerLine = itemTop10users.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative =  itemTop10users.findViewById(R.id.relative);
            //set background color
            //relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }
        /*End*/

        //If the view matches itemProfile
        if (view == itemProfile) {
            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //visibility gone
            imgMore.setVisibility(View.GONE);
            //visibility gone
            imageAddComments.setVisibility(View.GONE);
            //visibility gone
            imgProfile.setVisibility(View.GONE);
            dividerLine = itemProfile.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.VISIBLE);
            //visibility gone
            imgEdit1.setVisibility(View.GONE);
            //view visible
            frame1.setVisibility(View.GONE);
            //view visible
            imgSearch.setVisibility(View.VISIBLE);
            //view visible
            imgEdit2.setVisibility(View.VISIBLE);
            //view gone
            imgSearch.setVisibility(View.GONE);
            //view gone
            bDone.setVisibility(View.GONE);
            imageAddComments.setVisibility(View.VISIBLE);
            //view gone
            titleBarLeftMenu1.setVisibility(View.VISIBLE);
            //set the title as my polls
            imgEdit1.setVisibility(View.VISIBLE);
            imgProfile.setVisibility(View.VISIBLE);

            //image profile is visible
            if (imgProfile.getVisibility() == View.VISIBLE) {
                //   Getting the string from image url

                Bitmap image = db.getImageCache("profile_pic");
                if (image != null) {
                    imgProfile.setImageBitmap(image);
                } else {
                    String imageUrl = MApplication.getString(mContext, Constants.PROFILE_IMAGE);
                    Utils.loadImageWithGlideSingleImageRounderCorner(mContext, imageUrl, imgProfile, R.drawable.img_ic_user);
                }

       //         Utils.loadImageWithGlideSingleImageRounderCorner(mContext, imageUrl, imgProfile, R.drawable.img_ic_user);

            }
            txtTitle.setText(getResources().getString(R.string.activity_profile));
            txtTitle.setVisibility(View.GONE);
            //Set the boolean
            MApplication.setBoolean(this, "createpoll", false);
            //This view contains only create poll icon
            imageChangeFragment(new MyProfileNew());

        }
       else if (!view.equals(itemProfile)) {

            layoutTop.setVisibility(View.VISIBLE);
            //title
            title =  itemProfile.findViewById(R.id.tv_title);
            //icon
            icon =  itemProfile.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //divider line
            dividerLine = itemProfile.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            imgEdit1.setVisibility(View.GONE);
            imgProfile.setVisibility(View.GONE);
            relative =  itemProfile.findViewById(R.id.relative);
            txtTitle.setVisibility(View.VISIBLE);
            //set background color
            //relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }
        if (view == itemCategories) {
            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //Image gone
            imageAddComments.setVisibility(View.GONE);
            //Image profile
            imgProfile.setVisibility(View.GONE);
            //edit icon
            imgEdit1.setVisibility(View.GONE);
            //edit icon
            findViewById(R.id.imgEdit).setVisibility(View.GONE);
            //search icon
            findViewById(R.id.imgSearch).setVisibility(View.GONE);
            //create poll more icon
            imgMore.setVisibility(View.GONE);
            //frame 1 visible
            frame1.setVisibility(View.VISIBLE);
            //text done visibility when internet connected
            if(isWorkingInternetPersent())
            {bDone.setVisibility(View.VISIBLE);}
            else
            {bDone.setVisibility(View.GONE);}
            //image search invisible
            imgSearch.setVisibility(View.INVISIBLE);
            //view gone
            titleBarLeftMenu1.setVisibility(View.GONE);
            //category
            txtTitle.setText(getResources().getString(R.string.activity_mycategory));
            // Default fragment
            imageChangeFragment(new Category());

        } else if (!view.equals(itemCategories)) {

            layoutTop.setVisibility(View.VISIBLE);
            //title
            title =  itemCategories.findViewById(R.id.tv_title);
            //icon
            icon =  itemCategories.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //divider line
            dividerLine = itemCategories.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative =  itemCategories.findViewById(R.id.relative);
            //set background color
            //relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }
       /* if (view == itemSettings) {
            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //view are initialized
            viewsBinding();
            //mTitle the title
            txtTitle.setText(getResources().getString(R.string.activity_settings));
            // Default fragment
            imageChangeFragment(new Settings());
        } *//*else if (!view.equals(itemSettings)) {
            layoutTop.setVisibility(View.VISIBLE);
            //title
           // title = (TextView) itemSettings.findViewById(R.id.tv_title);
            //icon
           // icon = (ImageView) itemSettings.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.helvetica_light_greycolor));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.helvetica_light_greycolor));
            //divider line
           // dividerLine = itemSettings.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            //relative = (RelativeLayout) itemSettings.findViewById(R.id.relative);
            //set background color
            relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }*/
        if (view == itemAboutUs) {
            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //view are initialized
            viewsBinding();
            //setting the text
            txtTitle.setText(getResources().getString(R.string.activity_about_app));
            // Default fragment
            imageChangeFragment(new AboutUs());
        } else if (!view.equals(itemAboutUs)) {
            layoutTop.setVisibility(View.VISIBLE);
            //title
            title =  itemAboutUs.findViewById(R.id.tv_title);
            //icon
            icon =  itemAboutUs.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //divider line
            dividerLine = itemAboutUs.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative =  itemAboutUs.findViewById(R.id.relative);
            //set background color
            //relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }
   /*     if (view == itemFeedback) {
            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //view are initialized
            viewsBinding();
            //setting the text
            txtTitle.setText(getResources().getString(R.string.activity_feedback));
            imageChangeFragment(new Feedback());
        } else if (!view.equals(itemFeedback)) {
            layoutTop.setVisibility(View.VISIBLE);
            //title
            title = (TextView) itemFeedback.findViewById(R.id.tv_title);
            //icon
            icon = (ImageView) itemFeedback.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.helvetica_light_greycolor));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.helvetica_light_greycolor));
            //divider line
            dividerLine = itemFeedback.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative = (RelativeLayout) itemFeedback.findViewById(R.id.relative);
            //set background color
            relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }*/
        if (view == itemContactUs) {
            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //view are initialized
            viewsBinding();
            //setting the text
            txtTitle.setText(getResources().getString(R.string.activity_contactus));
            imageChangeFragment(new ContactUs());
        } else if (!view.equals(itemContactUs)) {
            layoutTop.setVisibility(View.VISIBLE);
            //title
            title =  itemContactUs.findViewById(R.id.tv_title);
            //icon
            icon =  itemContactUs.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //divider line
            dividerLine = itemContactUs.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative =  itemContactUs.findViewById(R.id.relative);
            //set background color
            //relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }

        resideMenu.closeMenu();
    }

    /**
     * Views are initialized
     */
    private void viewsBinding() {
        //Image gone
        imageAddComments.setVisibility(View.GONE);
        //Image profile
        imgProfile.setVisibility(View.GONE);
        //frame 1 visible
        frame1.setVisibility(View.VISIBLE);
        //create poll more icon
        imgMore.setVisibility(View.GONE);  //edit icon
        imgEdit1.setVisibility(View.GONE);
        imgEdit2.setVisibility(View.GONE);
        //edit icon
        btnCreatePoll.setVisibility(View.GONE);
        //search icon
        imgSearch.setVisibility(View.GONE);
        //text done visible
        bDone.setVisibility(View.GONE);
        //image search invisible
        //view gone
        titleBarLeftMenu1.setVisibility(View.GONE);
    }


    /**
     * ChangeFragment
     *
     * @param targetFragment
     */

    private void imageChangeFragment(Fragment targetFragment) {
        //clear view list
        resideMenu.clearIgnoredViewList();
        //Visibility gone
        customFragmnet.setVisibility(View.VISIBLE);
        //set visiblity view
        mFragment.setVisibility(View.INVISIBLE);


        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack

        String backStateName = targetFragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragment2, targetFragment, targetFragment.getClass().getName());
            ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
        if (backStateName.equals(UserPolls.class.getName())) {
            chat.setVisibility(View.INVISIBLE);
            publicPoll.setVisibility(View.INVISIBLE);
            userPoll.setVisibility(View.VISIBLE);
        } else {
            chat.setVisibility(View.INVISIBLE);
            publicPoll.setVisibility(View.INVISIBLE);
            userPoll.setVisibility(View.INVISIBLE);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
    if("yes".equalsIgnoreCase(fromActivity)){
        if(doubleBackToExitPressedOnce){
            // Do what ever you want
            exitToast.show();
            doubleBackToExitPressedOnce = false;
        } else{
            finishAffinity();
            finish();
            // Do exit app or back press here
            super.onBackPressed();
        }
  }

        FragmentManager manager = getSupportFragmentManager();
        if (manager != null) {
            currFrag = manager.
                    findFragmentById(R.id.fragment2);
        }
        if (currFrag != null) {
            if ((currFrag.getTag() != null && currFrag.getTag().equals(UserPolls.class.getName())) || (currFrag.getTag() != null && currFrag.getTag().equals(HomeChat.class.getName())) || currFrag.getTag() == PublicPoll.class.getName()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
                if(doubleBackToExitPressedOnce){
                    // Do what ever you want
                    exitToast.show();
                    doubleBackToExitPressedOnce = false;
                } else{
                    finishAffinity();
                    finish();
                    // Do exit app or back press here
                    super.onBackPressed();
                }
            } else {

                super.onBackPressed();

            }
        } else {

            super.onBackPressed();
        }
    }


    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNotificationBadgeReceiver();
        removeChatUnseenCount();
        getPollCountDetails();
//If image profile is visible
        if (imgProfile.getVisibility() == View.VISIBLE) {
            //Get the url from the  prefernce
            Bitmap image = db.getImageCache("profile_pic");
            if (image != null) {
                imgProfile.setImageBitmap(image);
            } else {
                String imageUrl = MApplication.getString(mContext, Constants.PROFILE_IMAGE);
                Utils.loadImageWithGlideSingleImageRounderCorner(mContext, imageUrl, imgProfile, R.drawable.img_ic_user);
            }


            //If image url is not empty then the url is set in imageview
            //Setting the profiel image
//
        }

        if (MApplication.getBoolean(this, Constants.SEARCH_BACKPRESS_BOOLEAN)) {
            Log.e("usermy", "usermy");
            imageChangeFragment(new UserPolls());
        }
        if (MApplication.getBoolean(this, "createpoll") && MApplication.getBoolean(this, "mypollfragment")) {
            imageChangeFragment(new MyPolls());
        }
    }

    @Override
    public void OnMyPollsFragment(String type, String id) {
        imageChangeFragment(new MyPolls());
    }

    @Override
    public void OnUserPollFragment(String type, String id) {
        imageChangeFragment(new UserPolls());
    }

    @Override
    public void OnCampaignPollFragment(String type, String id) {
        publicPoll.setVisibility(View.GONE);
        imageChangeFragment(new PublicPoll());
    }


    @Override
    public void onBackStackChanged() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager != null) {
            Fragment currFrag = manager.
                    findFragmentById(R.id.fragment2);
            if (itemHome != null && currFrag != null) {
                navigateBackPressed(currFrag);
            }
        }

    }

    private void navigateBackPressed(Fragment currFrag) {
        String selectedTag = currFrag.getTag();
        Log.e("selectedTag", selectedTag + "");
        if (selectedTag.equals(UserPolls.class.getName())) {
            layoutTop.setVisibility(View.GONE);
            //View visible
            btnCreatePoll.setVisibility(View.VISIBLE);
            //View visible
            imgMore.setVisibility(View.GONE);
            imageAddComments.setVisibility(View.GONE);
            titleBarLeftMenu1.setVisibility(View.GONE);
            imgEdit1.setVisibility(View.GONE);
            //View visible
            imgSearch.setVisibility(View.VISIBLE);
            //view visible
            frame1.setVisibility(View.GONE);
            //View visible
            // view visible
            imgProfile.setVisibility(View.GONE);
            //title
            title =  itemHome.findViewById(R.id.tv_title);
            //icon
            icon =  itemHome.findViewById(R.id.iv_icon);
            //divider line
            dividerLine = itemHome.findViewById(R.id.view);
            //setting the text color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //Setting the icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
            //divider line visible
            dividerLine.setVisibility(View.VISIBLE);
            //View visible
            userPoll.setVisibility(View.VISIBLE);
            //View gone
            chat.setVisibility(View.GONE);
            //View gone
            publicPoll.setVisibility(View.GONE);
            bDone.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            //setting the text
            mTitle.setText(getResources().getString(R.string.activity_userpoll));
            //View visible
            userPoll.setVisibility(View.VISIBLE);
            //View gone
            chat.setVisibility(View.GONE);
            //View gone
            publicPoll.setVisibility(View.GONE);

        } else if (selectedTag.equals(PublicPoll.class.getName())) {
            toolbar.setVisibility(View.VISIBLE);
            layoutTop.setVisibility(View.GONE);
            //view gone
            imageAddComments.setVisibility(View.GONE);
            //image profile gone
            imgProfile.setVisibility(View.GONE);
            //image edit gone
            imgEdit1.setVisibility(View.GONE);
            //image more visibility gone
            imgMore.setVisibility(View.GONE);
            //view visible
            frame1.setVisibility(View.GONE);
            //view visible
            btnCreatePoll.setVisibility(View.GONE);
            //view visible
            imgSearch.setVisibility(View.GONE);
            //view gone
            bDone.setVisibility(View.GONE);
            titleBarLeftMenu1.setVisibility(View.GONE);
            //View visible
            userPoll.setVisibility(View.GONE);
            //View gone
            chat.setVisibility(View.GONE);
            //title
            title =  itemHome.findViewById(R.id.tv_title);
            //icon
            icon =  itemHome.findViewById(R.id.iv_icon);
            //divider line
            dividerLine = itemHome.findViewById(R.id.view);
            //setting the text color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //Setting the icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
            //divider line visible
            dividerLine.setVisibility(View.VISIBLE);
            //View gone
            publicPoll.setVisibility(View.GONE);
            //view gone
            mTitle.setText(getResources().getString(R.string.campaigns));

        } else if (selectedTag.equals(HomeChat.class.getName())) {
            toolbar.setVisibility(View.VISIBLE);
            //view gone
            imageAddComments.setVisibility(View.GONE);
            //image profile gone
            imgProfile.setVisibility(View.GONE);
            titleBarLeftMenu1.setVisibility(View.GONE);
            imgEdit1.setVisibility(View.GONE);
            //image more visibility gone
            imgMore.setVisibility(View.GONE);
            //view visible
            frame1.setVisibility(View.GONE);
            //view visible
            btnCreatePoll.setVisibility(View.GONE);
            //view visible
            imgSearch.setVisibility(View.GONE);
            //view gone
            bDone.setVisibility(View.GONE);
            //title
            title =  itemHome.findViewById(R.id.tv_title);
            //icon
            icon =  itemHome.findViewById(R.id.iv_icon);
            //divider line
            dividerLine = itemHome.findViewById(R.id.view);
            //setting the text color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //Setting the icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
            //divider line visible
            dividerLine.setVisibility(View.VISIBLE);
            //View visible
            userPoll.setVisibility(View.GONE);
            //View gone
            chat.setVisibility(View.VISIBLE);
            //View gone
            publicPoll.setVisibility(View.GONE);
            //view gone
            titleBarLeftMenu1.setVisibility(View.GONE);
            layoutTop.setVisibility(View.GONE);
            mTitle.setText(getResources().getString(R.string.activity_chats));
        } else if (!selectedTag.equals(UserPolls.class.getName())) {
            title =  itemHome.findViewById(R.id.tv_title);
            //icon
            icon =  itemHome.findViewById(R.id.iv_icon);
            //divider line
            dividerLine = itemHome.findViewById(R.id.view);
            //visibility invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative
            relative = itemHome.findViewById(R.id.relative);
            //title set text color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon set text color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //relative set text color
            //relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }
        if (selectedTag.equals(Category.class.getName())) {

            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //Image gone
            imageAddComments.setVisibility(View.GONE);
            //Image profile
            imgProfile.setVisibility(View.GONE);
            //edit icon
            imgEdit1.setVisibility(View.GONE);
            //edit icon
            btnCreatePoll.setVisibility(View.GONE);
            //search icon
            imgSearch.setVisibility(View.GONE);
            //create poll more icon
            imgMore.setVisibility(View.GONE);
            //frame 1 visible
            frame1.setVisibility(View.VISIBLE);
            //text done visible
            if(isWorkingInternetPersent())
            {bDone.setVisibility(View.VISIBLE);}
            else
            {bDone.setVisibility(View.GONE);}
            //image search invisible
            imgSearch.setVisibility(View.INVISIBLE);
            //view gone
            titleBarLeftMenu1.setVisibility(View.GONE);
            //category
            txtTitle.setText(getResources().getString(R.string.activity_mycategory));
            // Default fragment
            //title
            title =  itemCategories.findViewById(R.id.tv_title);
            //icon
            icon =  itemCategories.findViewById(R.id.iv_icon);
            //divider line
            dividerLine = itemCategories.findViewById(R.id.view);
            //setting the text color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //Setting the icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
            //divider line visible
            dividerLine.setVisibility(View.VISIBLE);

        } else {

            layoutTop.setVisibility(View.VISIBLE);
            //title
            title =  itemCategories.findViewById(R.id.tv_title);
            //icon
            icon =  itemCategories.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //divider line
            dividerLine = itemCategories.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative =  itemCategories.findViewById(R.id.relative);
            //set background color
           // relative.setBackgroundColor(getResources().getColor(R.color.color_white));
        }

        if (selectedTag.equals(MyPolls.class.getName())) {
            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //visibility gone
            imgMore.setVisibility(View.GONE);
            //visibility gone
            imageAddComments.setVisibility(View.GONE);
            //visibility gone
            imgProfile.setVisibility(View.GONE);
            //visibility gone
            imgEdit1.setVisibility(View.GONE);
            //view visible
            frame1.setVisibility(View.VISIBLE);
            //view visible
            imgSearch.setVisibility(View.VISIBLE);
            //view visible
            findViewById(R.id.imgEdit2).setVisibility(View.VISIBLE);
            //view gone
            imgSearch.setVisibility(View.GONE);
            //view gone
            bDone.setVisibility(View.GONE);
            //view gone
            titleBarLeftMenu1.setVisibility(View.GONE);
            //set the title as my polls
            txtTitle.setText(getResources().getString(R.string.activity_mypolls));
            //Set the boolean
            MApplication.setBoolean(this, "createpoll", false);
            //title
            title =  itemMyPolls.findViewById(R.id.tv_title);
            //icon
            icon =  itemMyPolls.findViewById(R.id.iv_icon);
            //divider line
            dividerLine = itemMyPolls.findViewById(R.id.view);
            //setting the text color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //Setting the icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
            //divider line visible
            dividerLine.setVisibility(View.VISIBLE);

        } else if (!selectedTag.equals(MyPolls.class.getName())) {

            layoutTop.setVisibility(View.VISIBLE);
            //title
            title =  itemMyPolls.findViewById(R.id.tv_title);
            //icon
            icon =  itemMyPolls.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //divider line
            dividerLine = itemMyPolls.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative =  itemMyPolls.findViewById(R.id.relative);
            //set background color
           // relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }

        if (selectedTag.equals(MyProfileNew.class.getName())) {
            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //view visible
            imageAddComments.setVisibility(View.VISIBLE);
            //view visible
            imgProfile.setVisibility(View.VISIBLE);
            //image profile is visible
            if (imgProfile.getVisibility() == View.VISIBLE) {
                //   Getting the string from image url

                Bitmap image = db.getImageCache("profile_pic");
                if (image != null) {
                    imgProfile.setImageBitmap(image);
                } else {
                    String imageUrl = MApplication.getString(mContext, Constants.PROFILE_IMAGE);
                    Utils.loadImageWithGlideSingleImageRounderCorner(mContext, imageUrl, imgProfile, R.drawable.img_ic_user);
                }

                //Utils.loadImageWithGlideSingleImageRounderCorner(mContext, imageUrl, imgProfile, R.drawable.img_ic_user);
                //v1iew visible
                imgEdit1.setVisibility(View.VISIBLE);
                //view invisible
                frame1.setVisibility(View.GONE);
                //text view done
                titleBarLeftMenu.setVisibility(View.VISIBLE);
                bDone.setVisibility(View.GONE);
                //view visible
                //view invisible
                imgSearch.setVisibility(View.INVISIBLE);
                //setting the text as my profile
                txtTitle.setText(getResources().getString(R.string.activity_myprofile));
                //Set the boolean
                MApplication.setBoolean(this, "createpoll", false);
                //title
                title =  itemProfile.findViewById(R.id.tv_title);
                //icon
                icon =  itemProfile.findViewById(R.id.iv_icon);
                //divider line
             //   NIKITA
                dividerLine = itemProfile.findViewById(R.id.view);//divider line visible
                dividerLine.setVisibility(View.VISIBLE);
                //setting the text color
                title.setTextColor(getResources().getColor(R.color.color_white));
                //Setting the icon color
                icon.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
            }
        } else if(!selectedTag.equals(itemProfile)) {

            layoutTop.setVisibility(View.VISIBLE);
            //title
            title =  itemProfile.findViewById(R.id.tv_title);
            //icon
            icon =  itemProfile.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //divider line
            dividerLine = itemProfile.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative =  itemProfile.findViewById(R.id.relative);
            //set background color
            //relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }
        if (selectedTag.equals(Category.class.getName())) {
            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //Image gone
            imageAddComments.setVisibility(View.GONE);
            //Image profile
            imgProfile.setVisibility(View.GONE);
            //edit icon
            imgEdit1.setVisibility(View.GONE);
            //edit icon
            btnCreatePoll.setVisibility(View.GONE);
            //search icon
            imgSearch.setVisibility(View.GONE);
            //create poll more icon
            imgMore.setVisibility(View.GONE);
            //frame 1 visible
            frame1.setVisibility(View.VISIBLE);
            //text done visible
            if(isWorkingInternetPersent())
            {bDone.setVisibility(View.VISIBLE);}
            else
            {bDone.setVisibility(View.GONE);}
            //image search invisible
            imgSearch.setVisibility(View.INVISIBLE);
            //view gone
            titleBarLeftMenu1.setVisibility(View.GONE);
            //category
            txtTitle.setText(getResources().getString(R.string.activity_mycategory));
            // Default fragment
            //title
            title =  itemCategories.findViewById(R.id.tv_title);
            //icon
            icon =  itemCategories.findViewById(R.id.iv_icon);
            //divider line
            dividerLine = itemCategories.findViewById(R.id.view);
            //setting the text color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //Setting the icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
            //divider line visible
            dividerLine.setVisibility(View.VISIBLE);

        } else {
            layoutTop.setVisibility(View.VISIBLE);
            //title
            title =  itemCategories.findViewById(R.id.tv_title);
            //icon
            icon =  itemCategories.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //divider line
            dividerLine = itemCategories.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative =  itemCategories.findViewById(R.id.relative);
            //set background color
           // relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }
        if (selectedTag.equals(Settings.class.getName())) {
           /* toolbar.setVisibility(View.GONE);

            layoutTop.setVisibility(View.VISIBLE);
            //view are initialized
            viewsBinding();
            //mTitle the title
            txtTitle.setText(getResources().getString(R.string.activity_settings));
            // Default fragment
            //title
            title = (TextView) itemSettings.findViewById(R.id.tv_title);
            //icon
            icon = (ImageView) itemSettings.findViewById(R.id.iv_icon);
            //divider line
            dividerLine = itemSettings.findViewById(R.id.view);
            //setting the text color
            title.setTextColor(getResources().getColor(R.color.blue_color));
            //Setting the icon color
            icon.setColorFilter(getResources().getColor(R.color.blue_color), PorterDuff.Mode.SRC_ATOP);
            //divider line visible
            dividerLine.setVisibility(View.VISIBLE);*/

        } else {
/*
            layoutTop.setVisibility(View.VISIBLE);
            //title
            title = (TextView) itemSettings.findViewById(R.id.tv_title);
            //icon
            icon = (ImageView) itemSettings.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.helvetica_light_greycolor));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.helvetica_light_greycolor));
            //divider line
            dividerLine = itemSettings.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative = (RelativeLayout) itemSettings.findViewById(R.id.relative);
            //set background color
            relative.setBackgroundColor(getResources().getColor(R.color.background_color));*/
        }
        if (selectedTag.equals(AboutUs.class.getName())) {
            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //view are initialized
            viewsBinding();
            //setting the text
            txtTitle.setText(getResources().getString(R.string.activity_about_app));
            //title
            title =  itemAboutUs.findViewById(R.id.tv_title);
            //icon
            icon =  itemAboutUs.findViewById(R.id.iv_icon);
            //divider line
            dividerLine = itemAboutUs.findViewById(R.id.view);
            //setting the text color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //Setting the icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
            //divider line visible
            dividerLine.setVisibility(View.VISIBLE);
        } else {
            layoutTop.setVisibility(View.VISIBLE);
            //title
            title =  itemAboutUs.findViewById(R.id.tv_title);
            //icon
            icon =  itemAboutUs.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //divider line
            dividerLine = itemAboutUs.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative =  itemAboutUs.findViewById(R.id.relative);
            //set background color
            //relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }

     /*   if (selectedTag.equals(Feedback.class.getName())) {
            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //view are initialized
            viewsBinding();
            //setting the text
            txtTitle.setText(getResources().getString(R.string.activity_feedback));
            //title
            title = (TextView) itemFeedback.findViewById(R.id.tv_title);
            //icon
            icon = (ImageView) itemFeedback.findViewById(R.id.iv_icon);
            //divider line
            dividerLine = itemFeedback.findViewById(R.id.view);
            //setting the text color
            title.setTextColor(getResources().getColor(R.color.blue_color));
            //Setting the icon color
            icon.setColorFilter(getResources().getColor(R.color.blue_color), PorterDuff.Mode.SRC_ATOP);
            //divider line visible
            dividerLine.setVisibility(View.VISIBLE);
        } else {

            layoutTop.setVisibility(View.VISIBLE);
            //title
            title = (TextView) itemFeedback.findViewById(R.id.tv_title);
            //icon
            icon = (ImageView) itemFeedback.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.helvetica_light_greycolor));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.helvetica_light_greycolor));
            //divider line
            dividerLine = itemFeedback.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative = (RelativeLayout) itemFeedback.findViewById(R.id.relative);
            //set background color
            relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }*/
        if (selectedTag.equals(ContactUs.class.getName())) {
            toolbar.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
            //view are initialized
            viewsBinding();
            //setting the text
            txtTitle.setText(getResources().getString(R.string.activity_contactus));

            //title
            title =  itemContactUs.findViewById(R.id.tv_title);
            //icon
            icon =  itemContactUs.findViewById(R.id.iv_icon);
            //divider line
            dividerLine = itemContactUs.findViewById(R.id.view);
            //setting the text color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //Setting the icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
            //divider line visible
            dividerLine.setVisibility(View.VISIBLE);
        } else {

            layoutTop.setVisibility(View.VISIBLE);
            //title
            title =  itemContactUs.findViewById(R.id.tv_title);
            //icon
            icon =  itemContactUs.findViewById(R.id.iv_icon);
            //title color
            title.setTextColor(getResources().getColor(R.color.color_white));
            //icon color
            icon.setColorFilter(getResources().getColor(R.color.color_white));
            //divider line
            dividerLine = itemContactUs.findViewById(R.id.view);
            //divider line invisible
            dividerLine.setVisibility(View.INVISIBLE);
            //relative layout
            relative =  itemContactUs.findViewById(R.id.relative);
            //set background color
            //relative.setBackgroundColor(getResources().getColor(R.color.background_color));
        }
    }

    public void registerNotificationBadgeReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getChatViewUnseenCount != null && getPublicPollChatUnseenCount != null && getPrivatePollChatUnseenCount != null) {
                    getChatViewUnseenCount.setText(mApplication.getStringFromPreference(Constants.GET_MESSAGE_UNREAD_COUNT));
                    getPublicPollChatUnseenCount.setText(mApplication.getStringFromPreference(Constants.GET_MESSAGE_UNREAD_COUNT));
                    getPrivatePollChatUnseenCount.setText(mApplication.getStringFromPreference(Constants.GET_MESSAGE_UNREAD_COUNT));
                   // getChatViewUnseenCount.setVisibility(View.VISIBLE);
                   // getPublicPollChatUnseenCount.setVisibility(View.VISIBLE);
                  //  getPrivatePollChatUnseenCount.setVisibility(View.VISIBLE);
                }
            }
        }, new IntentFilter("get_chat_unread_count"));
    }

    public void removeChatUnseenCount() {
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getChatViewUnseenCount != null && getPublicPollChatUnseenCount != null && getPrivatePollChatUnseenCount != null) {
                    getChatViewUnseenCount.setVisibility(View.GONE);
                    getPublicPollChatUnseenCount.setVisibility(View.GONE);
                    getPrivatePollChatUnseenCount.setVisibility(View.GONE);
                    mApplication.storeStringInPreference(com.contus.app.Constants.GET_MESSAGE_UNREAD_COUNT, "0");
                }
            }
        }, new IntentFilter("clear_unseen_count_chat"));
    }

    public void getPollCountDetails() {
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String userCount = intent.getStringExtra("user_count");
                String adminCount = intent.getStringExtra("admin_count");
                Log.d("get_count_details", userCount + "--" + adminCount);
                if (!userCount.equals("0")) {
                    getUserPollCount.setText(userCount);
                    getUserPollUnseenCountChatview.setText(userCount);
                    getUserPollCountPrivateView.setText(userCount);
                    getUserPollCount.setVisibility(View.VISIBLE);
                    getUserPollUnseenCountChatview.setVisibility(View.VISIBLE);
                    getUserPollCountPrivateView.setVisibility(View.VISIBLE);
                } else {
                    getUserPollCount.setVisibility(View.GONE);
                    getUserPollUnseenCountChatview.setVisibility(View.GONE);
                    getUserPollCountPrivateView.setVisibility(View.GONE);
                }

                if (!adminCount.equals("0")) {
                    getAdminPollCountAdminView.setText(adminCount);
                    getAdminPollUnseenCountChatview.setText(adminCount);
                    getAdminPollCount.setText(adminCount);
                    //getAdminPollCountAdminView.setVisibility(View.VISIBLE);
                    getAdminPollUnseenCountChatview.setVisibility(View.VISIBLE);
                    //getAdminPollCount.setVisibility(View.VISIBLE);
                } else {
                    getAdminPollCountAdminView.setVisibility(View.GONE);
                    getAdminPollUnseenCountChatview.setVisibility(View.GONE);
                    getAdminPollCount.setVisibility(View.GONE);
                }
            }
        }, new IntentFilter("get_user_poll_details"));
    }

    @Override
    public void replaceFragment() {
        imageChangeFragment(new UserPolls());
    }


    private void getInfo() {
        JSONObject obj = new JSONObject();


        UserPollRestClient.getInstance().getEarnigs(new GetErnigsInputModel("rewardsapi",userId), new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response1) {

                String result =CustomRequest.ResponcetoString(response1);
                try {
                    JSONObject response = new JSONObject(result);

                    int success = response.optInt("success");
                    {
                        if(success == 1)
                        {
                            JSONArray results= response.optJSONArray("results");
                            String total_points =results.optJSONObject(0).optString("total_points");
                            resideMenu.total_rewards_tv.setText(total_points);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),response.getString("msg"),Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        /*CustomRequest.makeJsonObjectRequest(getApplicationContext(), Constants.GET_REWARDS,obj, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                //dialog.dismiss();
                Log.i("onErrormessage", "message= " +message);
            }
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Log.i("PCCmessage", "message " + result.getString("msg"));
                    int seccess=response.optInt("success");
                    if(seccess==1)
                    {
                        JSONArray results= response.optJSONArray("results");
                        String total_points =results.optJSONObject(0).optString("total_points");
                        resideMenu.total_rewards_tv.setText(total_points);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),response.getString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

    public boolean isWorkingInternetPersent() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getBaseContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }
}
