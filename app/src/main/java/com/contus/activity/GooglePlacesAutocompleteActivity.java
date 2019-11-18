package com.contus.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contusfly.MApplication;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.google.android.gms.maps.model.LatLng;
import com.polls.polls.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;



public class GooglePlacesAutocompleteActivity extends Activity implements OnItemClickListener {

    private static final String LOG_TAG = "Google Places Autocomplete";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";

    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyAnn1LY0Ro3A2r0_mALOOuHu1oQybN3HAA";

    private static final String GET_LAT_LONG = "http://maps.google.com/maps/api/geocode/json?address=mumbai&sensor=false";

    private static final String ADDRESS ="";

    private static SmoothProgressBar googleNow;

    private ArrayList resultList;

    private GooglePlacesAutocompleteActivity mLocationActivity;
    GooglePlacesAutocompleteAdapter adapter;

@Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_google_places_autocomplete);

        mLocationActivity = this;

        resultList = new ArrayList();

        final AutoCompleteTextView autoCompView =  findViewById(R.id.autoCompleteTextView);
        autoCompView.setThreshold(3);

        adapter = new GooglePlacesAutocompleteAdapter(this, R.layout.list_item);

        autoCompView.setAdapter(adapter);

        googleNow  = findViewById(R.id.google_now);

        autoCompView.setOnItemClickListener(this);

   /*     autoCompView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!autoCompView.getText().equals(""))
                {
                    adapter.notifyDataSetChanged();
                }
                else {
                    resultList = new ArrayList();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int
                    after) {}

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/

    }
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {

        String str = (String) adapterView.getItemAtPosition(position);

     //   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    // Nikita
        getLatitudeLongitude(this, str);

        finish();

    }

    private LatLng getLatitudeLongitude(Context context, String strAddress) {

            Geocoder coder = new Geocoder(context);
            List<Address> address;
            LatLng p1 = null;

            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }

                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude() );

            } catch (IOException ex) {

                ex.printStackTrace();
            }
      //     Toast.makeText(this, p1.toString(), Toast.LENGTH_SHORT).show();

        MApplication.setString(mLocationActivity, Constants.CITY, strAddress);
        //Storing the latitude in preference
        MApplication.setString(mLocationActivity, Constants.LATITUDE, String.valueOf(p1.latitude));
        //Storing the longitude in preference
        MApplication.setString(mLocationActivity, Constants.LONGITUDE, String.valueOf(p1.longitude));
        return p1;
    }

    @SuppressLint("LongLogTag")
    public static ArrayList autocomplete(String input) {

        ArrayList resultList = null;
        HttpURLConnection conn = null;
        googleNow.progressiveStart();

        StringBuilder jsonResults = new StringBuilder();
                try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
           // sb.append("&components=country:gr");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
           // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
             }
        }
        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {

                JSONObject jobj = predsJsonArray.getJSONObject(i).getJSONObject("structured_formatting");
                System.out.println(jobj.getString("main_text"));
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(jobj.getString("main_text"));

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }
        return resultList;
    }
    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }
        @Override
        public int getCount() {
            return resultList.size();
        }
        @Override
        public String getItem(int index) {
            return String.valueOf(resultList.get(index));
                 }
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;

                        filterResults.count = resultList.size();

                    }
                    return filterResults;

                }
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }

                }
            };
            return filter;
        }

    }


    @Override
    protected void onResume() {
/*
        resultList = new ArrayList();

        final AutoCompleteTextView autoCompView =  findViewById(R.id.autoCompleteTextView);
        autoCompView.setThreshold(1);

        adapter = new GooglePlacesAutocompleteAdapter(this, R.layout.list_item);

        autoCompView.setAdapter(adapter);
        autoCompView.setThreshold(1);

        googleNow  = findViewById(R.id.google_now);*/
        super.onResume();
    }
}
