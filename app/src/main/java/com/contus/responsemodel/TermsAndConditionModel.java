/**
 * TermsAndConditionModel.java
 *
 * Model class for tearms and condition response
 *
 * @category   Contus
 * @package    com.contus.activity
 * @version    1.0
 * @author     Contus Team <developers@contus.in>
 * @copyright  Copyright (C) 2015 Contus. All rights reserved.
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */

package com.contus.responsemodel;
/**
 * Created by user on 8/4/2015.
 */
public class TermsAndConditionModel {
    /**String success**/
    private String success;
    /**Class details**/
    Details results;


    /**
     * Get the success
     * @return
     */
    public String getSuccess() {
        return success;
    }


    /**
     * Getting the results  from the class details
     * @return results value
     */
    public Details getResults() {
        return results;
    }


    /**
     * The Class UserDetails.
     */
    public class Details{
        /** The user_subscription. */
        private String information;
        public String getInformation() {
            return information;
        }


    }

}
