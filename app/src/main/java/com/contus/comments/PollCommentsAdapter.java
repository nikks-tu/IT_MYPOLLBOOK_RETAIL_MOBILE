package com.contus.comments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/8/2015.
 */
public class PollCommentsAdapter extends ArrayAdapter<CommentDisplayResponseModel.Results.Data> {
    //Dialog
    private  Dialog listDialog;
    //edit text
    private  EditText editCampaignComments;
    //Button delete,save
    private  Button txtCampaignCommentsDelete, txtCampaignCommnetsSave;
    //Comments count
    private final int commentsCountPosition;
    //Dataresults from the response
    private  List<CommentDisplayResponseModel.Results.Data> dataResults;
    //Casmapingid
    private  String pollId;
    //user id
    private  String userId;
    //Context
    private Activity pollCommentsAdapterContext;
    //comment layout
    private final int pollCommentsLayoutId;
    //user profile
    private String pollCommentsUserProfile;
    //Data fromt he response
    private  CommentDisplayResponseModel.Results.Data pollCommentsData;
    //Campaign COMMENTS
    private  String mCampaignComments;
    //View holder
    private ViewHolder holder;
    //Campaign poll comemnts count
    private ArrayList<Integer> userPollcommentsCount = new ArrayList<Integer>();
    //Comments count from the arraylist
    private ArrayList<Integer> prefrenceUserPollCommentsCount;
    //view
    private View mView;


    /**
     * initializes a new instance of the ListView class.
     * @param activity
     * @param dataResults
     * @param layoutId
     * @param commentsCountPosition
     * @param userId
     */
    public PollCommentsAdapter(Activity activity, List<CommentDisplayResponseModel.Results.Data> dataResults, int layoutId, String id, String userId, int commentsCountPosition) {
        super(activity, 0, dataResults);
        this.pollCommentsLayoutId = layoutId;
        this.pollCommentsAdapterContext = activity;
        this.dataResults=dataResults;
        this.pollId=id;
        this.userId=userId;
        this.commentsCountPosition=commentsCountPosition;
    }

