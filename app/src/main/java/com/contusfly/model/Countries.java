/**
 * @category   ContusMessanger
 * @package    com.contusfly.model
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The Class Countries.
 */
public class Countries implements Parcelable {

    /** The Constant CREATOR. */
    public static final Creator<Countries> CREATOR = new Creator<Countries>() {
        @Override
        public Countries createFromParcel(Parcel in) {
            return new Countries(in);
        }

        @Override
        public Countries[] newArray(int size) {
            return new Countries[size];
        }
    };

    /** The country code. */
    private String countryName, countryNo, countryCode;

    /**
     * Gets the creator.
     * 
     * @return the creator
     */
    public static Creator<Countries> getCREATOR() {
        return CREATOR;
    }

    /**
     * Instantiates a new countries.
     * 
     * @param in
     *            the in
     */
    private Countries(Parcel in) {
        countryName = in.readString();
        countryNo = in.readString();
        countryCode = in.readString();
    }

    /**
     * Instantiates a new countries.
     */
    public Countries() {
    }

    /**
     * Gets the country name.
     * 
     * @return the country name
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the country name.
     * 
     * @param countryName
     *            the new country name
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * Gets the country no.
     * 
     * @return the country no
     */
    public String getCountryNo() {
        return countryNo;
    }

    /**
     * Sets the country no.
     * 
     * @param countryNo
     *            the new country no
     */
    public void setCountryNo(String countryNo) {
        this.countryNo = countryNo;
    }

    /**
     * Gets the country code.
     * 
     * @return the country code
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the country code.
     * 
     * @param countryCode
     *            the new country code
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(countryName);
        dest.writeString(countryNo);
        dest.writeString(countryCode);
    }
}
