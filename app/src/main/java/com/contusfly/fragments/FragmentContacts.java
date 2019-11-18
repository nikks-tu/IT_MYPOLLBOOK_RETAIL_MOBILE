/**
 * @category ContusMessanger
 * @package com.contusfly.fragments
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.contusfly.MApplication;
import com.contusfly.activities.ActivityChatView;
import com.contusfly.adapters.AdapterContacts;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.utils.Constants;
import com.contusfly.utils.Utils;
import com.polls.polls.R;

import java.util.List;

/**
 * The Class FragmentContacts.
 */
public class FragmentContacts extends Fragment {

    /**
     * The rosters list.
     */
    private List<Rosters> rostersList;

    /**
     * The adapter contacts.
     */
    private AdapterContacts adapterContacts;

    /**
     * The context.
     */
    private Activity context;
    private View footerView;

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
        footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.empty_footer, null, false);
        listContacts.addFooterView(footerView);
        adapterContacts = new AdapterContacts(context);
        rostersList = MDatabaseHelper.getRosterInfo(Constants.EMPTY_STRING,
                String.valueOf(Constants.COUNT_ZERO));

        for (int i = 0; i < rostersList.size(); i++) {
            if (rostersList.get(i).getRosterName().isEmpty()) {
                rostersList.remove(i);
            }
        }
        adapterContacts.setData(rostersList);
        listContacts.setAdapter(adapterContacts);
        ///Add a fixed view to appear at the bottom of the list.
        TextView emptyView = (TextView) view.findViewById(R.id.empty_view);
        emptyView.setText(getString(R.string.txt_no_results));
        listContacts.setEmptyView(emptyView);
        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleOnItemClick(position);
            }
        });
        ((MApplication) context.getApplication()).startLoginService(context);
    }

    /**
     * Handle on item click.
     *
     * @param position the position
     */
    private void handleOnItemClick(int position) {
        if (rostersList.size() > position) {
            String rosterAvailability = rostersList.get(position).getRosterAvailability();
            if (rosterAvailability.isEmpty() || rosterAvailability
                    .equalsIgnoreCase(String.valueOf(Constants.ROSTER_NOT_ACTIVE))) {
                Utils.sendMsgToContact(context, rostersList.get(position).getRosterID());
            } else {
                startActivity(new Intent(context, ActivityChatView.class).putExtra(
                        Constants.ROSTER_USER_ID,
                        rostersList.get(position).getRosterID()).putExtra(
                        Constants.CHAT_TYPE,
                        String.valueOf(Constants.CHAT_TYPE_SINGLE)));
            }
        }
    }

    /**
     * Filter list.
     *
     * @param filterKey the filter key
     */
    public void filterList(String filterKey) {
        adapterContacts.filter(filterKey);
        adapterContacts.notifyDataSetChanged();
    }

    /**
     * Refresh contacts.
     */
    public void refreshContacts() {

        rostersList = MDatabaseHelper.getRosterInfo(Constants.EMPTY_STRING,
                String.valueOf(Constants.COUNT_ZERO));
        Log.e("rostersList", rostersList.size() + "");
        if (!rostersList.isEmpty()) {
            for (int i = 0; i < rostersList.size(); i++) {
                if (rostersList.get(i).getRosterName().isEmpty()) {
                    rostersList.remove(i);
                }
            }
            adapterContacts.setData(rostersList);
            adapterContacts.notifyDataSetChanged();
        }
    }

    /**
     * Gets the rosters list.
     *
     * @return the rosters list
     */
    public List<Rosters> getRostersList() {
        return rostersList;
    }
}