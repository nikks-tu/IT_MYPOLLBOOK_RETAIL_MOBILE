package com.contus.activity;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/*-----------------------------------------------------------------------------
Class Name: Mysingleton
Created By: Ramakrishna
Created Date: 15-05-2017
Modified By:
Modified Date:
Purpose: This Class is used for put server requists in queue
-----------------------------------------------------------------------------*/
public class Mysingleton {
    private static Mysingleton mysingleton;
    private RequestQueue requestQueue;
    private static Context mCtxt;


    private Mysingleton(Context context){
        mCtxt = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized Mysingleton getMysingleton(Context context){

        if(mysingleton==null){
            mysingleton = new Mysingleton(context);
        }
        return mysingleton;

    }

    public RequestQueue getRequestQueue(){

        if(requestQueue == null){

            requestQueue = Volley.newRequestQueue(mCtxt.getApplicationContext());

        }
        return requestQueue;
    }

    public <T>void addTorequestqueue(Request<T> request){
        requestQueue.add(request);
    }
    public <T>void delTorequestqueue() {
        requestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }
}