    @Override
    public View getView(final int position, View mCommentsView, ViewGroup parent) {
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        LayoutInflater mInflater = (LayoutInflater)
                pollCommentsAdapterContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mView=mCommentsView;
        // The old view to reuse, if possible.If convertView is NOT null, we can simple re-use the convertView as the new View.
        // It will happen when a new row appear and a old row in the other end roll out.
        if (mView == null) {
             /* create a new view of my layout and inflate it in the row */
            mView = mInflater.inflate(pollCommentsLayoutId, parent, false);
            holder = new ViewHolder();
            holder.campaignImage = (ImageView) mView.findViewById(R.id.imgProfile);
            holder.userName = (TextView) mView.findViewById(R.id.txtProfileName);
            holder.comments = (TextView) mView.findViewById(R.id.txtChatDetails);
            holder.imgMore = (ImageView) mView.findViewById(R.id.imgMore);
            holder.txtCommentTime=(TextView)mView.findViewById(R.id.txtCommentTime);
            mView.setTag(holder);
        } else {
            holder = (ViewHolder) mView.getTag();
        }
        //Geting the particular object from the response based on the position
        pollCommentsData = getItem(position);
        //If the user id matches from the response then the edit icon will be visible
        //else edit icon wont be visivble
        if (pollCommentsData.getUserId().equals(MApplication.getString(pollCommentsAdapterContext, Constants.USER_ID))) {
            holder.imgMore.setVisibility(View.VISIBLE);
        } else {
            holder.imgMore.setVisibility(View.INVISIBLE);
        }
        if(pollCommentsData.getUserInfo()!=null)
        {
            //Getting the user profiel image
            pollCommentsUserProfile = pollCommentsData.getUserInfo().getUserProfileImg();
            //Set the profiel image from the resposne
            Utils.loadImageWithGlideProfileRounderCorner(pollCommentsAdapterContext,pollCommentsUserProfile,holder.campaignImage,R.drawable.placeholder_image);
            //Getting the user name from the response
            holder.userName.setText(pollCommentsData.getUserInfo().getUserName());

        }
        //Getting the COMMENTS and setting in the text view
        holder.comments.setText(MApplication.getDecodedString(pollCommentsData.getComments()));
        //comment time

        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/M/yyyy hh:mm");
        try {
            Date date1 = simpleDateFormat.parse(pollCommentsData.getUpdatedAt().toString());
            holder.txtCommentTime.setText(date1.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            holder.txtCommentTime.setText(MApplication.getHours(pollCommentsData.getUpdatedAt()));
        }

        //String a=simpleDateFormat.applyPattern(pollCommentsData.getUpdatedAt().toString());

       // holder.txtCommentTime.setText(MApplication.getHours(pollCommentsData.getUpdatedAt()));
      //  holder.txtCommentTime.setText(simpleDateFormat.applyPattern(pollCommentsData.getUpdatedAt().toString()));
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clcik position
                int clickPosition = position;
                //Geting the particular object from the response based on the position
                pollCommentsData = getItem(clickPosition);
                //Comments from the response
                String comments = MApplication.getDecodedString(pollCommentsData.getComments());
                //COMMENTS id from the response
                String commentId = MApplication.getDecodedString(pollCommentsData.getId());
                //Custom dialog to edit and delete the comment
                customCampaignPollCustomDialog(pollCommentsAdapterContext, comments, clickPosition, commentId);
            }
        });
        //returning the views
        return mView;
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
        //txt comment time
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
        listDialog = new Dialog(mActivity);
        //Enable extended window features.
        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Change the background of this window to a custom Drawable.
        listDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Set the screen content to an explicit view.
        listDialog.setContentView(R.layout.layout_edit_comments);
        //Edit text
        editCampaignComments = (EditText) listDialog.findViewById(R.id.editComments);
        Log.e("comments",comments.toString());
        //Setting the comment text from the response in edit text
        editCampaignComments.setText(comments);
        //Delte button
        txtCampaignCommentsDelete =(Button) listDialog.findViewById(R.id.delete);
        //Save buitton
        txtCampaignCommnetsSave =(Button) listDialog.findViewById(R.id.Save);
        //Start the dialog and display it on screen.
        listDialog.show();
        //Register a callback to be invoked when this view is clicked.
        txtCampaignCommentsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User input text is stored into the varaible
                mCampaignComments = MApplication.getEncodedString(editCampaignComments.getText().toString().trim());
                //If it is empty  toast message will display to enter the text
                //else request to delete the COMMENTS
                if (TextUtils.isEmpty(mCampaignComments)) {
                    Toast.makeText(pollCommentsAdapterContext, pollCommentsAdapterContext.getResources().getString(R.string.enter_the_comments), Toast.LENGTH_SHORT).show();
                } else {
                    campaignCommentsDelete(position, commentId, listDialog);
              //      Toast.makeText(pollCommentsAdapterContext,pollCommentsAdapterContext.getResources().getString(R.string.delete_the_comment),Toast.LENGTH_LONG).show();
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
                    Toast.makeText(pollCommentsAdapterContext, pollCommentsAdapterContext.getResources().getString(R.string.enter_the_comments),
                            Toast.LENGTH_SHORT).show();
                } else {
                    campaignCommentsSave(position, commentId, listDialog);
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
    private void campaignCommentsDelete(final int clickPosition, String commentId,final Dialog listDialog) {
        MApplication.materialdesignDialogStart(pollCommentsAdapterContext);//Material dialog starts
        CampaignCommentEditDeleteRestClient.getInstance().postCampaignPollCommentDelete(new String("delete_poll"), new String(pollId), new String((MApplication.getString(pollCommentsAdapterContext, Constants.USER_ID))), new String(commentId), new Callback<CampaignCommentDelete>() {      /**  Requesting the response from the given base url**/
            @Override
            public void success(CampaignCommentDelete campaignCommentDelete, Response response) {
                //If the succes matches with the value 1 then the corresponding row is removed
                //else corresponding row wont remove
                if (("1").equals(campaignCommentDelete.getSuccess())) {
                    Toast.makeText(pollCommentsAdapterContext, campaignCommentDelete.getMessage(), Toast.LENGTH_SHORT).show();
                    dataResults.remove(clickPosition);//remove the particular cell
                    prefrenceUserPollCommentsCount = MApplication.loadArray(pollCommentsAdapterContext, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE);    //Prefernce comment count is stored in array list
                    prefrenceUserPollCommentsCount.set(commentsCountPosition, Integer.valueOf(campaignCommentDelete.getCount()));  //Changing the particular position value
                    MApplication.saveArray(pollCommentsAdapterContext, prefrenceUserPollCommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE);  //Saving again into the prefernce
                    //Notifies the attached observers that the underlying data has been changed
                    notifyDataSetChanged();   // and any View reflecting the data set should refresh itself.
                    listDialog.dismiss();  //dismiss the dialog
                }
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                MApplication.errorMessage(retrofitError, pollCommentsAdapterContext);//Error message popups when the user cannot able to coonect with the server
            }
        });
        MApplication.materialdesignDialogStop();  //Progress bar stops
    }

    /**
     * This method is used to save the comment
     * @param position
     * @param commentId
     * @param listDialog
     */
    private void campaignCommentsSave(final int position, String commentId, final Dialog listDialog) {
        MApplication.materialdesignDialogStart(pollCommentsAdapterContext);  //Material dialog starts
        CampaignCommentEditDeleteRestClient.getInstance().postCampaignPollCommentEdit(new String("edit_poll"), new String(pollId), new String((MApplication.getString(pollCommentsAdapterContext, Constants.USER_ID))), new String(commentId), new String(mCampaignComments), new Callback<CommentDisplayResponseModel>() {    /**  Requesting the response from the given base url**/
            @Override
            public void success(CommentDisplayResponseModel campaignCommentEdit, Response response) {
                //If the succes matches with the value 1 then the corresponding row is modified
                //else corresponding row wont remove
                if (("1").equals(campaignCommentEdit.getSuccess())) {
                    listDialog.dismiss();//list dialoq dismiss
                    dataResults.set(position, campaignCommentEdit.getResults().getData().get(0));//adding thed etails in prefernce
                    prefrenceUserPollCommentsCount = MApplication.loadArray(pollCommentsAdapterContext, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE);  //Prefernce comment count is stored in array list
                    prefrenceUserPollCommentsCount.set(commentsCountPosition, Integer.valueOf(campaignCommentEdit.getCount()));   //Changing the particular position value
                    MApplication.saveArray(pollCommentsAdapterContext, prefrenceUserPollCommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE); //Saving again into the prefernce
                    notifyDataSetChanged();//Notifies the attached observers that the underlying data has been changed
                    // and any View reflecting the data set should refresh itself.
                }
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                MApplication.errorMessage(retrofitError, pollCommentsAdapterContext);  //Error message popups when the user cannot able to coonect with the server
            }
        });
        MApplication.materialdesignDialogStop();//Progress bar stops
    }
}