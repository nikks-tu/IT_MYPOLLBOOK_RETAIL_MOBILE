package com.contus.activity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.polls.polls.R;

/**
 * Created by user on 8/5/2015.
 */
public class CustomDialogAdapter extends BaseAdapter {
    //Camera options
    private final String[] cameraOptions;
   //Context
    Context context;
   //View holder
    private ViewHolder holder;
    //view
    private View mCustomDialogAdapter;

    /**
     *initializes a new instance of the ListView class.
     * @param context-activiy class
     * @param cameraOptions--string array
     */
    public CustomDialogAdapter(Context context, String[] cameraOptions) {
        this.context = context;
        this.cameraOptions = cameraOptions;
    }

    @Override
    public View getView(int position, View mView, ViewGroup parent) {
        holder = null;
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //view
        mCustomDialogAdapter=mView;
        // The old view to reuse, if possible.If convertView is NOT null, we can simple re-use the convertView as the new View.
        // It will happen when a new row appear and a old row in the other end roll out.
        if (mCustomDialogAdapter == null) {
        /* create a new view of my layout and inflate it in the row */
            mCustomDialogAdapter = mInflater.inflate(R.layout.custom_countrylist_singleview, null);
            holder = new ViewHolder();
            holder.country = (TextView) mCustomDialogAdapter.findViewById(R.id.country);
            mCustomDialogAdapter.setTag(holder);
        } else {
            holder = (ViewHolder) mCustomDialogAdapter.getTag();
        }
        //Setting the cameraOptions in text view
        holder.country.setText(cameraOptions[position]);
        //Return views
        return mCustomDialogAdapter;
    }

    @Override
    public int getCount() {
        return cameraOptions.length;
    }

    @Override
    public Object getItem(int position) {
        return cameraOptions.length;
    }

    @Override
    public long getItemId(int position) {
        return cameraOptions.length;
    }

    /**
     * A ViewHolder object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder {
        /*Textview**/
        private TextView country;

    }
}