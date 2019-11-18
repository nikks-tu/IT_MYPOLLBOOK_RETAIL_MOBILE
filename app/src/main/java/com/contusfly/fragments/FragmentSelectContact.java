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

import com.contusfly.activities.ActivityChatView;
import com.contusfly.adapters.AdapterContacts;
import com.contusfly.model.ChatMsg;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.utils.Constants;
import com.polls.polls.R;

import java.util.List;

/**
 * Created by user on 11/13/2015.
 */
public class FragmentSelectContact extends Fragment {

    /**
     * The rosters list.
     */
    private List<Rosters> contactsList;

    /**
     * The adapter contacts.
     */
    private AdapterContacts adapterView;

    /**
     * The context.
     */
    private Activity context;

    public static FragmentSelectContact getInstance(ChatMsg msg) {
        FragmentSelectContact fragmentSelectContact = new FragmentSelectContact();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.CHAT_MESSAGE, msg);
        fragmentSelectContact.setArguments(bundle);
        return fragmentSelectContact;
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
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    /**
     * Inits the views.
     *
     * @param view the view
     */
    private void initViews(View view) {
        context = getActivity();
        final ListView listContacts = (ListView) view
                .findViewById(R.id.list_contacts);
        adapterView = new AdapterContacts(context);
        contactsList = MDatabaseHelper.getOnlineContacts();
        adapterView.setData(contactsList);
        listContacts.setAdapter(adapterView);
        final ChatMsg msg = getArguments().getParcelable(Constants.CHAT_MESSAGE);
        TextView emptyView = (TextView) view.findViewById(R.id.empty_view);
        emptyView.setText(getString(R.string.txt_no_results));
        listContacts.setEmptyView(emptyView);

        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleOnItemClick(position, msg);
            }
        });
    }

    /**
     * Handle on item click.
     *
     * @param position the position
     */
    private void handleOnItemClick(int position, ChatMsg msg) {
        startActivity(new Intent(context, ActivityChatView.class).putExtra(
                Constants.ROSTER_USER_ID,
                contactsList.get(position).getRosterID()).putExtra(
                Constants.CHAT_TYPE,
                String.valueOf(Constants.CHAT_TYPE_SINGLE)).putExtra(Constants.CHAT_MESSAGE, msg)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        context.finish();
    }

    /**
     * Filter list.
     *
     * @param filterKey the filter key
     */
    public void filterList(String filterKey) {
        adapterView.filter(filterKey);
        adapterView.notifyDataSetChanged();
    }

}
