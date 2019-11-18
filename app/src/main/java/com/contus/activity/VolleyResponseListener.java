package com.contus.activity;

import android.app.AlertDialog;

import org.json.JSONObject;

/*-----------------------------------------------------------------------------
Class Name: Mysingleton
Created By: Ramakrishna
Created Date: 15-05-2017
Modified By:
Modified Date:
Purpose: This interface is used for to chatch onError and onResponce with server
-----------------------------------------------------------------------------*/

public interface VolleyResponseListener {
    void onError(String message);

        void onResponse(JSONObject response);
}