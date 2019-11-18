package com.contus.publicpoll;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.responsemodel.CampaignCommentDelete;
import com.contus.responsemodel.CommentDisplayResponseModel;
import com.contus.restclient.CampaignCommentEditDeleteRestClient;
import com.contusfly.MApplication;
import com.contusfly.utils.Utils;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/8/2015.
 */
public class CampaignCommentsAdapter extends ArrayAdapter<CommentDisplayResponseModel.Results.Data> {
    //Dialog
    private Dialog campaignCommentsCustomDialog;
    //edit text
    private EditText editCampaignComments;
    //Button delete,save
    private Button txtCampaignCommentsDelete, txtCampaignCommnetsSave;
    //Comments count
    private final int commentsCountPosition;
    //Dataresults from the response
    private List<CommentDisplayResponseModel.Results.Data> dataResults;
    //Casmapingid
    private  String campaignId;
    //user id
    private String userId;
    //Context
    private Activity campaignPollCommentsContext;
    //comment layout
    private final int campaignCommentsPollLayout;
    //user profile
    private String campaignPollUserProfile;
    //Uri
    private Uri uri;
    //Data fromt he response
    private CommentDisplayResponseModel.Results.Data campaignPollCommentsData;
    //Campaign COMMENTS
    private String mCampaignComments;
    //View holder
    private ViewHolder holder;
    //Campaign poll comemnts count
    private ArrayList<Integer> campaignPollcommentsCount = new ArrayList<Integer>();
    //Comments count from the arraylist
    private ArrayList<Integer> prefrenceCampaignPollCommentsCount;
    //Comments count
    private ArrayList<Integer> prefrenceCommentsCount;
    //campaign COMMENTS count
    private ArrayList<Integer> campaigncommentsCount = new ArrayList<Integer>();

    /**
     * initializes a new instance of the ListView class.
     * @param activity
     * @param dataResults
     * @param layoutId
     * @param totalComments
     * @param commentsCountPosition
     * @param campaignId
     * @param userId
     */
    public CampaignCommentsAdapter(Activity activity, List<CommentDisplayResponseModel.Results.Data> dataResults, int layoutId, String totalComments, int commentsCountPosition, String campaignId, String userId) {
        super(activity, 0, dataResults);
        this.campaignCommentsPollLayout = layoutId;
        this.campaignPollCommentsContext = activity;
        this.commentsCountPosition = commentsCountPosition;
        this.dataResults=dataResults;
        this.campaignId=campaignId;
        this.userId=userId;
    }

