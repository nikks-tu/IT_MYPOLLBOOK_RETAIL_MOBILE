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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.contus.chatlib.listeners.OnTaskCompleted;
import com.contus.chatlib.utils.ContusConstantValues;
import com.contusfly.MApplication;
import com.contusfly.adapters.AdapterGroupUsers;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.service.ChatService;
import com.contusfly.utils.Constants;
import com.contusfly.utils.ImageUploadS3;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.Utils;
import com.contusfly.views.CommonAlertDialog;
import com.contusfly.views.CustomToast;
import com.contusfly.views.DoProgressDialog;
import com.polls.polls.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * The Class ActivityGroupInfo.
 */
public class ActivityGroupInfo extends ActivityBase implements Utils.OnOptionSelection, View.OnClickListener,
        OnTaskCompleted, AdapterView.OnItemClickListener, CommonAlertDialog.CommonDialogClosedListener {

    /**
     * The list members.
     */
    private ListView listMembers;

    /**
     * The view add participants.
     */
    private LinearLayout viewAddParticipants;

    /**
     * The temp image.
     */
    private String groupName, groupImage, groupId, users = Constants.EMPTY_STRING, tempName = Constants.EMPTY_STRING,
            tempImage = Constants.EMPTY_STRING, rosterName = "";

    /**
     * The roster list.
     */
    private List<Rosters> rosterList = new ArrayList<>();

    /**
     * The m application.
     */
    private MApplication mApplication;

    /**
     * The txt group status.
     */
    private TextView txtCount, txtGroupStatus;

    /**
     * The image pic.
     */
    private ImageView imagePic;

    /**
     * The adapter group users.
     */
    private AdapterGroupUsers adapterGroupUsers;

    /**
     * The m dialog.
     */
    private CommonAlertDialog mDialog;

    /**
     * The array group menu.
     */
    private String[] arrayGroupMenu;

    /**
     * The dialog mode.
     */
    private int lastKnownPosition, dialogMode;

    /**
     * The progress dialog.
     */
    private DoProgressDialog progressDialog;

    /**
     * The image upload task.
     */
    private ImageUploadS3 imageUploadTask;

    /**
     * The m file temp.
     */
    private File mFileTemp;

    /**
     * The is group exit.
     */
    private boolean isGroupExit;

    /**
     * The Constant DELETE_GROUP.
     */
    private static final int REMOVE_GROUP = 1, EXIT_GROUP = 2, DELETE_GROUP = 3;

    /**
     * The toolbar layout.
     */
    private CollapsingToolbarLayout toolbarLayout;

    /*
     * (non-Javadoc)
     *
     * @see ActivityBase#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listMembers = (ListView) findViewById(R.id.view_members);
        viewAddParticipants = (LinearLayout) findViewById(R.id.add_participants);
        txtCount = (TextView) findViewById(R.id.txt_count);
        txtGroupStatus = (TextView) findViewById(R.id.txt_exitgroup);
        imagePic = (ImageView) findViewById(R.id.image);

        Intent intent = getIntent();
        groupImage = intent.getStringExtra(Constants.USER_IMAGE);
        groupName = intent.getStringExtra(Constants.USER_PROFILE_NAME);
        toolbar.setTitle("");

        Utils.loadImageWithGlide(this, groupImage, imagePic, R.drawable.ic_grp_bg);

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "image");
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        setToolbarTitle();
        viewAddParticipants.setVisibility(View.GONE);
        viewAddParticipants.setOnClickListener(this);
        groupId = getIntent().getStringExtra(Constants.ROSTER_USER_ID);
        mApplication = (MApplication) getApplication();
        adapterGroupUsers = new AdapterGroupUsers(this);
        rosterName = mApplication.getStringFromPreference(Constants.USERNAME);

        setAdapter();
        mDialog = new CommonAlertDialog(this);
        mDialog.setOnDialogCloseListener(this);
        listMembers.setOnItemClickListener(this);
        imageUploadTask = new ImageUploadS3(getApplicationContext());
        imageUploadTask.uplodingCallback(this);

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), Constants.TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), Constants.TEMP_PHOTO_FILE_NAME);
        }
        txtGroupStatus.setOnClickListener(this);

        imagePic.setOnClickListener(this);


    }


    /**
     * Sets the adapter.
     */
    private void setAdapter() {
        rosterList.clear();
        try {
            List<Rosters> groupInfo = MDatabaseHelper.getRosterInfo(groupId, String.valueOf(Constants.COUNT_ONE));
            if (!groupInfo.isEmpty()) {
                Rosters item = groupInfo.get(0);
                users = MApplication.returnEmptyStringIfNull(item.getRosterGroupUsers());
                String userList = users.replace(rosterName, "").replace(",,", ",");
                if (userList.startsWith(","))
                    userList = userList.substring(1, userList.length());
                String[] groupUsers = userList.split(",");
                for (String userName : groupUsers) {
                    List<Rosters> userItem = MDatabaseHelper.getRosterInfo(userName,
                            String.valueOf(Constants.COUNT_ZERO));
                    rosterList.add(userItem.get(0));
                }
                String adminUser = MApplication.returnEmptyStringIfNull(item.getRosterAdmin());
                Rosters loggedInRoster = new Rosters();
                if (adminUser.contains(mApplication.getStringFromPreference(Constants.USERNAME))) {
                    viewAddParticipants.setVisibility(View.VISIBLE);
                    loggedInRoster.setRosterAdmin(mApplication.getStringFromPreference(Constants.USERNAME));
                    arrayGroupMenu = getResources().getStringArray(R.array.array_group_user_menu);
                } else {
                    viewAddParticipants.setVisibility(View.GONE);
                    arrayGroupMenu = getResources().getStringArray(R.array.array_group_user_menu_noremove);
                }
                loggedInRoster.setRosterName(mApplication.getStringFromPreference(Constants.USER_PROFILE_NAME));
                loggedInRoster.setRosterID(mApplication.getStringFromPreference(Constants.USERNAME));
                loggedInRoster.setRosterImage(mApplication.getStringFromPreference(Constants.USER_IMAGE));
                loggedInRoster.setRosterStatus(mApplication.getStringFromPreference(Constants.USER_STATUS));

                String groupStatus = MApplication.returnEmptyStringIfNull(item.getRosterGroupStatus());
                if (groupStatus.equalsIgnoreCase(String.valueOf(Constants.COUNT_ZERO))) {
                    txtGroupStatus.setText(getString(R.string.txt_exit_group));
                    isGroupExit = true;
                    rosterList.add(loggedInRoster);
                } else
                    txtGroupStatus.setText(getString(R.string.txt_delete_group));
                adapterGroupUsers.setData(rosterList, adminUser);
                listMembers.setAdapter(adapterGroupUsers);
                txtCount.setText(String.valueOf(rosterList.size()));

                if (rosterList.size() < 3)
                    arrayGroupMenu = getResources().getStringArray(R.array.array_group_user_menu_noremove);
                invalidateOptionsMenu();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_participants:
                startActivityForResult(new Intent(this, ActivityAddRoster.class)
                                .putExtra(Constants.ROSTER_GROUP_USERS, users).putExtra(Constants.ROSTER_USER_ID, groupId),
                        Constants.ACTIVITY_REQ_CODE);
                break;
            case R.id.image:
                showOptions();
                break;
            case R.id.txt_exitgroup:
                exitOrDeleteGroup();
                break;
            default:
                break;
        }
    }

    /**
     * Exit or delete group.
     */
    private void exitOrDeleteGroup() {
        try {
            if (isGroupExit) {
                mDialog.showAlertDialog(Constants.EMPTY_STRING, getString(R.string.txt_are_you_sure_exit),
                        getString(R.string.text_yes), getString(R.string.text_no),
                        CommonAlertDialog.DIALOG_TYPE.DIALOG_DUAL);
                dialogMode = EXIT_GROUP;
            } else {
                mDialog.showAlertDialog(Constants.EMPTY_STRING, getString(R.string.txt_are_you_sure_delete),
                        getString(R.string.text_yes), getString(R.string.text_no),
                        CommonAlertDialog.DIALOG_TYPE.DIALOG_DUAL);
                dialogMode = DELETE_GROUP;
            }
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e);
        }
    }

    /**
     * Show options.
     */
    private void showOptions() {
        Utils mUtils = new Utils();
        mUtils.setOnOptionListener(this);
        mUtils.showImageOptionDialog(this,
                new String[]{getString(R.string.text_choose_gallery), getString(R.string.text_take_photo)},
                Constants.MSG_TYPE_IMAGE);
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
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
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
        if (item.getItemId() == R.id.action_edit) {
            startActivityForResult(new Intent(this, ActivityCommonEditor.class)
                    .putExtra(Constants.TITLE, getString(R.string.text_new_group_name))
                    .putExtra(Constants.TITLE_TYPE, Constants.GROUP_NAME_UPDATE)
                    .putExtra(Constants.MSG_TYPE_TEXT, groupName), Constants.EDIT_REQ_CODE);

        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * (non-Javadoc)
     *
     * @see Utils.OnOptionSelection#onOptionSelected(java.lang.String, int)
     */
    @Override
    public void onOptionSelected(String type, int position) {
        if (position == 0) {
            mApplication.chooseFileFromGallery(this, Constants.MIME_TYPE_IMAGE);
        } else
            mApplication.takePhotoFromCamera(this, Environment.getExternalStorageDirectory() + "/"
                    + getString(R.string.app_name) + "/" + getString(R.string.text_profile_photos), true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Should not show options for currently logged in user
        if (position != rosterList.size() - 1 || !isGroupExit) {
            lastKnownPosition = position;
            mDialog.showListDialog(Constants.EMPTY_STRING, arrayGroupMenu);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see CommonAlertDialog.CommonDialogClosedListener#onDialogClosed(
     * CommonAlertDialog.DIALOG_TYPE, boolean)
     */
    @Override
    public void onDialogClosed(CommonAlertDialog.DIALOG_TYPE dialogType, boolean isSuccess) {
        if (isSuccess)
            switch (dialogMode) {
                case REMOVE_GROUP:
                    removeUserFromGroup();
                    break;
                case EXIT_GROUP:
                    exitFromGroup();
                    break;
                case DELETE_GROUP:
                    deleteGroup();
                    break;
                default:
                    break;
            }
    }

    /**
     * Removes the user from group.
     */
    private void removeUserFromGroup() {
        if (mApplication.isNetConnected(this)) {
            progressDialog = new DoProgressDialog(this);
            progressDialog.showProgress();
            Intent mIntent = new Intent(this, ChatService.class);
            mIntent.putExtra("groupId", groupId);
            mIntent.putExtra("groupMember", rosterList.get(lastKnownPosition).getRosterID());
            mIntent.setAction(ContusConstantValues.CONTUS_XMPP_REMOVE_GROUP_MEMBER);
            startService(mIntent);
        } else
            CustomToast.showToast(this, getString(R.string.text_no_internet));
    }

	/*
     * (non-Javadoc)
	 *
	 * @see
	 * com.contus.activities.ActivityBase#groupMemberRemoved(java.lang.String)
	 */

    /**
     * Group member removed.
     *
     * @param result the result
     */
    @Override
    protected void groupMemberRemoved(String result) {
        super.groupMemberRemoved(result);
        getFreshGroupInfo();
    }

    /*
     * (non-Javadoc)
     *
     * @see CommonAlertDialog.CommonDialogClosedListener#listOptionSelected(int)
     */
    @Override
    public void listOptionSelected(int position) {
        try {
            switch (position) {
                case 0:
                    openChatView();
                    break;
                case 1:
                    openInfoView();
                    break;
                case 2:
                    showConfirmDialog();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e);
        }
    }

    /**
     * Open chat view.
     */
    private void openChatView() {
        startActivity(new Intent(this, ActivityChatView.class)
                .putExtra(Constants.ROSTER_USER_ID, rosterList.get(lastKnownPosition).getRosterID())
                .putExtra(Constants.CHAT_TYPE, String.valueOf(Constants.CHAT_TYPE_SINGLE))
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    /**
     * Open info view.
     */
    private void openInfoView() {
        startActivity(new Intent(this, ActivityUserProfile.class).putExtra(Constants.ROSTER_USER_ID,
                rosterList.get(lastKnownPosition).getRosterID()));
    }

    /**
     * Show confirm dialog.
     */
    private void showConfirmDialog() {
        Rosters item = rosterList.get(lastKnownPosition);
        dialogMode = REMOVE_GROUP;
        mDialog.showAlertDialog(Constants.EMPTY_STRING,
                getString(R.string.txt_are_you_sure_remove) + " " + item.getRosterName() + "?", getString(R.string.text_yes),
                getString(R.string.text_no), CommonAlertDialog.DIALOG_TYPE.DIALOG_DUAL);
    }

    /**
     * On activity result.
     *
     * @param requestCode the request code
     * @param resultCode  the result code
     * @param data        the data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case Constants.ACTIVITY_REQ_CODE:
                        setAdapter();
                        break;
                    case Constants.EDIT_REQ_CODE:
                        updateGroupInfo(data.getStringExtra(Constants.TITLE), groupImage);
                        break;
                    case Constants.TAKE_PHOTO:
                        Utils.cropImage(this, mFileTemp);
                        break;
                    case Constants.FROM_GALLERY:
                        handleGalleryIntent(data);
                        break;
                    case Constants.CROP_IMAGE:
                        uploadImage();
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e);
        }
    }

    /**
     * @param data Intent value
     */
    private void handleGalleryIntent(Intent data) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(data.getData());
            FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
            Utils.copyStream(inputStream, fileOutputStream);
            fileOutputStream.close();
            inputStream.close();
            Utils.cropImage(this, mFileTemp);
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ActivityBase#newGroupCreated()
     */
    @Override
    protected void newGroupCreated() {
        super.newGroupCreated();
        if (progressDialog != null)
            progressDialog.dismiss();
        setAdapter();
    }

    /**
     * Upload image.
     */
    private void uploadImage() {
        try {
            if (mApplication.isNetConnected(this)) {
                Bitmap photo = BitmapFactory.decodeFile(mFileTemp.getPath());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 0, stream);
                progressDialog = new DoProgressDialog(this);
                progressDialog.showProgress();
                imageUploadTask.executeUpload(mFileTemp, Constants.MSG_TYPE_IMAGE, "","POLLS/");
            } else
                CustomToast.showToast(this, getString(R.string.text_no_internet));
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e);
        }
    }

    /**
     * Update group info.
     *
     * @param name   the name
     * @param imgUrl the img url
     */
    private void updateGroupInfo(String name, String imgUrl) {
        try {
            if (mApplication.isNetConnected(this)) {
                tempName = name;
                tempImage = imgUrl;
                startService(new Intent(this, ChatService.class)
                        .putExtra("groupName", Utils.getEncodedString(name))
                        .putExtra("groupimage", imgUrl).putExtra("groupId", groupId)
                        .setAction(ContusConstantValues.CONTUS_XMPP_UPDATE_GROUP_INFO));
            } else
                CustomToast.showToast(this, getString(R.string.text_no_internet));
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e);
        }
    }

    /**
     * Gets the fresh group info.
     *
     * @return the fresh group info
     */
    private void getFreshGroupInfo() {
        startService(new Intent(this, ChatService.class).putExtra("groupId", groupId)
                .setAction(ContusConstantValues.CONTUS_XMPP_GET_GROUP_INFO));
    }

    /*
     * (non-Javadoc)
     *
     * @see ActivityBase#groupInfoUpdated(java.lang.String)
     */
    @Override
    protected void groupInfoUpdated(String response) {
        super.groupInfoUpdated(response);
        groupName = tempName;
        groupImage = tempImage;
        setToolbarTitle();
        Utils.loadImageWithGlide(this, groupImage, imagePic, R.drawable.ic_grp_bg);
    }

    /**
     * Sets the toolbar title.
     */
    private void setToolbarTitle() {
        try {
            groupName = Utils.getDecodedString(groupName);
            toolbarLayout.setTitle(groupName);
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e);
        }
    }

    /**
     * On task completed.
     *
     * @param url the url
     * @param s   the s
     * @param s1  the s1
     * @param i   the i
     */
    @Override
    public void onTaskCompleted(URL url, String s, String s1, int i) {
        if (progressDialog != null)
            progressDialog.dismiss();
        if (url != null)
            tempImage = url.toString();
        updateGroupInfo(groupName, tempImage);
    }

	/*
     * (non-Javadoc)
	 *
	 * @see
	 * com.contus.activities.ActivityBase#groupExitResponse(java.lang.String)
	 */

    /**
     * Group exit response.
     *
     * @param response the response
     */
    protected void groupExitResponse(String response) {
        isGroupExit = false;
        txtGroupStatus.setText(getString(R.string.txt_delete_group));
        getFreshGroupInfo();
    }

    /**
     * Exit from group.
     */
    private void exitFromGroup() {
        try {
            if (mApplication.isNetConnected(this)) {
                progressDialog = new DoProgressDialog(this);
                progressDialog.showProgress();
                startService(new Intent(this, ChatService.class).putExtra("groupId", groupId)
                        .setAction(ContusConstantValues.CONTUS_XMPP_EXIT_GROUP));
            } else
                CustomToast.showToast(this, getString(R.string.text_no_internet));
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e);
        }
    }

    /**
     * Delete group.
     */
    private void deleteGroup() {
        try {
            progressDialog = new DoProgressDialog(this);
            progressDialog.showProgress();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    if (MDatabaseHelper.checkIDStatus(groupId, Constants.TABLE_RECENT_CHAT_DATA, Constants.RECENT_CHAT_ID))
                        MDatabaseHelper.deleteRecord(Constants.TABLE_RECENT_CHAT_DATA, Constants.RECENT_CHAT_ID + "=?",
                                new String[]{groupId});
                    if (MDatabaseHelper.checkIDStatus(groupId, Constants.TABLE_ROSTER, Constants.ROSTER_USER_ID))
                        MDatabaseHelper.deleteRecord(Constants.TABLE_ROSTER, Constants.ROSTER_USER_ID + "=?",
                                new String[]{groupId});
                    setResult(RESULT_FIRST_USER);
                    finish();
                }
            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * On prepare options menu.
     *
     * @param menu the menu
     *
     * @return true, if successful
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isGroupExit) {
            menu.getItem(0).setVisible(true);
            imagePic.setOnClickListener(this);
        } else {
            menu.getItem(0).setVisible(false);
            imagePic.setOnClickListener(null);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
