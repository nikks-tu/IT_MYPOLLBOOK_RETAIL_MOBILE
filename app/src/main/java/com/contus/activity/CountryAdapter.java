package com.contus.activity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.contus.responsemodel.CountryResponseModel;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 7/20/2015.
 */
public class CountryAdapter extends ArrayAdapter<CountryResponseModel.UserDetails> {
    // String array country*
    private final CountryResponseModel country;
    // Context
    Context context;
    //  Viewholder
    private ViewHolder holder;
    //Country details in array
    private List<CountryResponseModel.UserDetails> arrayList;
    //tempory items
    private final ArrayList<CountryResponseModel.UserDetails> tempItems;
    //view
    private View mAdapterView;
    //country name
    private String countryName;

    /**
     * initializes a new instance of the ListView class.
     *
     * @param countryActivity-actvity class
     * @param countryResponseModel- response from the server
     * @param arrayList-response from the server is stored as arraylist
     *
     */
    public CountryAdapter(CountryActivity countryActivity, List<CountryResponseModel.UserDetails> arrayList, int customCountrylistSingleview, CountryResponseModel countryResponseModel) {
        super(countryActivity, customCountrylistSingleview, arrayList);
        this.context = countryActivity;
        this.arrayList = arrayList;
        this.country=countryResponseModel;
        tempItems = new ArrayList<CountryResponseModel.UserDetails>(arrayList); // this makes the difference.
    }

    @Override
    public View getView(int position, View mView, ViewGroup parent) {
        holder = null;
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mAdapterView=mView;
        // The old view to reuse, if possible.If convertView is NOT null, we can simple re-use the convertView as the new View.
        // It will happen when a new row appear and a old row in the other end roll out.
        if (mAdapterView == null) {
            /* create a new view of my layout and inflate it in the row */
            mAdapterView = mInflater.inflate(R.layout.custom_countrylist_singleview, null);
            holder = new ViewHolder();
            holder.country = (TextView) mAdapterView.findViewById(R.id.country);
            holder.phoneCode = (TextView) mAdapterView.findViewById(R.id.countryCode);
            mAdapterView.setTag(holder);
        } else {
            holder = (ViewHolder) mAdapterView.getTag();
        }
        //Setting the country name in text view
        holder.country.setText(arrayList.get(position).getCountryName());
        //Setting the phone code in text view
        holder.phoneCode.setText("+" + arrayList.get(position).getCountryPhonecode());
      //Return views
        return mAdapterView;
    }

    @Override
    public int getCount() {
        return country.getData().size();
    }

    // Filter Class
    public void filter(String typedString) {
        countryName = typedString.toLowerCase(Locale.getDefault());
        arrayList.clear();
        if (countryName.length() == 0) {
            arrayList.addAll(tempItems);
        }
        else
        {
            for (CountryResponseModel.UserDetails wp : tempItems)
            {
                if (wp.getCountryName().toLowerCase(Locale.getDefault()).contains(typedString))
                {
                    arrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * A ViewHolder object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder {
        /*Textview**/
        private TextView country;
         //Phone code
        private TextView phoneCode;
    }
}
