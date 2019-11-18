package com.contus.search;

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
 * Created by user on 12/17/2015.
 */
public class SearchPollCommentsAdapter extends ArrayAdapter<CommentDisplayResponseModel.Results.Data> {
    //Dialog
    private Dialog searchlistDialog;
    //edit text
    private EditText editCampaignComments;
    //Button delete,save
    private Button txtSearchCampaignCommentsDelete, txtSearchCampaignCommnetsSave;
    //Comments count
    private final int commentsCountSearchPosition;
    //Dataresults from the response
    private List<CommentDisplayResponseModel.Results.Data> dataResultsSearch;
    //Casmapingid
    private String searchPollId;
    //user id
    private  String userId;
    //Context
    private Activity searchPollCommentsAdapterContext;
    //comment layout
    private final int searchPollCommentsLayoutId;
    //user profile
    private String searchPollCommentsUserProfile;
    //Data fromt he response
    private CommentDisplayResponseModel.Results.Data searchPollCommentsData;
    //Campaign COMMENTS
    private String mSearchComments;
    //View holder
    private ViewHolder holder;
    //Campaign poll comemnts count
    private ArrayList<Integer> searchcommentsCount = new ArrayList<Integer>();
    //Comments count from the arraylist
    private ArrayList<Integer> prefrenceSearchCommentsCount;
    //views
    private View mCampaignCommentsPollView;


    /**
     * initializes a new instance of the ListView class.
     *
     * @param activity
     * @param dataResults
     * @param layoutId
     * @param commentsCountPosition
     * @param userId
     */
    public SearchPollCommentsAdapter(Activity activity, List<CommentDisplayResponseModel.Results.Data> dataResults, int layoutId, String id, String userId, int commentsCountPosition) {
        super(activity, 0, dataResults);
        this.searchPollCommentsLayoutId = layoutId;
        this.searchPollCommentsAdapterContext = activity;
        this.dataResultsSearch = dataResults;
        this.searchPollId = id;
        this.userId = userId;
        this.commentsCountSearchPosition = commentsCountPosition;
    }

    @Override
    public View getView(final int position, View mViews, ViewGroup parent) {
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        LayoutInflater mInflater = (LayoutInflater)
                searchPollCommentsAdapterContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mCampaignCommentsPollView=mViews;
        // The old view to reuse, if possible.If convertView is NOT null, we can simple re-use the convertView as the new View.
        // It will happen when a new row appear and a old row in the other end roll out.
        if (mCampaignCommentsPollView == null) {
             /* create a new view of my layout and inflate it in the row */
            mCampaignCommentsPollView = mInflater.inflate(searchPollCommentsLayoutId, parent, false);
            holder = new ViewHolder();
            holder.campaignImageSearch = (ImageView) mCampaignCommentsPollView.findViewById(R.id.imgProfile);
            holder.userNameSearch = (TextView) mCampaignCommentsPollView.findViewById(R.id.txtProfileName);
            holder.searchComments = (TextView) mCampaignCommentsPollView.findViewById(R.id.txtChatDetails);
            holder.imgMore = (ImageView) mCampaignCommentsPollView.findViewById(R.id.imgMore);
            holder.txtCommentTime=(TextView)mCampaignCommentsPollView.findViewById(R.id.txtCommentTime);
            mCampaignCommentsPollView.setTag(holder);
        } else {
            holder = (ViewHolder) mCampaignCommentsPollView.getTag();
        }
        //Geting the particular object from the response based on the position
        searchPollCommentsData = getItem(position);
        //If the user id matches from the response then the edit icon will be visible
        //else edit icon wont be visivble
        if (searchPollCommentsData.getUserId().equals(MApplication.getString(searchPollCommentsAdapterContext, Constants.USER_ID))) {
            holder.imgMore.setVisibility(View.VISIBLE);
        } else {
            holder.imgMore.setVisibility(View.INVISIBLE);
        }
        //Getting the user profiel image
        searchPollCommentsUserProfile = searchPollCommentsData.getUserInfo().getUserProfileImg();
        Utils.loadImageWithGlideProfileRounderCorner(searchPollCommentsAdapterContext,searchPollCommentsUserProfile,holder.campaignImageSearch,R.drawable.placeholder_image);
        //Getting the user name from the response
        holder.userNameSearch.setText(searchPollCommentsData.getUserInfo().getUserName());
        //Getting the COMMENTS and setting in the text view
        holder.searchComments.setText(MApplication.getDecodedString(searchPollCommentsData.getComments()));
        //comment time
        holder.txtCommentTime.setText(MApplication.getHours(searchPollCommentsData.getUpdatedAt()));
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clcik position
                int clickPosition = position;
                //Geting the particular object from the response based on the position
                searchPollCommentsData = getItem(clickPosition);
                //Comments from the response
                String comments = MApplication.getDecodedString(searchPollCommentsData.getComments());
                //COMMENTS id from the response
                String commentId = MApplication.getDecodedString(searchPollCommentsData.getId());
                //Custom dialog to edit and delete the comment
                customCampaignPollCustomDialog(searchPollCommentsAdapterContext, comments, clickPosition, commentId);
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
        private TextView searchComments;
        //Text view user name
        private TextView userNameSearch;
        //Campaign Image
        private ImageView campaignImageSearch;
        //Image view
        private ImageView imgMore;
        //comment time
        private TextView txtCommentTime;
    }


