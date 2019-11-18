/**
 * @category   ContusMessanger
 * @package    com.contusfly.utils
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.contusfly.views.DoProgressDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * The Class GetLocation.
 */
public class GetLocation implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /** The m google api client. */
    private GoogleApiClient mGoogleApiClient;

    /** The location manager. */
    private LocationManager locationManager;

    /** The service provider. */
    private String serviceProvider;

    /** The m last location. */
    private Location mLastLocation;

    /** The context. */
    private Context context;

    /** The listener. */
    private UserLocationListener listener;

    /** The progress dialog. */
    private DoProgressDialog progressDialog;

    /**
     * The listener interface for receiving userLocation events. The class that
     * is interested in processing a userLocation event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>addUserLocationListener<code> method. When
     * the userLocation event occurs, that object's appropriate
     * method is invoked.
     * 
     * @see
     */
    public interface UserLocationListener {

        /**
         * On location found.
         * 
         * @param location
         *            the location
         */
        void onLocationFound(Location location);
    }

    /**
     * Instantiates a new gets the location.
     * 
     * @param context
     *            the context
     */
    public GetLocation(Context context) {
        this.context = context;
    }

    /**
     * Sets the location listener.
     * 
     * @param listener
     *            the new location listener
     */
    public void setLocationListener(UserLocationListener listener) {
        this.listener = listener;
    }

    /**
     * Setup location service.
     */
    public void setupLocationService() {
        progressDialog = new DoProgressDialog(context);
        progressDialog.showProgress();
        new LocationTask().execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if (listener != null)
                    listener.onLocationFound(mLastLocation);
            }
        }, 2000);
    }

    /**
     * The Class LocationTask.
     */
    private class LocationTask extends AsyncTask<Void, Void, Void> {

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Void doInBackground(Void... voids) {
            locationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            serviceProvider = locationManager.getBestProvider(criteria, true);
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(GetLocation.this)
                    .addOnConnectionFailedListener(GetLocation.this)
                    .addApi(LocationServices.API).build();
            mGoogleApiClient.connect();
            return null;
        }
    }

    /**
     * On connected.
     * 
     * @param bundle
     *            the bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    /**
     * On connection suspended.
     * 
     * @param i
     *            the i
     */
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.location.LocationListener#onLocationChanged(android.location.
     * Location)
     */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.location.LocationListener#onStatusChanged(java.lang.String,
     * int, android.os.Bundle)
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.location.LocationListener#onProviderEnabled(java.lang.String)
     */
    @Override
    public void onProviderEnabled(String provider) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.location.LocationListener#onProviderDisabled(java.lang.String)
     */
    @Override
    public void onProviderDisabled(String provider) {
    }

    /**
     * On connection failed.
     * 
     * @param connectionResult
     *            the connection result
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    /**
     * Start location updates.
     */
    private void startLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(serviceProvider, 0, 0, this);
            displayLocation();
        } catch (Exception e) {
            LogMessage.e(Constants.TAG,e+"");
        }
    }

    /**
     * Dis connect location services.
     */
    public void disConnectLocationServices() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    /**
     * Display location.
     */
    private void displayLocation() {
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

}
