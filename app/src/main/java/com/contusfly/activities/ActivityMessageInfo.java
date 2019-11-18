/**
 * @category ContusMessanger
 * @package com.time.activities
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.contusfly.adapters.AdapterMsgInfo;
import com.contusfly.model.GroupMsgStatus;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.utils.Constants;
import com.contusfly.utils.Utils;
import com.contusfly.utils.WrapContentLayoutManager;
import com.contusfly.views.CustomRecyclerView;
import com.polls.polls.R;

import java.util.List;


/**
 * The Activity class which acts as the base class for all other Activity classes for this
 * application, All other activities must extend this class so that they can receive the callbacks
 * of the chat service through the overriding methods of other this class all the callbacks
 * will be received from the broadcast receiver of this class.
 *
 * @author ContusTeam <developers@contus.in>
 * @version 1.1
 */
public class ActivityMessageInfo extends ActivityBase {

    private AdapterMsgInfo mDeliveredAdapter,mSeenAdapter;

    private List<GroupMsgStatus> deliveryStatus,readStatus;

    private Intent intent;

    private String receivers;

    private CustomRecyclerView listReadBy, listDelivers;

    private int count;

    private TextView txtReadCounts, txtDeliversCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_info);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utils.setUpToolBar(this, toolbar, getSupportActionBar(), getString(R.string.text_msg_info));
        txtReadCounts = (TextView) findViewById(R.id.txt_read_rem);
        txtDeliversCount = (TextView) findViewById(R.id.txt_del_rem);

        listReadBy = (CustomRecyclerView) findViewById(R.id.list_read_by);
        listReadBy.setLayoutManager(new WrapContentLayoutManager(this));
        listDelivers = (CustomRecyclerView) findViewById(R.id.list_delivers);
        listDelivers.setLayoutManager(new WrapContentLayoutManager(this));
        mDeliveredAdapter = new AdapterMsgInfo(this);
        mSeenAdapter = new AdapterMsgInfo(this);
        intent = getIntent();
        receivers = intent.getStringExtra(Constants.ROSTER_GROUP_USERS);
        deliveryStatus = MDatabaseHelper
                .getGroupMsgDetails(intent.getStringExtra(Constants.CHAT_MSG_ID), Constants.CHAT_REACHED_TO_RECEIVER, receivers);
        readStatus = MDatabaseHelper
                .getGroupMsgDetails(intent.getStringExtra(Constants.CHAT_MSG_ID), Constants.CHAT_SEEN_BY_RECEIVER, receivers);
        mDeliveredAdapter.setData(deliveryStatus, true);
        listDelivers.setAdapter(mDeliveredAdapter);
        mSeenAdapter.setData(readStatus, false);
        listReadBy.setAdapter(mSeenAdapter);
        count = TextUtils.split(receivers, ",").length;
        setCount();
    }

    private void setCount() {
        if (!deliveryStatus.isEmpty()) {
            listDelivers.setVisibility(View.VISIBLE);
        }else{
            listDelivers.setVisibility(View.GONE);
        }
        if (!readStatus.isEmpty()) {
            listReadBy.setVisibility(View.VISIBLE);
        }else{
            listReadBy.setVisibility(View.GONE);
        }
        String readCount = String.valueOf(count - readStatus.size()) + getString(R.string.txt_remaining);
        txtReadCounts.setText(readCount);

        String deliveryCount = String.valueOf(count - deliveryStatus.size() - readStatus.size())
                + getString(R.string.txt_remaining);
        txtDeliversCount.setText(deliveryCount);
    }

    @Override
    protected void msgReceiptCallBack(Intent chatData) {
        setAdapter();
        setCount();
    }

    private void setAdapter() {
        deliveryStatus = MDatabaseHelper
                .getGroupMsgDetails(intent.getStringExtra(Constants.CHAT_MSG_ID), Constants.CHAT_REACHED_TO_RECEIVER, receivers);
        readStatus = MDatabaseHelper
                .getGroupMsgDetails(intent.getStringExtra(Constants.CHAT_MSG_ID), Constants.CHAT_SEEN_BY_RECEIVER, receivers);
        mDeliveredAdapter.setData(deliveryStatus, true);
        mSeenAdapter.setData(readStatus, false);
        mDeliveredAdapter.notifyDataSetChanged();
        mSeenAdapter.notifyDataSetChanged();
    }

    @Override
    protected void msgSeenCallBack(Intent chatData) {
        setAdapter();
        setCount();
    }
}
