package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by user on 8/13/2015.
 */
public class LocationResponseModel {
    //Results
    private ArrayList<LocationDetails> results;
    /**
     * Get the results
     * @return results
     */
    public ArrayList<LocationDetails> getResults() {
        return results;
    }
    /**
     * LocationDetails
     */
    public class LocationDetails{
        //Formateed address
        @SerializedName("formatted_address")
        private String formattedAddress;
        //Geomentry
        private LocationDetails.LatLong geometry;
        /**
         * getFormattedAddress
         * @return formattedAddress
         */
        public String getFormattedAddress() {
            return formattedAddress;
        }

        /**
         * getGeometry
         * @return geometry
         */
        public LatLong getGeometry() {
            return geometry;
        }


        /**
         * Lat Long
         */
        public class LatLong{
            //Location
            private LocationDetails.LatLong.Details location;
            /**
             * getLocation
             * @return location
             */
            public Details getLocation() {
                return location;
            }


            /**
             *Details
             */
            public class Details{
                //lat
                private  String lat;
                //Lng
                private String lng;
                /**
                 * Get lat
                 * @return lat
                 */
                public String getLat() {
                    return lat;
                }
                /**
                 * getLongitude
                 * @return lng
                 */
                public String getLongitude() {
                    return lng;
                }
            }
        }
    }
}