    @Override
    public View getView(final int position, View mCampaignCommentsView, ViewGroup parent) {
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        LayoutInflater mInflater = (LayoutInflater)
                campaignPollCommentsContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // The old view to reuse, if possible.If convertView is NOT null, we can simple re-use the convertView as the new View.
        // It will happen when a new row appear and a old row in the other end roll out.
        if (mCampaignCommentsView == null) {
             /* create a new view of my layout and inflate it in the row */
            mCampaignCommentsView = mInflater.inflate(campaignCommentsPollLayout, parent, false);
            holder = new ViewHolder();
            holder.campaignImage = (ImageView) mCampaignCommentsView.findViewById(R.id.imgProfile);
            holder.userName = (TextView) mCampaignCommentsView.findViewById(R.id.txtProfileName);
            holder.comments = (TextView) mCampaignCommentsView.findViewById(R.id.txtChatDetails);
            holder.imgMore = (ImageView) mCampaignCommentsView.findViewById(R.id.imgMore);
            holder.txtCommentTime=(TextView)mCampaignCommentsView.findViewById(R.id.txtCommentTime);
            mCampaignCommentsView.setTag(holder);
        } else {
            holder = (ViewHolder) mCampaignCommentsView.getTag();
        }
        //Geting the particular object from the response based on the position
        campaignPollCommentsData = getItem(position);
        //If the user id matches from the response then the edit icon will be visible
        //else edit icon wont be visivble
        if (campaignPollCommentsData.getUserId().equals(MApplication.getString(campaignPollCommentsContext,Constants.USER_ID).trim())) {
            Log.e("check", MApplication.getString(campaignPollCommentsContext,Constants.USER_ID).trim());
            Log.e("check",campaignPollCommentsData.getUserId());
            holder.imgMore.setVisibility(View.VISIBLE);
        } else {
            Log.e("check1", MApplication.getString(campaignPollCommentsContext,Constants.USER_ID).trim());
            Log.e("check1",campaignPollCommentsData.getUserId());
            holder.imgMore.setVisibility(View.INVISIBLE);
        }
        //Getting the user profiel image
        campaignPollUserProfile = campaignPollCommentsData.getUserInfo().getUserProfileImg();
       //Set the profiel image from the resposne
        Utils.loadImageWithGlideProfileRounderCorner(campaignPollCommentsContext,campaignPollUserProfile,holder.campaignImage,R.drawable.icon_profile);
        //Getting the user name from the response
        holder.userName.setText(campaignPollCommentsData.getUserInfo().getUserName());
        //Getting the COMMENTS and setting in the text view
        holder.comments.setText(MApplication.getDecodedString(campaignPollCommentsData.getComments()));
        //comment time
        holder.txtCommentTime.setText(MApplication.getHours(campaignPollCommentsData.getUpdatedAt()));
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clcik position
                int clickPosition = position;
                //Geting the particular object from the response based on the position
                campaignPollCommentsData = getItem(clickPosition);
                //Comments from the response
                String comments = MApplication.getDecodedString(campaignPollCommentsData.getComments());
                //COMMENTS id from the response
                String commentId = MApplication.getDecodedString(campaignPollCommentsData.getId());
                //Custom dialog to edit and delete the comment
                customCampaignPollCustomDialog(campaignPollCommentsContext, comments, clickPosition, commentId);
            }
        });
        //returning the views
        return mCampaignCommentsView;
    }

    /**
     * A ViewHolder object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder {
        //Text view COMMENTS
        private TextView comments;
        //Text view user name
        private TextView userName;
        //Campaign Image
        private ImageView campaignImage;
        //Image view
        private ImageView imgMore;
        public TextView txtCommentTime;
    }


    /**
     * customCampaignPollCustomDialog
     * @param mActivity
     * @param comments
     * @param position
     * @param commentId
     */

    public void customCampaignPollCustomDialog(final Activity mActivity, String comments, final int position, final String commentId) {
        //Creates a dialog window that uses the default dialog theme.
        campaignCommentsCustomDialog = new Dialog(mActivity);
       //Enable extended window features.
        campaignCommentsCustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       //Change the background of this window to a custom Drawable.
        campaignCommentsCustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Set the screen content to an explicit view.
        campaignCommentsCustomDialog.setContentView(R.layout.layout_edit_comments);
        //Edit text
        editCampaignComments = (EditText) campaignCommentsCustomDialog.findViewById(R.id.editComments);
        //Setting the comment text from the response in edit text
        editCampaignComments.setText(comments);
        //Delte button
        txtCampaignCommentsDelete =(Button) campaignCommentsCustomDialog.findViewById(R.id.delete);
        //Save buitton
        txtCampaignCommnetsSave =(Button) campaignCommentsCustomDialog.findViewById(R.id.Save);
        //Start the dialog and display it on screen.
        campaignCommentsCustomDialog.show();
        //Register a callback to be invoked when this view is clicked.
        txtCampaignCommentsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User input text is stored into the varaible
                mCampaignComments = MApplication.getEncodedString(editCampaignComments.getText().toString().trim());
                //If it is empty  toast message will display to enter the text
                //else request to delete the COMMENTS
                if (TextUtils.isEmpty(mCampaignComments)) {
                    Toast.makeText(campaignPollCommentsContext, campaignPollCommentsContext.getResources().getString(R.string.enter_the_comments), Toast.LENGTH_SHORT).show();
                } else {
                    campaignCommentsDelete(position, commentId, campaignCommentsCustomDialog);
                }
            }
        });

        //Register a callback to be invoked when this view is clicked.
        txtCampaignCommnetsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //User input text is stored into the varaible
                mCampaignComments = MApplication.getEncodedString(editCampaignComments.getText().toString().trim());
                //If it is empty  toast message will display to enter the text
                //else request to delete the COMMENTS
                if (TextUtils.isEmpty(mCampaignComments)) {
                    Toast.makeText(campaignPollCommentsContext, campaignPollCommentsContext.getResources().getString(R.string.enter_the_comments),
                            Toast.LENGTH_SHORT).show();
                } else {
                    campaignCommentsSave(position, commentId, campaignCommentsCustomDialog);
                }
            }
        });

    }

    /**
     * campaignCommentsDelete
     * @param clickPosition
     * @param commentId
     * @param list_dialog
     */
    private void campaignCommentsDelete(final int clickPosition, String commentId,final Dialog list_dialog) {
        //Material dialog starts
        MApplication.materialdesignDialogStart(campaignPollCommentsContext);
        /**  Requesting the response from the given base url**/
        CampaignCommentEditDeleteRestClient.getInstance().postCampaignCommentDelete(new String("delete_comments"), new String(campaignId), new String(userId), new String(commentId)
                , new Callback<CampaignCommentDelete>() {
            @Override
            public void success(CampaignCommentDelete campaignCommentDelete, Response response) {
                //If the succes matches with the value 1 then the corresponding row is removed
                //else corresponding row wont remove
                if (("1").equals(campaignCommentDelete.getSuccess())) {
                    //remove the particular cell
                    dataResults.remove(clickPosition);
                    //Prefernce comment count is stored in array list
                    prefrenceCommentsCount = MApplication.loadArray(campaignPollCommentsContext, campaigncommentsCount, Constants.CAMPAIGN_COMMENTS_COUNT, Constants.CAMPAIGN_COMMENTS_SIZE);
                   //Changing the particular position value
                    prefrenceCommentsCount.set(commentsCountPosition, Integer.valueOf(campaignCommentDelete.getCount()));
                  //Saving again into the prefernce
                    MApplication.saveArray(campaignPollCommentsContext, prefrenceCommentsCount, Constants.CAMPAIGN_COMMENTS_COUNT, Constants.CAMPAIGN_COMMENTS_SIZE);
                   //Notifies the attached observers that the underlying data has been changed
                   // and any View reflecting the data set should refresh itself.
                    notifyDataSetChanged();
                    //dismiss the dialog
                    list_dialog.dismiss();
                }
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                //Error message popups when the user cannot able to coonect with the server
                MApplication.errorMessage(retrofitError, campaignPollCommentsContext);
            }

        });
        //Progress bar stops
        MApplication.materialdesignDialogStop();
    }

    /**
     * This method is used to save the comment
     * @param position
     * @param commentId
     * @param listDialog
     */

    private void campaignCommentsSave(final int position, String commentId, final Dialog listDialog) {
        //Material dialog starts
        MApplication.materialdesignDialogStart(campaignPollCommentsContext);
            /**  Requesting the response from the given base url**/
            CampaignCommentEditDeleteRestClient.getInstance().postCampaignCommentEdit(new String("edit_comments"), new String(campaignId), new String(userId), new String(commentId), new String(mCampaignComments)
                    , new Callback<CommentDisplayResponseModel>() {
                @Override
                public void success(CommentDisplayResponseModel campaignCommentEdit, Response response) {
                   //If the succes matches with the value 1 then the corresponding row is modified
                    //else corresponding row wont remove
                    if (("1").equals(campaignCommentEdit.getSuccess())) {
                        listDialog.dismiss();
                        dataResults.set(position, campaignCommentEdit.getResults().getData().get(0));
                        //Prefernce comment count is stored in array list
                        prefrenceCampaignPollCommentsCount= MApplication.loadArray(campaignPollCommentsContext,campaignPollcommentsCount,Constants.CAMPAIGN_COMMENTS_COUNT,Constants.CAMPAIGN_COMMENTS_SIZE);
                        //Changing the particular position value
                        prefrenceCampaignPollCommentsCount.set(commentsCountPosition, Integer.valueOf(campaignCommentEdit.getCount()));
                        //Saving again into the prefernce
                        MApplication.saveArray(campaignPollCommentsContext, prefrenceCampaignPollCommentsCount, Constants.CAMPAIGN_COMMENTS_COUNT, Constants.CAMPAIGN_COMMENTS_SIZE);
                        //Notifies the attached observers that the underlying data has been changed
                        // and any View reflecting the data set should refresh itself.
                        notifyDataSetChanged();
                    }
                }
                @Override
                public void failure(RetrofitError retrofitError) {
                    //Error message popups when the user cannot able to coonect with the server
                    MApplication.errorMessage(retrofitError, campaignPollCommentsContext);
                }

            });
        //Progress bar stops
        MApplication.materialdesignDialogStop();
    }

}