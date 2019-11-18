package com.contus.campaignparticipate;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
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
public class CampignPollCommentsAdapter extends ArrayAdapter<CommentDisplayResponseModel.Results.Data> {
    //Dialog
    private Dialog campaignPollCommentsCustomDialog;
    //edit text
    private EditText editCampaignPollComments;
    //Button delete,save
    private Button txtCampaignPollCommentsDelete, txtCampaignPollCommentsSave;
    //Comments count
    private final int commentsClickPosition;
    //Dataresults from the response
    private List<CommentDisplayResponseModel.Results.Data> campaignPOllCommentsResults;
    //Casmapingid
    private String pollId;
    //Context
    private Activity camapignPollCommentsContext;
    //comment layout
    private final int campaignCommentsPollLayout;
    //user profile
    private String campaign;
    //Data fromt he response
    private  CommentDisplayResponseModel.Results.Data camapignPollCommentsDataResults;
    //Campaign COMMENTS
    private  String mCampaignPollComment;
    //View holder
    private ViewHolder holder;
    //Campaign poll comemnts count
    private ArrayList<Integer> campaignPollcommentsCount = new ArrayList<Integer>();
    //Comments count from the arraylist
    private  ArrayList<Integer> prefrenceCampaignPollCommentsCount;
    //view
    private View mCampaignCommentsPollView;


    public CampignPollCommentsAdapter(Activity activity, List<CommentDisplayResponseModel.Results.Data> dataResults, int layoutId, String id,int commentsCountPosition) {
        super(activity, 0, dataResults);
        this.campaignCommentsPollLayout = layoutId;
        this.camapignPollCommentsContext = activity;
        this.campaignPOllCommentsResults =dataResults;
        this.pollId=id;
        this.commentsClickPosition =commentsCountPosition;
    }


