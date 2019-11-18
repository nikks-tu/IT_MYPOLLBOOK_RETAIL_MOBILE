/**
 * CountryResponseModel.java
 * <p>
 * Model class for country response
 *
 * @category Contus
 * @package com.contus.activity
 * @version 1.0
 * @author Contus Team <developers@contus.in>
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */

package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 8/3/2015.
 */
public class CountryResponseModel {
    /**String success**/
    private String success;
    /**Custom list class**/
    List<UserDetails> results;

    /**
     *  Get success
     * @return
     */
    public String getSuccess() {
        return success;
    }
    /**
     * Get the results
     * @return results
     */
    public List<UserDetails> getData() {
        return results;
    }


    /**
     * The Class UserDetails.
     */
    public class UserDetails {
        /** The user_subscription. */
        @SerializedName("country_name")
        private String countryName;
        //Country phone code
        @SerializedName("country_phonecode")
        private String countryPhonecode;
        //Country id
        @SerializedName("country_id")
        private String countryId;
        //Country code
        @SerializedName("country_code")
        private String countryCode;
        /**
         * Get the countryName
         * @return countryName
         */
        public String getCountryName() {
            return countryName;
        }
        /**
         * Get the countryPhonecode
         * @return countryPhonecode
         */
        public String getCountryPhonecode() {
            return countryPhonecode;
        }
        /**
         * Get the countryId
         * @return countryId
         */
        public String getCountryId() {
            return countryId;
        }
        /**
         * Get the countryCode
         * @return countryCode
         */
        public String getCountryCode() {
            return countryCode;
        }
    }


}


