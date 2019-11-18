package com.contusfly.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.contus.app.Constants;
import com.contus.responsemodel.PollPrivateResponseModel;
import com.contus.restclient.PrivatePollRestClient;
import com.contusfly.MApplication;
import com.contusfly.privatepolls.PrivatePolls;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.polls.polls.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 1/19/2016.
 */
public class PollsFragment extends Fragment {
    //Root
    private ViewGroup root;
    //Listview
    private ListView listView;
    //Current date and time
    private String currentDateandTime;
    //User id
    private String userId;
    //Poll private adapter
    public PollPrivateAdapter pollPrivateAdapter;
    //Simple date format
    private SimpleDateFormat sdf;
    private TextView noPollFound;
    public List<PollPrivateResponseModel.Results> pollResults;
    private View footerView;
    private Activity userPollsFragment;
    private SmoothProgressBar google_now;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.activity_share_polls, null);
        google_now = root.findViewById(R.id.google_now);
        listView = root.findViewById(R.id.listView);
        noPollFound = root.findViewById(R.id.noPollFound);
        footerView = ((LayoutInflater) userPollsFragment.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.empty_footer, null, false);
        ///Add a fixed view to appear at the bottom of the list.
        listView.addFooterView(footerView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent a=new Intent(userPollsFragment, PrivatePolls.class);
                a.putExtra("type",pollResults.get(i).getType());
                a.putExtra("id",pollResults.get(i).getId());
                startActivity(a);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        //date format
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //current date and time
        currentDateandTime = sdf.format(new Date());
        //Getting the user id
        userId = MApplication.getString(userPollsFragment, Constants.USER_ID);
        //Serv er request
        serverRequest();
    }

    private void serverRequest() {
        if (MApplication.isNetConnected(userPollsFragment)) {
            google_now.progressiveStart();
            MApplication.materialdesignDialogStart(userPollsFragment);
            /**  Requesting the response from the given base url**/
            PrivatePollRestClient.getInstance().postPrivatePoll(new String("getPrivatePolls"), new String(userId), new String(currentDateandTime)
                    , new Callback<PollPrivateResponseModel>() {
                        @Override
                        public void success(PollPrivateResponseModel pollPrivateResponseModel, Response response) {
                            pollResults = pollPrivateResponseModel.getResults();
                            //If success equals to 1
                            if (("1").equals(pollPrivateResponseModel.getSuccess())) {
                                listView.setVisibility(View.VISIBLE);
                                //Passing the response to the adapter
                                pollPrivateAdapter = new PollPrivateAdapter(userPollsFragment, pollPrivateResponseModel.getResults());
                                //Setting the adapter
                                listView.setAdapter(pollPrivateAdapter);
                                noPollFound.setVisibility(View.INVISIBLE);
                            } else if (("0").equals(pollPrivateResponseModel.getSuccess())) {
                                noPollFound.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.INVISIBLE);
                            }
                            google_now.progressiveStop();
                            MApplication.materialdesignDialogStop();
                        }
                        @Override
                        public void failure(RetrofitError retrofitError) {
                            /**Error message will display when not able to connec t to the server**/
                            MApplication.errorMessage(retrofitError, userPollsFragment);
                            MApplication.materialdesignDialogStop();
                        }
                    });
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView=(ListView)root.findViewById(R.id.listView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof Activity) {
            userPollsFragment = (Activity) context;
        }

    }
}
