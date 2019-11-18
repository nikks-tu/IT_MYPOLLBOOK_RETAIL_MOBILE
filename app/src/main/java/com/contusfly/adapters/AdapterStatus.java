/**
 * @category   ContusMessanger
 * @package    com.contusfly.adapters
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.contusfly.views.CustomTextView;
import com.polls.polls.R;

import java.net.URLDecoder;
import java.util.List;

/**
 * The Class AdapterStatus.
 */
public class AdapterStatus extends BaseAdapter {

    /** The inflater. */
    private LayoutInflater inflater;

    /** The status list. */
    private List<String> statusList;

    /**
     * Instantiates a new adapter status.
     *
     * @param context
     *            the context
     */
    public AdapterStatus(Context context) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    public void setStatus(List<String> status) {
        statusList = status;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return statusList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public String getItem(int position) {
        return statusList.get(position);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        CustomTextView txtView;
        if (view == null)
            view = inflater.inflate(R.layout.row_status, null);
        txtView = (CustomTextView) view.findViewById(R.id.rowTextView);
        try {
            String status = getItem(position);
            status = URLDecoder.decode(status, "UTF-8").replaceAll("%", "%25");
            txtView.setText(status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return view;
    }
}
