package com.contusfly.fragments;

import android.app.Activity;
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

import com.contusfly.MApplication;
import com.contusfly.activities.ActivityChatView;
import com.contusfly.adapters.AdapterRecentChat;
import com.contusfly.model.ChatMsg;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.RecentChat;
import com.contusfly.model.Rosters;
import com.contusfly.utils.Constants;
import com.contusfly.views.CustomToast;
import com.polls.polls.R;

import java.util.List;

/**
 * Created by user on 11/13/2015.
 */
public class FragmentShareToRecent extends Fragment {

    /**
     * The recent chats.
     */
    private List<RecentChat> chatList;

    /**
     * The adapter recent chat.
     */
    private AdapterRecentChat adapterChat;

    /**
     * The context.
     */
    private Activity context;

    /**
     * The list recent.
     */
    private ListView listView;

    /**
     * The m application.
     */
    private MApplication mApplication;

    public static FragmentShareToRecent getInstance(ChatMsg msg) {
        FragmentShareToRecent fragmentShareToRecent = new FragmentShareToRecent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.CHAT_MESSAGE, msg);
        fragmentShareToRecent.setArguments(bundle);
        return fragmentShareToRecent;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onViewCreated(android.view.View,
     * android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
    }

    /**
     * Inits the views.
     *
     * @param view the view
     */
    private void initViews(View view) {
        context = getActivity();
        mApplication = (MApplication) context.getApplicationContext();
        listView = (ListView) view.findViewById(R.id.list_contacts);
        adapterChat = new AdapterRecentChat(context);
        final ChatMsg msg = getArguments().getParcelable(Constants.CHAT_MESSAGE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openChatView(position, msg);
            }
        });
        TextView emptyView = (TextView) view.findViewById(R.id.empty_view);
        emptyView.setText(getString(R.string.txt_no_chats));
        listView.setEmptyView(emptyView);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        setChatAdapter();
    }

    /**
     * Sets the chat adapter.
     */
    public void setChatAdapter() {
        try {
            chatList = MDatabaseHelper.getRecentChatHistory();
            adapterChat.setData(chatList);
            listView.setAdapter(adapterChat);
            if (chatList.isEmpty()) {
                listView.setVisibility(View.GONE);
            } else
                listView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Filter list.
     *
     * @param filterKey the filter key
     */
    public void filterList(String filterKey) {
        adapterChat.filter(filterKey);
        adapterChat.notifyDataSetChanged();
    }

    /**
     * Open chat view.
     *
     * @param position the position
     */
    private void openChatView(int position, ChatMsg msg) {
        try {
            RecentChat item = chatList.get(position);
            String chatType = MApplication.returnEmptyStringIfNull(item
                    .getRecentChatType());
            String userID = item.getRecentChatId();
            List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(userID,
                    Constants.EMPTY_STRING);
            if (!rosterInfo.isEmpty()) {
                String status = MApplication.returnEmptyStringIfNull(rosterInfo.get(0).getRosterGroupStatus());
                if (chatType.equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_GROUP)) &&
                        status.equalsIgnoreCase(String.valueOf(Constants.COUNT_ZERO)))
                    openChatView(userID, chatType, msg);
                else if (chatType.equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_SINGLE)))
                    openChatView(userID, chatType, msg);
                else
                    CustomToast.showToast(context, getString(R.string.text_forward_error));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openChatView(String userID, String chatType, ChatMsg msg) {
        Intent mIntent = new Intent(context, ActivityChatView.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mIntent.putExtra(
                Constants.ROSTER_USER_ID, userID).putExtra(Constants.CHAT_TYPE,
                chatType).putExtra(Constants.CHAT_MESSAGE, msg));
        context.finish();
    }

}