    @Override
    public View getView(final int position, View mView, ViewGroup parent) {
        mCampaignCommentsPollView=mView;
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        LayoutInflater mInflater = (LayoutInflater)
                camapignPollCommentsContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // The old view to reuse, if possible.If convertView is NOT null, we can simple re-use the convertView as the new View.
        // It will happen when a new row appear and a old row in the other end roll out.
        if (mCampaignCommentsPollView == null) {
             /* create a new view of my layout and inflate it in the row */
            mCampaignCommentsPollView = mInflater.inflate(campaignCommentsPollLayout, parent, false);
            holder = new ViewHolder();
            holder.userImage = (ImageView) mCampaignCommentsPollView.findViewById(R.id.imgProfile);
            holder.userName = (TextView) mCampaignCommentsPollView.findViewById(R.id.txtProfileName);
            holder.comments = (TextView) mCampaignCommentsPollView.findViewById(R.id.txtChatDetails);
            holder.imgMore = (ImageView) mCampaignCommentsPollView.findViewById(R.id.imgMore);
            holder.txtCommentTime=(TextView)mCampaignCommentsPollView.findViewById(R.id.txtCommentTime);
            mCampaignCommentsPollView.setTag(holder);
        } else {
            holder = (ViewHolder) mCampaignCommentsPollView.getTag();
        }
        //Geting the particular object from the response based on the position
        camapignPollCommentsDataResults = getItem(position);
        //If the user id matches from the response then the edit icon will be visible
        //else edit icon wont be visivble
        if (camapignPollCommentsDataResults.getUserId().equals(MApplication.getString(camapignPollCommentsContext, Constants.USER_ID))) {
            holder.imgMore.setVisibility(View.VISIBLE);
        } else {
            holder.imgMore.setVisibility(View.INVISIBLE);
        }
        //Getting the user profiel image
        campaign = camapignPollCommentsDataResults.getUserInfo().getUserProfileImg();
        //Set the profiel image from the resposne
        Utils.loadImageWithGlideProfileRounderCorner(camapignPollCommentsContext,campaign,holder.userImage,R.drawable.placeholder_image);
        //Getting the user name from the response
        holder.userName.setText(camapignPollCommentsDataResults.getUserInfo().getUserName());
        //Getting the COMMENTS and setting in the text view
        holder.comments.setText(MApplication.getDecodedString(camapignPollCommentsDataResults.getComments()));
        //comment time
        holder.txtCommentTime.setText(MApplication.getHours(camapignPollCommentsDataResults.getUpdatedAt()));
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clcik position
                int clickPosition = position;
                //Geting the particular object from the response based on the position
                camapignPollCommentsDataResults = getItem(clickPosition);
                //Comments from the response
                String comments = MApplication.getDecodedString(camapignPollCommentsDataResults.getComments());
                //COMMENTS id from the response
                String commentId = MApplication.getDecodedString(camapignPollCommentsDataResults.getId());
                //Custom dialog to edit and delete the comment
                customCampaignPollCustomDialog(camapignPollCommentsContext, comments, clickPosition, commentId);
            }
        });
        //returning the views
        return mCampaignCommentsPollView;
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
        private ImageView userImage;
        //Image view
        private ImageView imgMore;
        //textview
        private TextView txtCommentTime;
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
        campaignPollCommentsCustomDialog = new Dialog(mActivity);
        //Enable extended window features.
        campaignPollCommentsCustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Change the background of this window to a custom Drawable.
        campaignPollCommentsCustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Set the screen content to an explicit view.
        campaignPollCommentsCustomDialog.setContentView(R.layout.layout_edit_comments);
        //Edit text
        editCampaignPollComments = (EditText) campaignPollCommentsCustomDialog.findViewById(R.id.editComments);
        //Setting the comment text from the response in edit text
        editCampaignPollComments.setText(comments);
        //Delte button
        txtCampaignPollCommentsDelete =(Button) campaignPollCommentsCustomDialog.findViewById(R.id.delete);
        //Save buitton
        txtCampaignPollCommentsSave =(Button) campaignPollCommentsCustomDialog.findViewById(R.id.Save);
        //Start the dialog and display it on screen.
        campaignPollCommentsCustomDialog.show();
        //Register a callback to be invoked when this view is clicked.
        txtCampaignPollCommentsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User input text is stored into the varaible
                mCampaignPollComment = MApplication.getEncodedString(editCampaignPollComments.getText().toString().trim());
                //If it is empty  toast message will display to enter the text
                //else request to delete the COMMENTS
                if (TextUtils.isEmpty(mCampaignPollComment)) {
                    Toast.makeText(camapignPollCommentsContext, camapignPollCommentsContext.getResources().getString(R.string.enter_the_comments), Toast.LENGTH_SHORT).show();
                } else {

                    campaignCommentsDelete(position, commentId, campaignPollCommentsCustomDialog);
                }
            }
        });

        //Register a callback to be invoked when this view is clicked.
        txtCampaignPollCommentsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User input text is stored into the varaible
                mCampaignPollComment = MApplication.getEncodedString(editCampaignPollComments.getText().toString().trim());
                //If it is empty  toast message will display to enter the text
                //else request to delete the COMMENTS
                if (TextUtils.isEmpty(mCampaignPollComment)) {
                    Toast.makeText(camapignPollCommentsContext, camapignPollCommentsContext.getResources().getString(R.string.enter_the_comments),
                            Toast.LENGTH_SHORT).show();
                } else {
                    campaignCommentsSave(position, commentId, campaignPollCommentsCustomDialog);
                }
            }
        });

    }

    /**
     * campaignCommentsDelete
     * @param clickPosition
     * @param commentId
     * @param listDialog
     */
    private void campaignCommentsDelete(final int clickPosition, String commentId, final Dialog listDialog) {
        MApplication.materialdesignDialogStart(camapignPollCommentsContext);   //Material dialog starts
        CampaignCommentEditDeleteRestClient.getInstance().postCampaignPollCommentDelete(new String("delete_poll"), new String(pollId), new String(MApplication.getString(camapignPollCommentsContext, Constants.USER_ID)), new String(commentId), new Callback<CampaignCommentDelete>() {/**  Requesting the response from the given base url**/
            @Override
            public void success(CampaignCommentDelete campaignCommentDelete, Response response) {
                if (("1").equals(campaignCommentDelete.getSuccess())) { //If the succes matches with the value 1 then the corresponding row is removed
                    //else corresponding row wont remove
                    campaignPOllCommentsResults.remove(clickPosition); //remove the particular cell
                    prefrenceCampaignPollCommentsCount = MApplication.loadArray(camapignPollCommentsContext, campaignPollcommentsCount, Constants.CAMPAIGN_POLL_COMMENTS_COUNT, Constants.CAMPAIGN_POLL_COMMENTS_SIZE);     //Prefernce comment count is stored in array list
                    prefrenceCampaignPollCommentsCount.set(commentsClickPosition, Integer.valueOf(campaignCommentDelete.getCount()));     //Changing the particular position value
                    MApplication.saveArray(camapignPollCommentsContext, prefrenceCampaignPollCommentsCount, Constants.CAMPAIGN_POLL_COMMENTS_COUNT, Constants.CAMPAIGN_POLL_COMMENTS_SIZE);  //Saving again into the prefernce
                    notifyDataSetChanged();  //Notifies the attached observers that the underlying data has been changed
                    // and any View reflecting the data set should refresh itself.
                    listDialog.dismiss();   //dismiss the dialog
                }
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                MApplication.errorMessage(retrofitError, camapignPollCommentsContext); //Error message popups when the user cannot able to coonect with the server
                }
        });
        MApplication.materialdesignDialogStop(); //Progress bar stops
    }

    /**
     * This method is used to save the comment
     * @param position
     * @param commentId
     * @param listDialog
     */

    private void campaignCommentsSave(final int position, String commentId, final Dialog listDialog) {
        MApplication.materialdesignDialogStart(camapignPollCommentsContext);  //Material dialog starts
        CampaignCommentEditDeleteRestClient.getInstance().postCampaignPollCommentEdit(new String("edit_poll"), new String(pollId), new String(MApplication.getString(camapignPollCommentsContext, Constants.USER_ID)), new String(commentId), new String(mCampaignPollComment), new Callback<CommentDisplayResponseModel>() {
            @Override
            public void success(CommentDisplayResponseModel campaignCommentEdit, Response response) {
                //If the succes matches with the value 1 then the corresponding row is modified
                //else corresponding row wont remove
                if (("1").equals(campaignCommentEdit.getSuccess())) {
                    listDialog.dismiss();
                    campaignPOllCommentsResults.set(position, campaignCommentEdit.getResults().getData().get(0));
                    prefrenceCampaignPollCommentsCount = MApplication.loadArray(camapignPollCommentsContext, campaignPollcommentsCount, Constants.CAMPAIGN_POLL_COMMENTS_COUNT, Constants.CAMPAIGN_POLL_COMMENTS_SIZE);   //Prefernce comment count is stored in array list
                    prefrenceCampaignPollCommentsCount.set(commentsClickPosition, Integer.valueOf(campaignCommentEdit.getCount()));     //Changing the particular position value
                    MApplication.saveArray(camapignPollCommentsContext, prefrenceCampaignPollCommentsCount, Constants.CAMPAIGN_POLL_COMMENTS_COUNT, Constants.CAMPAIGN_POLL_COMMENTS_SIZE);  //Saving again into the prefernce
                    notifyDataSetChanged();  //Notifies the attached observers that the underlying data has been changed
                    // and any View reflecting the data set should refresh itself.
                }
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                MApplication.errorMessage(retrofitError, camapignPollCommentsContext);   //Error message popups when the user cannot able to coonect with the server
            }
        });
        MApplication.materialdesignDialogStop();//Progress bar stops
    }
}