/**
 * @category ContusMessanger
 * @package com.contusfly.activities
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.activities;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.polls.polls.R;

/**
 * The Class ActivityMapView.
 */
public class ActivityMapView extends FragmentActivity {

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
//        GoogleMap supportMap = ((SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.view_map)).getMap();
//        Intent intent = getIntent();
//        String latitude = intent.getStringExtra(Constants.LATITUDE);
//        Log.e("latitude", latitude + "");
//        String longitude = intent.getStringExtra(Constants.LONGITUDE);
//        Log.e("longitude", longitude + "");
//        LatLng location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
//        supportMap.addMarker(new MarkerOptions().position(location).icon(
//                BitmapDescriptorFactory.fromResource(R.drawable.pin_small)));
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(location).zoom(13f).bearing(360).tilt(15f).build();
//        supportMap.moveCamera(CameraUpdateFactory
//                .newCameraPosition(cameraPosition));
    }
}