    /**
     * customCampaignPollCustomDialog
     *
     * @param mActivity
     * @param comments
     * @param position
     * @param commentId
     */

    public void customCampaignPollCustomDialog(final Activity mActivity, String comments, final int position, final String commentId) {
        //Creates a dialog window that uses the default dialog theme.
        searchlistDialog = new Dialog(mActivity);
        //Enable extended window features.
        searchlistDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Change the background of this window to a custom Drawable.
        searchlistDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Set the screen content to an explicit view.
        searchlistDialog.setContentView(R.layout.layout_edit_comments);
        //Edit text
        editCampaignComments = (EditText) searchlistDialog.findViewById(R.id.editComments);
        //Setting the comment text from the response in edit text
        editCampaignComments.setText(comments);
        //Delte button
        txtSearchCampaignCommentsDelete = (Button) searchlistDialog.findViewById(R.id.delete);
        //Save buitton
        txtSearchCampaignCommnetsSave = (Button) searchlistDialog.findViewById(R.id.Save);
        //Start the dialog and display it on screen.
        searchlistDialog.show();
        //Register a callback to be invoked when this view is clicked.
        txtSearchCampaignCommentsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User input text is stored into the varaible
                mSearchComments = MApplication.getEncodedString(editCampaignComments.getText().toString().trim());
                //If it is empty  toast message will display to enter the text
                //else request to delete the COMMENTS
                if (TextUtils.isEmpty(mSearchComments)) {
                    Toast.makeText(searchPollCommentsAdapterContext, searchPollCommentsAdapterContext.getResources().getString(R.string.enter_the_comments), Toast.LENGTH_SHORT).show();
                } else {
                    campaignSearchCommentsDelete(position, commentId, searchlistDialog);
                }
            }
        });

        //Register a callback to be invoked when this view is clicked.
        txtSearchCampaignCommnetsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User input text is stored into the varaible
                mSearchComments = MApplication.getEncodedString(editCampaignComments.getText().toString().trim());
                //If it is empty  toast message will display to enter the text
                //else request to delete the COMMENTS
                if (TextUtils.isEmpty(mSearchComments)) {
                    Toast.makeText(searchPollCommentsAdapterContext, searchPollCommentsAdapterContext.getResources().getString(R.string.enter_the_comments),
                            Toast.LENGTH_SHORT).show();
                } else {
                    campaignSearchCommentsSave(position, commentId, searchlistDialog);
                }
            }
        });

    }

    /**
     * campaignSearchCommentsDelete
     *
     * @param clickPosition
     * @param commentId
     * @param listDialog
     */
    private void campaignSearchCommentsDelete(final int clickPosition, String commentId, final Dialog listDialog) {
        MApplication.materialdesignDialogStart(searchPollCommentsAdapterContext); //Material dialog starts
        CampaignCommentEditDeleteRestClient.getInstance().postCampaignPollCommentDelete(new String("delete_poll"), new String(searchPollId), new String((MApplication.getString(searchPollCommentsAdapterContext, Constants.USER_ID))), new String(commentId), new Callback<CampaignCommentDelete>() { /**  Requesting the response from the given base url**/
                    @Override
                    public void success(CampaignCommentDelete campaignCommentDelete, Response response) {
                        //If the succes matches with the value 1 then the corresponding row is removed
                        //else corresponding row wont remove
                        if (("1").equals(campaignCommentDelete.getSuccess())) {
                            dataResultsSearch.remove(clickPosition); //remove the particular cell
                            prefrenceSearchCommentsCount = MApplication.loadArray(searchPollCommentsAdapterContext, searchcommentsCount, Constants.SEARCH_POLL_COMMENTS_COUNT, Constants.SEARCH_POLL_COMMENTS_SIZE);   //Prefernce comment count is stored in array list
                            prefrenceSearchCommentsCount.set(commentsCountSearchPosition, Integer.valueOf(campaignCommentDelete.getCount()));  //Changing the particular position value
                            MApplication.saveArray(searchPollCommentsAdapterContext, prefrenceSearchCommentsCount, Constants.SEARCH_POLL_COMMENTS_COUNT, Constants.SEARCH_POLL_COMMENTS_SIZE);  //Saving again into the prefernce
                            notifyDataSetChanged(); //Notifies the attached observers that the underlying data has been changed
                            // and any View reflecting the data set should refresh itself.
                            listDialog.dismiss();  //dismiss the dialog
                            //Progress bar stops
                            MApplication.materialdesignDialogStop();
                        }
                    }
                    @Override
                    public void failure(RetrofitError retrofitError) {
                        MApplication.errorMessage(retrofitError, searchPollCommentsAdapterContext);  //Error message popups when the user cannot able to coonect with the server
                        //Progress bar stops
                        MApplication.materialdesignDialogStop();
                    }
                });

    }

    /**
     * This method is used to save the comment
     *
     * @param position
     * @param commentId
     * @param listDialog
     */

    private void campaignSearchCommentsSave(final int position, String commentId, final Dialog listDialog) {
        MApplication.materialdesignDialogStart(searchPollCommentsAdapterContext); //Material dialog starts
        CampaignCommentEditDeleteRestClient.getInstance().postCampaignPollCommentEdit(new String("edit_poll"), new String(searchPollId), new String(MApplication.getString(searchPollCommentsAdapterContext, Constants.USER_ID)), new String(commentId), new String(mSearchComments), new Callback<CommentDisplayResponseModel>() {   /**  Requesting the response from the given base url**/
                    @Override
                    public void success(CommentDisplayResponseModel campaignCommentEdit, Response response) {
                        //If the succes matches with the value 1 then the corresponding row is modified
                        //else corresponding row wont remove
                        if (("1").equals(campaignCommentEdit.getSuccess())) {
                            listDialog.dismiss();
                            dataResultsSearch.set(position, campaignCommentEdit.getResults().getData().get(0));
                            prefrenceSearchCommentsCount = MApplication.loadArray(searchPollCommentsAdapterContext, searchcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE);   //Prefernce comment count is stored in array list
                            prefrenceSearchCommentsCount.set(commentsCountSearchPosition, Integer.valueOf(campaignCommentEdit.getCount())); //Changing the particular position value
                            MApplication.saveArray(searchPollCommentsAdapterContext, prefrenceSearchCommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE); //Saving again into the prefernce
                            notifyDataSetChanged();//Notifies the attached observers that the underlying data has been changed
                            // and any View reflecting the data set should refresh itself.
                        }
                    }
            @Override
                    public void failure(RetrofitError retrofitError) {
                        MApplication.errorMessage(retrofitError, searchPollCommentsAdapterContext);  //Error message popups when the user cannot able to coonect with the server
                    }
                });
        MApplication.materialdesignDialogStop(); //Progress bar stops
    }
}